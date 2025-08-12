// @ts-nocheck
import {
    DB_NAME,
    DB_TB_NAME, DISCARD_DB_NAME_ARRAY,
    STATUS_TYPE
} from "@/components/Types";

import {
    Connection
} from "jsstore";
// import first from "lodash/first";
import isArray from "lodash/isArray";
import forEach from "lodash/forEach";
import { getKVValue, KeyValue, setKVValue } from "./dataBaseTable/KeyValue";
import { getKVCacheValue, KeyValueCache, setKVCacheValue } from "./dataBaseTable/KeyValueCache";
import {
    Areacode
} from "./dataBaseTable/Areacode";
import {
    FormDataCache
} from "./dataBaseTable/FormDataCache";
import {
    areaKey
} from "@/utils/dataBaseTable/Areacode";
import {
    checkAreaCodeDB
} from "@/utils/index";
import { isBuffer } from "lodash";


let defaultAdCode = "510000"; //记录父级ID、Name

function getDbSchema() {
    return {
        name: DB_NAME.mydb,
        tables: [Areacode, KeyValue, FormDataCache, KeyValueCache]
    };
}

const getWorkerPath = () => {
    if (process.env.NODE_ENV === "development") {
        return require("file-loader?name=scripts/[name].[hash].js!jsstore/dist/jsstore.worker.js");
    } else {
        return require("file-loader?name=scripts/[name].[hash].js!jsstore/dist/jsstore.worker.min.js");
    }
};
const workerPath = getWorkerPath();

let connection = new Connection(new Worker(workerPath));

let isDbCreated = false;

let whenInitJSStore = {
    deep: [],
    push(resolve) {
        whenInitJSStore.deep.push(resolve);
    },
    trigger(connection) {
        whenInitJSStore.deep.forEach(resolve => resolve(connection));
        whenInitJSStore = null;
    }
};

async function dropDiscardDB() {
    return DISCARD_DB_NAME_ARRAY.map(async (dbName) => {
        let isExist = await connection.isDbExist(dbName);
        if (isExist) {
            await connection.openDb(dbName);
            await connection.dropDb();
        }
        return Promise.resolve();
    });
}

async function initJsStore() {
    if (initJsStore.state === STATUS_TYPE.pendding) {
        return new Promise(resolve => whenInitJSStore.push(resolve));
    } else {
        initJsStore.state = STATUS_TYPE.pendding;
        let database = getDbSchema();
        /* 如果修改了表结构，需要更新数据库 */
        await dropDiscardDB();
        await connection.initDb(database);
        if (!isDbCreated) {
            isDbCreated = true;
        }
        whenInitJSStore.trigger(connection);
        initJsStore.state = STATUS_TYPE.done;
        return connection;
    }
}

initJsStore();


/*获取indexedDB数据库的连接*/
export async function getDBC() {
    if (!isDbCreated) {
        await initJsStore();
    }
    return connection;
}

function makeRangeItem(value, length) {
    return [`${value.substring(0, length)}${"000000".substring(length)}`, value.substring(0, length)];
}


/*返回查找in的数组*/
function getAreacodeRangeByValue(value = "") {
    /*长度不够就补位*/
    /*四川省没有0补全*/
    if (value?.length === 2) {
        return [...makeRangeItem(value, 2)];
    }

    if (value?.length === 4) {
        return [...makeRangeItem(value, 2), ...makeRangeItem(value, 4)];
    }

    if (value?.length === 6) {
        /*有可能后面全是0*/
        if (/\d{2}0000$/.test(value)) {
            return [...makeRangeItem(value, 2)];
        } else if (/\d{4}00$/.test(value)) {
            return [...makeRangeItem(value, 2), ...makeRangeItem(value, 4)];
        } else {
            return [...makeRangeItem(value, 2), ...makeRangeItem(value, 4), value];
        }
    }

    if (value?.length === 8) {
        /*有可能后面全是0*/
        if (/\d{2}000000$/.test(value)) {
            return [...makeRangeItem(value, 2)];
        } else if (/\d{4}0000$/.test(value)) {
            return [...makeRangeItem(value, 2), ...makeRangeItem(value, 4)];
        } else if (/\d{6}00$/.test(value)){
            return [...makeRangeItem(value, 2), ...makeRangeItem(value, 4), value.substring(0,6)];
        } else {
            return [...makeRangeItem(value, 2), ...makeRangeItem(value, 4), value.substring(0,6), value];
        }
    }

    return [];
}

/*返回 三级数据的数组*/
export async function getAreaCodeNodeFromDB<T>(value): Promise<T[]> {
    // debugger;
    /*根据value缓存结果值*/
    const kv_key = `getAreaCodeNodeFromDB_${value}`;
    const cacheValue = await getKVCacheValue(kv_key);
    if ((isArray(cacheValue) && cacheValue.length > 0) || cacheValue) {
        return (cacheValue);
    }
    const _connection = await getDBC();
    await checkAreaCodeDB();
    /*https://jsstore.net/tutorial/in/*/

    const results: T[] = (await _connection.select({
        from: DB_TB_NAME.areacode,
        where: { [areaKey.value]: { in: getAreacodeRangeByValue(value) } },
        order: { by: areaKey.sort, type: "asc" }
    }) as T[]);
    await setKVCacheValue(kv_key, results);
    return results;
}

getAreaCodeNodeFromDB.cache = {};
getAreaCodeNodeFromDB.count = 0;


export async function getAreaCodeNodeById(id: string) {
    const _connection = await getDBC();
    await checkAreaCodeDB();
    /*https://jsstore.net/tutorial/where/*/
    const results = await _connection.select({
        from: DB_TB_NAME.areacode,
        where: {
            [areaKey.value]: id
        },
        order: {
            type: "string",
            by: areaKey.sort,
        }
    });
    return (isArray(results) && results.length === 1) ? results[0] : false;
}

export async function getAreaCodeNodesByName(name: string) {
    const _connection = await getDBC();
    await checkAreaCodeDB();
    /*https://jsstore.net/tutorial/where/*/
    const results = await _connection.select({
        from: DB_TB_NAME.areacode,
        where: {
            [areaKey.name]: {
                like: `%${name}%`
            }
        },
        order: {
            type: "string",
            by: areaKey.sort,
        }
    });
    return isArray(results) ? results : false;
}

export async function getAreaCodeNodeByPid(id) {
    const _connection = await getDBC();
    await checkAreaCodeDB();
    /*https://jsstore.net/tutorial/where/*/
    const results = await _connection.select({
        from: DB_TB_NAME.areacode,
        where: {
            pid: id
        },
        order: {
            type: "string",
            by: areaKey.sort,
        }
    });
    return (isArray(results) && results.length > 0) ? results : [];
}


function fillCode(_all: string[], _areaCode: string) {
    forEach(_areaCode.split(""), (numStr, index) => {
        _all[index] = numStr;
    });
    return _all.join("");
}

export function fillAreaCodeWithZero(areaCode) {
    return areaCode.length > 0 ? fillCode("000000".split(""), String(areaCode)) : defaultAdCode;
}
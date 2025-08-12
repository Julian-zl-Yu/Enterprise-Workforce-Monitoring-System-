// @ts-nocheck
import Cookies from "js-cookie";
import store from "@/store";
import http from "@/utils/http";
import {
    API_URL
} from "@/main.config";
import {
    $$
} from "@/utils/iQuery";
/*lodash*/
import includes from "lodash/includes";
import last from "lodash/last";
import merge from "lodash/merge";
import reduce from "lodash/reduce";
import isArray from "lodash/isArray";
import isString from "lodash/isString";
import filter from "lodash/filter";
/*lodash*/
import Element from "element-ui";
import {
    getDBC
} from "@/utils/dataBase.ts";
import _changeStyle from "./changeStyle";
import {
    SITE_CONFIG
} from "@/main.config";
import {
    getAreaCodeNodeFromDB
} from "@/utils/dataBase.ts";
import {
    getKVValue,
    setKVValue
} from "@/utils/dataBaseTable/KeyValue.ts";
import {
    isURL,
    isBlobURL,
    isRelativeURL
} from "@/utils/validate.ts";
import {
    DB_TB_kv_key,
    DB_TB_NAME,
    STATUS_TYPE
} from "@/components/Types";
import ls from "@/utils/localStorageHelper";
import {
    LS_KEY
} from "@/components/Types";
import { areaKey } from "@/utils/dataBaseTable/Areacode";


window.SITE_CONFIG = SITE_CONFIG;
window.IS_DONE = {};

/**
 * @Description 给静态资源添加domain和token，
 * @date 2020-07-31
 * @param {any} src
 * @returns {any}
 */
export function ensureStaticResourceURL(src) {
    if (!src) return "";
    let targetSrc = "";
    if (isBlobURL(src) || isURL(src)) {
        targetSrc = src;
    } else if (isRelativeURL(src)) {
        targetSrc = `${window.location.origin}/${src.substring(2)}`;
    } else {
        src = `${SITE_CONFIG.apiURL}${src}`;
        targetSrc = addToken(src);
    }
    return targetSrc;
}

/**
 * @Description 判断图片url地址是否有效
 * @date 2021-06-16
 * @param {any} src
 * @returns {Boolean}
 */

export function checkImgExists(imgurl) {
    return new Promise(function (resolve, reject) {
        var ImgObj = new Image();
        ImgObj.src = imgurl;
        ImgObj.onload = function (res) {
            resolve(res);
        }
        ImgObj.onerror = function (err) {
            reject(err)
        }
    })
}

/**
 * @Description 添加静态资源TOKEN
 * @date 2020-08-07
 * @param {any} src
 * @returns {any}
 */
export function addToken(src) {
    return `${src}?token=${sessionStorage.getItem(LS_KEY.token)}`;
}

/**
 * @Description PDF预览添加TOKEN
 * @date 2020-08-07
 * @param {any} src
 * @returns {any}
 */
export function PDFAddToken(src) {
    return `/pdfjs/web/viewer.html?file=${src}&ssltoken=${sessionStorage.getItem(LS_KEY.token)}`;
}

/**
 * @Description 根据民族汉字获取民族代码
 * @date 2020-08-07
 * @param {any} cName
 * @returns {any}
 */
export function getNationByChineseName(cName) {
    if (!getNationByChineseName.nationDict) {
        const nationDictItem = SITE_CONFIG.dictList.filter(dict => "NATION" === dict.dictType)[0];
        getNationByChineseName.nationDict = nationDictItem.dataList;
    }
    return getNationByChineseName.nationDict.filter(dict => dict.dictLabel.trim() === cName.trim())[0].dictValue;
}

export const changeStyle = _changeStyle;

const sleep = async (timeout = 500) => await new Promise((resolve) => setTimeout(resolve, timeout));

/*window.elementUI = Element;*/

/**
 * @Description removeBase64ImageHeader
 * @date 2020-07-31
 * @param {any} dataImg
 * @returns {any}
 */
export function removeBase64ImageHeader(dataImg) {
    return dataImg.replace(/^data:image\/\w+;base64,/, "");
}

/**
 * @Description addBase64ImageHeader
 * @date 2020-08-07
 * @param {any} dataImg
 * @returns {any}
 */
export function addBase64ImageHeader(dataImg) {
    return `data:image/png;base64,${dataImg}`;
}

/* 若res.data的属性值为null ，改以默认数据 */
export function checkFormData(defaultData, data) {
    const remoteData = reduce(data, (target, v, k) => {
        if (v) {
            target[k] = v;
        }
        return target;
    }, {});
    return merge({}, defaultData, remoteData);
}


let whenSysDictTypeAllResolve = {
    deep: [],
    push(resolve) {
        whenSysDictTypeAllResolve.deep.push(resolve);
    },
    trigger(SysDictTypeAll) {
        whenSysDictTypeAllResolve.deep.forEach(resolve => resolve(SysDictTypeAll));
        whenSysDictTypeAllResolve = null;
    }
};

/**
 * @Description 获取字典列表, 添加并保存为全局变量
 * @date 2020-08-05
 * @returns {any}
 */
export async function getSysDictTypeAll() {
    try {
        if (SITE_CONFIG.dictList?.length > 1) {
            return SITE_CONFIG.dictList;
        }
        /* 如果请求已经发送，当前进入队列 */
        if (getSysDictTypeAll?.status === STATUS_TYPE.pendding) {
            /*push 到 queue*/
            return new Promise((resolve) => whenSysDictTypeAllResolve.push(resolve));
        } else {
            getSysDictTypeAll.status = STATUS_TYPE.pendding;
            try {
                let {
                    data: res
                } = await http.get("/sys/dict/type/all");
                SuccessOrFailed(res, {
                    fn: () => {
                        getSysDictTypeAll.status = STATUS_TYPE.done;
                        SITE_CONFIG.dictList = res.data;
                        whenSysDictTypeAllResolve.trigger(SITE_CONFIG.dictList);
                        /* if (SITE_CONFIG.dictList?.length > 1) {
                        
                         } else {
                             throw new Error("网络故障，请稍后刷新页面!")
                         }*/
                    }
                }, {
                    msg: res.msg
                });
                return SITE_CONFIG.dictList;
            } catch (error) {
                console.error(error);
            }
        }
    } catch (error) {
        console.error(error)

    }
}

/*
 * res 结果集 code msg
 * resolve 成功处理 msg：存在->则提示； fn 成功回调
 * reject 错误处理 msg 必要，必然提示
 * */

/* element-ui cascader 的value可能是数组，只取最后一级的值 */
export function getAreaCodeValueFromArray(areaCodeArray) {
    let areacode = last(areaCodeArray);
    return areacode ? areacode : "";
}


/*完成前loading*/
export async function Loading(setLoadingFn, fn) {
    setLoadingFn(true);
    fn();
    setTimeout(() => {
        setLoadingFn(false);
    }, 1000);
}

/**
 * @Description 服务端返回的数据结构data.data.data 神烦。
 * 根据statusText、code 做出基本的状态判断，
 * 只关心成功失败的处理逻辑
 * @date 2020-08-07
 * @param {response} res
 * @param {function} resolve
 * @param {function} reject
 * @returns {any}
 */
export function SuccessOrFailed(res, resolve, reject) {
    const duration = 2000;
    // if ("OK" === res.statusText && 200 === res.status) {
    //     res = res.data;
    // }
    if (200 === res.status) {
        res = res.data;
    }

    if (res.code !== 0) {
        reject?.fn && reject?.fn(res);
        Element.Notification({
            message: reject?.msg || res?.msg,
            type: STATUS_TYPE.error,
            duration
        });
        throw new Error(res);
    } else {
        if (resolve?.msg) {
            resolve?.fn && resolve?.fn(res);
            Element.Notification({
                message: resolve?.msg || res?.msg,
                type: STATUS_TYPE.success,
                duration,
            });
        } else {
            resolve?.fn && resolve?.fn(res);
        }
        return STATUS_TYPE.done;
    }
}

/**
 * @Description 判断当前用户是否具有权限
 * @date 2020-08-05
 * @param {any} token
 * @returns {Boolean}
 */
export function hasPermission(token: string): boolean {
    const res = (SITE_CONFIG.permissions as string[]).includes(token);
    return res;
}

/**
 * @Description 获取字典数据列表
 * @date 2020-08-05
 * @param {String} dictType
 * @returns {any}
 */
export function getDictDataList(dictType) {
    const type = SITE_CONFIG?.dictList?.find((element) => element.dictType === dictType);
    if (type) {
        return type.dataList;
    } else {
        return [];
    }
}

/**
 * @Description
 * @date 2020-08-05
 * @param {any} dictType
 * @returns {any}
 */
export async function getDictDataListAsync(dictType, isCache=true) {
    /* 从单例getDictDataListAsync取出字典缓存的数据 */
    let targetDictList = getDictDataListAsync[dictType];
    if (targetDictList && isCache && dictType != 'CMD_CODE') { //干掉缓存机制 目前暂无更有效的解决办法
        return targetDictList;
    }

    const dictAll = await getSysDictTypeAll();
    const dictTypeCollection = dictAll?.find(
        (element) => element.dictType === dictType
    );
    const [labelName, valueName] = SITE_CONFIG.DICT_LABEL_VALUE_NAME;
    if (dictTypeCollection) {
        targetDictList = dictTypeCollection.dataList.map(d => ({
            ...d,
            label: d[labelName],
            value: d[valueName]
        }));
        return (getDictDataListAsync[dictType] = targetDictList);
    } else {
        console.error("未找到对应字典信息： " + dictType);
        return [];
    }
}

/**
 * 获取字典名称
 * @param dictType  字典类型
 * @param dictValue  字典值
 */
export function getDictLabel(dictType, dictValue) {
    const type = SITE_CONFIG?.dictList?.find(
        (element) => element.dictType === dictType
    );
    if (type) {
        const val = type.dataList.find(
            (element) => element.dictValue === dictValue + ""
        );
        if (val) {
            return val.dictLabel;
        } else {
            return dictValue;
        }
    } else {
        return dictValue;
    }
}

/**
 * 清除登录信息
 */
export function clearLoginInfo() {
    ls.clear();
    window.sessionStorage.clear()
    window.localStorage.clear();
    store.commit("resetStore");
    Cookies.remove("token");
    SITE_CONFIG.dynamicMenuRoutesHasAdded = false;
}

/**
 * 获取uuid
 */
export function getUUID() {
    return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, (c) => {
        return (c === "x" ? (Math.random() * 16) | 0 : "r&0x3" | "0x8").toString(
            16
        );
    });
}

/**
 * 获取svg图标(id)列表
 */
export function getIconList() {
    let res = [];
    let list = document.querySelectorAll("svg symbol");
    for (let i = 0; i < list.length; i++) {
        res.push(list[i].id);
    }

    return res;
}

/**
 * 树形数据转换
 * @param {*} data
 * @param {*} id
 * @param {*} pid
 */
export function treeDataTranslate(data, { id, pid, prop }) {
    id = id || "id";
    pid = pid || "pid";
    prop = prop || { label: "label", value: "value" };
    let res = [];
    let temp = {};
    for (let i = 0; i < data.length; i++) {
        /* 按照element-ui 接口修改对应字段 label value children */
        data[i].label = data[i][prop.label];
        data[i].value = data[i][prop.value];
        temp[data[i][id]] = data[i];
    }
    for (let k = 0; k < data.length; k++) {
        if (!temp[data[k][pid]] || data[k][id] === data[k][pid]) {
            res.push(data[k]);
            continue;
        }
        if (!temp[data[k][pid]]["children"]) {
            temp[data[k][pid]]["children"] = [];
        }
        temp[data[k][pid]]["children"].push(data[k]);
        data[k]["_level"] = (temp[data[k][pid]]._level || 0) + 1;
    }
    return res;
}

async function getAreaCodeOriginAjax() {
    getAreaCodeOriginAjax.status = STATUS_TYPE.pendding;
    try {
        let {
            data: res
        } = await http.get(API_URL.getAreaCodeOrigin);

        if ("OK" === res.statusText && 200 === res.status) {
            res = res.data;
        }
        const duration = 2000;
        if (res.code !== 0) {
            Element.Notification({
                message: res?.msg,
                type: STATUS_TYPE.error,
                duration
            });
        } else {
            getAreaCodeOriginAjax.status = STATUS_TYPE.done;
            return res.data;
        }
    } catch (error) {
        console.error(error);
        getAreaCodeOriginAjax.status = STATUS_TYPE.error;
    }
}

let whenAreaCodeOriginResolve = {
    deep: [],
    push(resolve) {
        whenAreaCodeOriginResolve.deep.push(resolve);
    },
    trigger(AreaCodeOrigin) {
        whenAreaCodeOriginResolve.deep.forEach(resolve => resolve(AreaCodeOrigin));
        whenAreaCodeOriginResolve = null;
    }
};

/**
 * @Description
 * @date 2020-08-03
 * @param {any} AREA_CODE_ORIGIN
 * @returns {any}
 */
async function handleUpdateAreaCodeFromRemoteDone(AREA_CODE_ORIGIN) {
    if (AREA_CODE_ORIGIN) {
        await setKVValue(DB_TB_kv_key.AREA_CODE_ORIGIN, AREA_CODE_ORIGIN);
    } else {
        AREA_CODE_ORIGIN = await getKVValue(DB_TB_kv_key.AREA_CODE_ORIGIN);
    }

    if (!AREA_CODE_ORIGIN) {
        console.error("获取 AREA_CODE_ORIGIN 出错！");
    }

    window.IS_DONE[DB_TB_kv_key.AREA_CODE_ORIGIN] = AREA_CODE_ORIGIN;
    /*触发队列更新*/
    whenAreaCodeOriginResolve.trigger(window.IS_DONE[DB_TB_kv_key.AREA_CODE_ORIGIN]);
    /*请求状态*/
    getAreaCodeOriginAjax.status = STATUS_TYPE.done;
    return window.IS_DONE[DB_TB_kv_key.AREA_CODE_ORIGIN];
}

async function updateAreaCodeOriginToDB(AREA_CODE_ORIGIN) {
    try {
        const connection = await getDBC();
        await connection.clear(DB_TB_NAME.areacode);
        await connection.insert({
            into: DB_TB_NAME.areacode,
            values: AREA_CODE_ORIGIN
        });
    } catch (e) {
        console.error(e);
    }
}


/*获取areaCode 扁平化数据 */
export async function checkAreaCodeDB() {
    if (!window.IS_DONE[DB_TB_kv_key.AREA_CODE_ORIGIN]) {
        await getAreaCodeOriginAsync();
    }
    return Promise.resolve();
}

export async function getAreaCodeOrigin() {
    if (!window.IS_DONE[DB_TB_kv_key.AREA_CODE_ORIGIN]) {
        await getAreaCodeOriginAsync();
    }
    return await getKVValue(DB_TB_kv_key.AREA_CODE_ORIGIN);
}

export async function getAreaCodeTreeByAreaCode(areaCode, prop) {
    const [china] = await getAreaCodeTree(prop);
    let target = [];
    if (String(areaCode).length === 2) {
        return filter(china.children, p => p[areaKey.value] === areaCode);
    }
    if (String(areaCode).length === 4) {
        target = filter(china.children, p => p[areaKey.value] === areaCode.substring(0, 2));
        return filter(target, p => p[areaKey.value] === areaCode);
    }
    if (String(areaCode).length === 6) {
        target = filter(china.children, p => p[areaKey.value] === areaCode.substring(0, 2));
        target = filter(target, p => p[areaKey.value] === areaCode.substring(0, 4));
        return filter(target, p => p[areaKey.value] === areaCode);
    }
}


async function getAreaCodeOriginAsync() {
    try {

        /* 如果请求已经发送，当前进入队列 */
        if (getAreaCodeOriginAjax?.status === STATUS_TYPE.pendding) {
            /*push 到 queue*/
            return new Promise((resolve) => whenAreaCodeOriginResolve.push(resolve));
        } else {
            getAreaCodeOriginAjax.status = STATUS_TYPE.pendding;
            /*检测是否需要更新*/
            const LOCAL_AREACODE_VERSION = await getKVValue(DB_TB_kv_key.AREACODE_VERSION);
            const {
                data: {
                    data: REMOTE_AREACODE_VERSION
                }
            } = await http.get(API_URL.getAreaCodeVersion);

            if (LOCAL_AREACODE_VERSION === REMOTE_AREACODE_VERSION) {
                window.IS_DONE[DB_TB_kv_key.AREA_CODE_ORIGIN] = true;
                return await getKVValue(DB_TB_kv_key.AREA_CODE_ORIGIN);
            } else {
                /*需要更新 areacode，AREACODE_VERSION，*/
                const AREA_CODE_ORIGIN = await getAreaCodeOriginAjax();
                await Promise.all([
                    /*原始数据放入数据库*/
                    updateAreaCodeOriginToDB(AREA_CODE_ORIGIN),
                    /*KEY-VALUE数据库*/
                    setKVValue(DB_TB_kv_key.AREA_CODE_ORIGIN, AREA_CODE_ORIGIN),
                    /*更新版本号*/
                    setKVValue(DB_TB_kv_key.AREACODE_VERSION, REMOTE_AREACODE_VERSION),
                ]);

                /*处理其他状态值*/
                window.IS_DONE[DB_TB_kv_key.AREA_CODE_ORIGIN] = true;
                /*触发队列更新*/
                whenAreaCodeOriginResolve?.trigger(AREA_CODE_ORIGIN);
                /*请求状态*/
                getAreaCodeOriginAjax.status = STATUS_TYPE.done;
                return AREA_CODE_ORIGIN;
            }
        }
    } catch (error) {
        console.error(error)

    }
}

let whenAreaCodeTreeResolve = {
    deep: [],
    push(resolve) {
        whenAreaCodeTreeResolve.deep.push(resolve);
    },
    trigger(tree) {
        whenAreaCodeTreeResolve.deep.forEach(resolve => resolve(tree));
        whenAreaCodeTreeResolve = null;
    }
};

type typeStatus = {
    status: string
}

/*获取行政区划 树形结构*/
export async function getAreaCodeTree(prop) {
    try {
        if (window.IS_DONE[DB_TB_kv_key.AREA_CODE_TREE]) {
            return await getKVValue(DB_TB_kv_key.AREA_CODE_TREE);
        } else if (getAreaCodeTree?.status === STATUS_TYPE.pendding) {
            return new Promise(resolve => whenAreaCodeTreeResolve.push(resolve));
        } else {
            getAreaCodeTree.status = STATUS_TYPE.pendding;
            const AREA_CODE_TREE = treeDataTranslate(await getAreaCodeOrigin(), { prop });
            await setKVValue(DB_TB_kv_key.AREA_CODE_TREE, AREA_CODE_TREE);
            window.IS_DONE[DB_TB_kv_key.AREA_CODE_TREE] = true;
            getAreaCodeTree.status = STATUS_TYPE.done;
            whenAreaCodeTreeResolve.trigger(AREA_CODE_TREE);
            return AREA_CODE_TREE;
        }
    } catch (error) {
        console.error(error);
    }
}

/* 通过id从行政区划列表中获取行政区划的汉字名 */
export async function getAreaCodeLabel(id, isAll) {
    /*使用indexedDB缓存的方案*/
    const result = await getAreaCodeNodeFromDB(id);
    if (isArray(result)) {
        if (isAll) {
            return result.map(i => i.name).join("/");
        } else {
            return last(result).name;
        }
    }
}

/* 表单提示动画 */
function getItemNode(node) {
    if (!node) return false;
    return node.classList.contains("el-form-item__error") ? node : getItemNode(node?.parentNode);
}

export const animateCSS = (element, animation, prefix = "animate__") =>
    // We create a Promise and return it
    new Promise((resolve) => {
        const node = isString(element) ? getItemNode(document.querySelector(element)) : element;
        if (!node) return;
        const animationName = `${prefix}${animation}`;
        node.classList.add(`${prefix}animated`, animationName);

        // When the animation ends, we clean the classes and resolve the Promise
        function handleAnimationEnd() {
            node.classList.remove(`${prefix}animated`, animationName);
            node.removeEventListener("animationend", handleAnimationEnd);
            resolve("Animation ended");
        }

        node.addEventListener("animationend", handleAnimationEnd);
    });

export function addDisabled(dslFormItems, disabledArray, flag) {
    for (let i = 0; i < disabledArray.length; i++) {
        dslFormItems[disabledArray[i]].disabled = flag;
    }

    return dslFormItems;
}


export function getViewTypeBy(type) {
    if (~["image/png", "image/gif", "image/jpeg"].indexOf(type)) {
        return "img";
    } else if (~["application/pdf"].indexOf(type)) {
        return "pdf";
    } else {
        return "other";
    }
}

export function scrollify(targetSelector, containerSelector) {
    try {
        const child = $$(targetSelector);
        const container = $$(containerSelector);
        const gap = container?.children[0]?.offsetTop || 0;
        if (!child || !container) return;
        const targetTop = child.offsetTop - gap - container.offsetTop;
        container.scrollTo({
            top: targetTop,
            behavior: "smooth"
        });
    } catch (error) {
        console.error(error);
    }
}

export function resolvePath(path) {
    /* TODO:处理路径 */
    if (isRelativeURL(path)) {
        return path;
    }
    return path;
}

export function findNodeFromTreeById<R>(element: R[], matchingId, options = { id: "id", children: "children" }): R | false {
    if (isArray(element)) {
        let length = element.length;
        while (length--) {
            const ele = element[length];
            if (ele[options.id] == matchingId) return ele;
            if (isArray(ele[options.children]) && ele[options.children].length > 0) {
                const resNode = findNodeFromTreeById<R>(ele[options.children], matchingId);
                if (resNode) {
                    return resNode;
                }
            }
        }
    }
    return false;
}



//@ts-check
export function checkFile(file, targetType): Boolean {
    const isAllowed = includes(targetType, file.type);
    if (!isAllowed) {
        Element.Notification({
            message: `请按文件要求上传发文件`,
            type: STATUS_TYPE.error,
            duration: 2000
        });
    }
    return isAllowed;
}

/**
 * @param {Array} data 数据
 * @param {string} treeLevel 判断为父为子
 * @param {string} parentKey 父级
 * @param {string} parentIDKey 子级
 * @param {string} limit 限制
 * @returns {Array}
 * @description 处理数组结构
 */
export function translateDataToTree(data,treeLevel, parentKey, parentIDKey, limit) {
    let parent = data.filter((value) => Number(value[treeLevel]) <= limit); // 父数据
    let children = data.filter((value) => Number(value[treeLevel]) > limit); // 子数据
    let translator = (parent, children) => {
        parent.forEach((parent) => {
            children.forEach((current, index) => {
                if (current[parentKey] === parent[parentIDKey]) {
                    const temp = JSON.parse(JSON.stringify(children));
                    temp.splice(index, 1);
                    translator([current], temp);
                    typeof parent.children !== "undefined"
                        ? parent.children.push(current)
                        : (parent.children = [current]);
                }
            });
        });
    };
    translator(parent, children);
    return parent;
}
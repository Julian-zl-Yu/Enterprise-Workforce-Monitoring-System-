// @ts-nocheck
import {
    DB_TB_NAME
} from "@/components/Types";
import { getDBC } from "@/utils/dataBase.ts";


export const KeyValueCache = {
    name: DB_TB_NAME.kvCache,
    columns: {
        key: {
            primaryKey: true,
            dataType: "string"
        },
        value: {
            dataType: "object"
        }
    }
};
export async function clearKVCacheValue() {
    const connection = await getDBC();
    return await connection.clear(DB_TB_NAME.kvCache);
}

export async function getKVCacheValue(key): Promise<string | false> {
    const connection = await getDBC();
    const res = await connection.select({
        from: DB_TB_NAME.kvCache,
        where: { key }
    });
    return res[0]?.value?.value || false;
}

export async function setKVCacheValue(key, value) {
    try {
        const connection = await getDBC();
        await connection.insert({
            into: DB_TB_NAME.kvCache,
            /* if exist update otherwise insert => upsert  */
            upsert: true,
            values: [{
                key,
                value: {
                    value
                }
            }],
        });
    } catch (error) {
        console.error(error);
    }
}
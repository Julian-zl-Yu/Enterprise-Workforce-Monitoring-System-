// @ts-nocheck
import {
    DB_TB_NAME
} from "@/components/Types";
import { getDBC } from "@/utils/dataBase.ts";

export const KeyValue = {
    name: DB_TB_NAME.kv,
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

export async function getKVValue(key) {
    const connection = await getDBC();
    const res = await connection.select({
        from: DB_TB_NAME.kv,
        where: {
            key,
        }
    });
    return res[0]?.value?.value || false;
}

type typeKVKey = "AREA_CODE_ORIGIN" | "AREACODE_VERSION" | "AREA_CODE_TREE";

export async function setKVValue(key: typeKVKey, value: any) {
    try {
        const connection = await getDBC();
        await connection.insert({
            into: DB_TB_NAME.kv,
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
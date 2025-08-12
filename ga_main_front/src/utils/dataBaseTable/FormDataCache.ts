import {
    DB_TB_NAME
} from "@/components/Types";


export const FormDataCache = {
    name: DB_TB_NAME.formDataCache,
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
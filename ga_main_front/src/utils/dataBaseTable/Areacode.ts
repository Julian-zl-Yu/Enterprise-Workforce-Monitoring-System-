import {
    DB_TB_NAME
} from "@/components/Types";

/*后端数据库字段变化，此为中间层作映射用*/
export const areaKey = {
    name: "name",
    sort: "sort",
    value: "value",
    rank: "rank",
    label: "name"
};

export const Areacode = {
    name: DB_TB_NAME.areacode,
    columns: {
        hasChildren: {
            dataType: "boolean"
        },
        id: {
            primaryKey: true,
            dataType: "string"
        },
        lat: {
            dataType: "number"
        },
        lng: {
            dataType: "number"
        },
        name: {
            dataType: "string"
        },
        parentName: {
            dataType: "string"
        },
        pid: {
            dataType: "string"
        },
        sort: {
            dataType: "string"
        },
        treeLevel: {
            dataType: "number"
        },
        updateDate: {
            dataType: "string"
        },
        value: {
            dataType: "string"
        },
        whether: {
            dataType: "string"
        },
    }
};
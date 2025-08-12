import {
    required,number
} from "@/utils/formRule";
import { FORM_ITEM_TYPE } from "@/components/Types";

export const MODULE_BASE_URL = "/enterprise/cp03config";

/* 列表 表头 */
export let DSLTableHeader = [
    {
        prop: "startday",
        label: "开始日"
    },{
        prop: "endday",
        label: "结束日"
    }
];

/* 查询框 */
export let DSLFormItemsQuery = {};


/* 详情表单 */
export let DSLFormItems = {
    startday: {
        label: "开始日",
        prop: "startday",
        type: FORM_ITEM_TYPE.InputNumber,
        minValue: 1,
        maxValue: 31,
    },
    endday: {
        label: "结束日",
        prop: "endday",
        type: FORM_ITEM_TYPE.InputNumber,
        minValue: 1,
        maxValue: 31,
       
    }

};


/* 详情表单 验证规则 */
export let DSLFormItemRule = {
    startday:[required,number],
    endday:[required,number],
};
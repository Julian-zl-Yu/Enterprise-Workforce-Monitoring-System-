import {
    required,number,fnExclude
} from "@/utils/formRule";
import { FORM_ITEM_TYPE } from "@/components/Types";

export const MODULE_BASE_URL = "/enterprise/cp03tj/generalSituation";
export const MODULE_BASE_URL_EXPORT = "/enterprise/cp03tj/exportGeneralSituation";


/* 列表 表头 */
export let DSLTableHeader = [
    {
        prop: "corpType",
        label: "企业类型"
    },
    {
        label: "监测企业数",
        prop: "changeWorkerCpCount"
    },
    {
        prop: "changeWorkerCount",
        label: "较上月变化人数",
    },
    {
        prop: "changeWorkerRange",
        label: "较上月变化幅度"
    },
    {
        prop: "changeWorkerCountCompareBeginYear",
        label: "较年初变化人数",
    }, {
        prop: "changeWorkerRangeCompareBeginYear",
        label: "较年初变化幅度"
    }
];

/* 查询框 */
export let DSLFormItemsQuery = {
    yearMonth: {
        span: 24,
        prop: "yearMonth",
        label: "查询年月",
        type: FORM_ITEM_TYPE.DatePicker,
        valueFormat: "yyyy-MM",
        dateType: "month",
        format: "yyyy-MM"
    }
};

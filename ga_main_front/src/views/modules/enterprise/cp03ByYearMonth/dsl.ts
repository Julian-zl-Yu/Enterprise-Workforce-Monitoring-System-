import {
    required,number,fnExclude
} from "@/utils/formRule";
import { FORM_ITEM_TYPE } from "@/components/Types";

export const MODULE_BASE_URL = "/enterprise/cp03tj/byYearMonth";
export const MODULE_BASE_URL_EXPORT = "/enterprise/cp03tj/exportByYearMonth";





/* 列表 表头 */
export let DSLTableHeader = [
    {
        prop: "yearMonth",
        label: "调查期"
    },
    {
        prop: "preWorkerCount",
        label: "上期工人数",
    },
    {
        prop: "nowWorkerCount",
        label: "本期工人数",
    },
    {
        prop: "changeWorkerCount",
        label: "比上期变化工人数"
    },
    {
        prop: "changeWorkerRange",
        label: "比上期变化工人幅度",
    },
    {
        prop: "changeWorkerCountCompareBeginYear",
        label: "比年初变化工人数"
    },
    {
        prop: "changeWorkerRangeCompareBeginYear",
        label: "比年初变化工人幅度"
    }
];

/* 查询框 */
export let DSLFormItemsQuery = {
    year: {
        span: 24,
        prop: "year",
        label: "查询年月",
        type: FORM_ITEM_TYPE.DatePicker,
        valueFormat: "yyyy",
        dateType: "year",
        format: "yyyy"
    }
};

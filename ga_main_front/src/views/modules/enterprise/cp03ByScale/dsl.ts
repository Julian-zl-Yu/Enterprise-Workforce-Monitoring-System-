import {
    required,number,fnExclude
} from "@/utils/formRule";
import { FORM_ITEM_TYPE } from "@/components/Types";

export const MODULE_BASE_URL = "/enterprise/cp03tj/byScale";
export const MODULE_BASE_URL_EXPORT = "/enterprise/cp03tj/exportByScale";



/* 列表 表头 */
export let DSLTableHeader = [
    {
        prop: "scale",
        label: "企业规模"
    },
    {
        label: "监测企业数",
        prop: "cpCount"
    },
    {
        prop: "nowWorkerCount",
        label: "当月工人数",
    },
    {
        prop: "preWorkerCount",
        label: "上月工人数"
    },
    {
        prop: "changeWorkerCount",
        label: "比上月变化工人数",
    },
    {
        prop: "changeWorkerRange",
        label: "比上月变化工人幅度"
    },
   
    {
        prop: "changeWorkerCountCompareBeginYear",
        label: "比年初变化工人数",
    }, {
        prop: "changeWorkerRangeCompareBeginYear",
        label: "比年初变化工人幅度"
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

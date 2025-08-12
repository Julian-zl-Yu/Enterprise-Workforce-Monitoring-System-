import {
    required,number,fnExclude
} from "@/utils/formRule";
import { FORM_ITEM_TYPE } from "@/components/Types";

export const MODULE_BASE_URL = "/enterprise/cp03tj/byLack";
export const MODULE_BASE_URL_EXPORT = "/enterprise/cp03tj/exportByLack";



/* 列表 表头 */
export let DSLTableHeader = [
    {
        prop: "category1",
        label: "项目"
    },
    {
        prop: "category2",
        label: ""
    },
    {
        label: "监测企业数",
        prop: "cpCount"
    },
    {
        prop: "nowLackCpCount",
        label: "本期缺工企业数",
    },
    {
        prop: "nowLackWorkerCount",
        label: "本期缺工人数",
    },
    {
        prop: "nowLackCpCountRange",
        label: "本期缺工企业占比"
    },
    {
        prop: "preLackCpCount",
        label: "上期缺工企业数",
    },
    {
        prop: "preLackWorkerCount",
        label: "上期缺工人数",
    }, {
        prop: "preLackCpCountRange",
        label: "上期缺工企业占比"
    },{
        prop: "beginLackCpCount",
        label: "年初缺工企业数",
    },
    {
        prop: "beginLackWorkerCount",
        label: "年初缺工人数",
    }, {
        prop: "beginLackCpCountRange",
        label: "年初缺工企业占比"
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

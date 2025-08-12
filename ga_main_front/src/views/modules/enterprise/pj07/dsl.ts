import {
    required,positiveNumber,phone
} from "@/utils/formRule";
import { FORM_ITEM_TYPE } from "@/components/Types";

export const MODULE_BASE_URL = "/enterprise/pj07";
export const MODULE_BASE_URL_TYC = "/enterprise/pj07/getpj07InfoByAli";

/* 列表 表头 */
export let DSLTableHeader = [
    {
        prop: "corpname",
        label: "企业名称"
    },
    {
        prop: "pj0702",
        label: "工点名称"
    },
    {
        label: "工点联系人",
        prop: "pj0703"
    },
    {
        prop: "pj0704",
        label: "联系人电话"
    }

];

/* 查询框 */
export let DSLFormItemsQuery = {
    corpname: {
        span: 24,
        prop: "corpname",
        label: "企业名称",
    },
    pj0702: {
        span: 24,
        prop: "pj0702",
        label: "工点名称",
    }
    
};


/* 详情表单 */
export let DSLFormItems = {
    corpname: {
        span: 24,
        prop: "corpname",
        label: "企业名称",
    },
    pj0702: {
        span: 24,
        prop: "pj0702",
        label: "工点名称",
    },
    pj0703: {
        span: 24,
        prop: "pj0703",
        label: "负责人姓名",
    },
    pj0704: {
        span: 24,
        prop: "pj0704",
        label: "负责人电话",
    }
};


/* 详情表单 验证规则 */
export let DSLFormItemRule = {

}
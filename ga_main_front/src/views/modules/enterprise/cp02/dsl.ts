import {
    required,
    phone,
    email,
    corpcode as corpcodeRule
} from "@/utils/formRule";
import {
    CURRENCY_TYPE,
    FORM_ITEM_TYPE
} from "@/components/Types";
import { idCardNumber } from "../../../../utils/formRule";

export const MODULE_BASE_URL = "/enterprise/cp02";

import { DSLFormItems as cp01DSLFormItems } from "../cp01/dsl";

/* 列表 表头 */
export let DSLTableHeader = [
    {
        prop: "corpname",
        label: "企业名称"
    },
    {
        prop: "corpcode",
        label: "社会统一信用代码"
    },
    {
        prop: "organizationtype",
        label: "单位性质",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "ORGANIZATIONTYPE"
    }, {
        prop: "regcapital",
        label: "注册资本(万元)"
    },
    {
        prop: "linkman",
        label: "联系人姓名"
    },
    {
        prop: "linkcellphone",
        label: "联系人手机号码"
    },
    {
        prop: "cp02Status",
        label: "审核状态",
        type: FORM_ITEM_TYPE.PassOrNot
    },
    {
        prop: "createDate",
        label: "创建时间"
    },
];

/* 查询框 */
export let DSLFormItemsQuery = {
    corpname: {
        span: 8,
        prop: "corpname",
        label: "企业名称",
        placeholder: "请输入企业名称"
    },
    cp02Status: {
        prop: "cp02Status",
        label: "审核状态",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "CP02STATUS"
    }
};

/* 详情表单 */

/* 审核数据 */
export let DSLFormItemsStatus2 = {
    cp02Status:{
        label: "审核状态",
        prop: "cp02Status",
        type: FORM_ITEM_TYPE.Radio,
        radioIs: "ElRadioButton",
        radioArray: [{
            value: "1",
            label: "通过"
        }, {
            value: "2",
            label: "不通过"
        }]
    },
    reason: {
        prop: "reason",
        label: "不通过原因"
    }
};

export let DSLFormItemsStatus = {
    cp02Status:{
        label: "审核状态",
        prop: "cp02Status",
        type: FORM_ITEM_TYPE.Radio,
        radioIs: "ElRadioButton",
        radioArray: [{
            value: "1",
            label: "通过"
        }, {
            value: "2",
            label: "不通过"
        }]
    }
};

//企业基础信
export let DSLFormItems = cp01DSLFormItems;
export let DSLFormLayoutJCXX =
    [];

/* 详情表单 验证规则 */
export let DSLFormItemRule = {
    legalmanidcardnumber:[idCardNumber],
    corptype: [required],
    address: [required],
    areacode: [required],
    businessstatus: [required],
    capitalcurrencytype: [required],
    /* 社会统一信用代码 */
    corpcode: [required, corpcodeRule],
    corpname: [required],
    establishdate: [required],
    legalman: [required],
    linkcellphone: [required, phone],
    linkman: [required],
    regcapital: [required],
    organizationtype: [required],
    pmName: [required],
    pmIdCardType: [required],
    pmIdCardNumber: [required,idCardNumber],
};


export let DSLFormItemRuleStatus2 = {
    cp02Status: [required],
    reason: [required],
};

export let DSLFormItemRuleStatus = {
    cp02Status: [required]
};
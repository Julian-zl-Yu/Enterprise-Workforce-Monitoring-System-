import {
    required,positiveNumber,phone
} from "@/utils/formRule";
import { FORM_ITEM_TYPE } from "@/components/Types";

export const MODULE_BASE_URL = "/enterprise/cp01";
export const MODULE_BASE_URL_TYC = "/enterprise/cp01/getCp01InfoByAli";

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
        label: "注册地区",
        prop: "areacode",
        type: FORM_ITEM_TYPE.Cascader,
        bindId: "areacode.china"
    },
    {
        prop: "organizationtype",
        label: "单位性质",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "ORGANIZATIONTYPE"
    },
      {
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
        prop: "ablestatus",
        label: "状态",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "ABLESTATUS"
    },
    
];

/* 查询框 */
export let DSLFormItemsQuery = {
    corpname: {
        span: 24,
        prop: "corpname",
        label: "企业名称",
        placeholder: "企业名称"
    },
    organizationtype: {
        span: 24,
        prop: "organizationtype",
        label: "单位性质",
        placeholder: "单位性质",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "ORGANIZATIONTYPE"
    },
    areacode: {
        span: 24,
        prop: "areacode",
        label: "注册地区",
        placeholder: "注册地区",
        type: FORM_ITEM_TYPE.Cascader,
        bindId: "areacode.china",
        checkStrictly: true
    },
    ablestatus:{
        prop: "ablestatus",
        label: "状态",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "ABLESTATUS"
    },
    
};


/* 详情表单 */
export let DSLFormItems = {
    corpcode: {
        label: "社会统一信用代码",
        prop: "corpcode",
        type: FORM_ITEM_TYPE.InputBtn
    }, 
    corpname: {
        label: "企业名称",
        prop: "corpname"
    }, 
   
    organizationtype: {
        label: "单位性质",
        prop: "organizationtype",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "ORGANIZATIONTYPE"
    },
    industry: {
        label: "行业",
        prop: "industry",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "INDUSTRY"
    },
    estate: {
        label: "产业",
        prop: "estate",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "ESTATE"
    },
    scale: {
        label: "规模",
        prop: "scale",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "SCALE"
    },
    areacode: {
        label: "注册地区",
        prop: "areacode",
        type: FORM_ITEM_TYPE.Cascader,
        bindId: "areacode.china",
        disabled: true
    }, 
    address: {
        label: "企业营业地址",
        prop: "address"
    },
     businessstatus: {
        label: "企业经营状态",
        prop: "businessstatus",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "BUSINESSSTATUS"
    }, 
    capitalcurrencytype: {
        label: "注册资本币种",
        prop: "capitalcurrencytype",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "CAPITALCURRENCYTYPE"
    }, 
    linkcellphone: {
        label: "联系人手机号码",
        prop: "linkcellphone"
    }, 
    linkman: {
        label: "联系人姓名",
        prop: "linkman"
    }, 
    email: {
        label: "企业联系邮箱",
        prop: "email"
    }, 
    entscope: {
        label: "经营范围",
        prop: "entscope"
    }, 
    establishdate: {
        label: "成立日期",
        prop: "establishdate",
        type: FORM_ITEM_TYPE.DatePicker
    }, 
    faxnumber: {
        label: "传真号码",
        prop: "faxnumber"
    }, 
    idcardtype: {
        label: "法定代表人证件类型",
        prop: "idcardtype",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "IDCARDTYPE"
    }, 
    legalman: {
        label: "法定代表人姓名",
        prop: "legalman"
    }, 
    legalmanidcardnumber: {
        label: "法定代表人证件号码",
        prop: "legalmanidcardnumber"
    }, 
    
    linkphone: {
        label: "联系人办公电话",
        prop: "linkphone"
    }, 
    memo: {
        label: "企业备注",
        prop: "memo"
    }, 
    officephone: {
        label: "办公电话",
        prop: "officephone"
    },
    regcapital: {
        label: "注册资本(万元)",
        prop: "regcapital",
    }, 
    regdept: {
        label: "登记机关",
        prop: "regdept"
    }, 
    website: {
        label: "企业网址",
        prop: "website"
    }, 
    zipcode: {
        label: "邮政编码",
        prop: "zipcode"
    },
    ablestatus:{
        label: "状态",
        prop: "ablestatus",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "ABLESTATUS",
        disabled: false
    }
};


/* 详情表单 验证规则 */
export let DSLFormItemRule = {
    address:[required],
    areacode:[required],
    organizationtype: [required],
    industry:[required],
    estate:[required],
    scale:[required],
    businessstatus:[required],
    corpcode:[required],
    corpname:[required],
    cp010:[required],

    linkcellphone:[required,phone],
    linkman:[required],

   
}
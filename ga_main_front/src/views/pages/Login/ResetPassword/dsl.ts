import {
    required,
    test,
    phone
} from "@/utils/formRule";


import {FORM_ITEM_TYPE} from "@/components/Types";

export let DSLFormItems = {
    /*name: {span: 24, prop: "name", label: "项目名称"},
    areacode: {span: 24, prop: "areacode", label: "项目所在地"},
    constructionname: {span: 24, prop: "constructionname", label: "建设单位名称"},
    constructionnumber: {span: 24, prop: "constructionnumber", label: "建设单位统一社会信用代码"},
    contractname: {span: 24, prop: "contractname", label: "施工总承包单位名称"},
    contractnumber: {span: 24, prop: "contractnumber", label: "施工总承包单位统一社会信用代码"},
    industry: {span: 24, prop: "industry", label: "所属行业"},
    linkman: {span: 24, prop: "linkman", label: "联系人姓名"},
    linkphone: {span: 24, prop: "linkphone", label: "联系人电话"},
*/
  
    "mobile": {
        span: 24,
        prop: "mobile",
        label: "联系人电话",
        placeholder: "请输入注册手机号"
    },
    "verificationCode":{
        span: 24,
        prop: "verificationCode",
        type: FORM_ITEM_TYPE.SMScode,
        smsUrl:'/sendCode',
        smsPhone: '',
        label: "短信验证码",
        placeholder: "请输入短信验证码"
    }

};

/* 验证规则 */
export let DSLFormItemRule = {
    "verificationCode": [required],
    "mobile": [required, phone],
};
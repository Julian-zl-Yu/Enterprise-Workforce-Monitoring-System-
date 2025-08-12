import {
    required,
    test,
    phone
} from "@/utils/formRule";
import {
    FORM_ITEM_TYPE
} from "@/components/Types";

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
  
    /* "industry": {
        span: 24,
        prop: "industry",
        label: "所属行业",
        type: FORM_ITEM_TYPE.Radio,
        radioArray: [{
            value: "1",
            label: "住建"
        },
            {
                value: "2",
                label: "水务"
            },
            {
                value: "3",
                label: "交通"
            },
            {
                value: "4",
                label: "教育"
            },
            {
                value: "5",
                label: "卫健"
            },
            {
                value: "6",
                label: "发改"
            },
            {
                value: "7",
                label: "国土"
            },
            {
                value: "8",
                label: "其它"
            }
        ]
    }, */
   

    "areacode":{
        span: 24,
        prop: "areacode",
        label: "企业所在地",
        type: FORM_ITEM_TYPE.Cascader,
        bindId: "areacode.china",
    },
    "corpcode":{
        span: 24,
        prop: "corpcode",
        label: "社会统一信用代码"
    },
    "corpname": {
        span: 24,
        prop: "corpname",
        label: "企业名称"
    },
    
    "linkman": {
        span: 24,
        prop: "linkman",
        label: "联系人姓名"
    },
    "mobile": {
        span: 24,
        prop: "mobile",
        label: "联系人电话",
        placeholder: "手机号码用于接收账号,请认真填写"
    },
    "industry": {
        span: 24,
        prop: "industry",
        label: "行业",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "INDUSTRY"
    },
    "estate": {
        span: 24,
        prop: "estate",
        label: "产业",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "ESTATE"
    },
    "scale": {
        label: "规模",
        prop: "scale",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "SCALE"
    },

};

/* 验证规则 */
export let DSLFormItemRule = {
    "areacode": [test, required],
    "corpcode": [required],
    "corpname": [required],
    "linkman": [required],
    "mobile": [required, phone],
    "industry": [required],
    "estate": [required],
    "scale": [required],
};
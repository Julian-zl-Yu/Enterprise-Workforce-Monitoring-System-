import {
    required,
} from "@/utils/formRule";
import {
    FORM_ITEM_TYPE
} from "@/components/Types";

export let MODULE_BASE_URL = "/enterprise/pj06/";

/* 列表 表头 */
export let DSLTableHeader = [{
    prop: "name",
    label: "项目名称",
    showOverflowTooltip: true
},
{
    prop: "industry",
    label: "所属行业",
    type: FORM_ITEM_TYPE.Dict,
    bindId: "INDUSTRY"
},
{
    prop: "areacode",
    label: "项目所在地",
    type: FORM_ITEM_TYPE.PositionPicker
},
{
    prop: "linkman",
    label: "联系人姓名",
},
{
    prop: "linkphone",
    label: "联系人电话",
},
{
    prop: "status",
    label: "审核状态",
    type: FORM_ITEM_TYPE.PassOrNot
}
];

/* 查询框 */
export let DSLFormItemsQuery = {
    name: {
        span: 24,
        prop: "name",
        label: "项目名称"
    }
};

/* 详情表单 */
export let DSLFormItems = {
    status: {
        prop: "status",
        label: "PassOrNot",
        type: FORM_ITEM_TYPE.PassOrNot,
    },
    pj0101: {
        prop: "pj0101",
        label: "Select selectArray",
        type: FORM_ITEM_TYPE.Select,
        selectArray: [
            {
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
    },
    tagno: {
        label: "Cascader-areacode.china",
        prop: "tagno",
        type: FORM_ITEM_TYPE.Cascader,
        bindId: "areacode.china"
    },
    model: {
        label: "dict INDUSTRY",
        prop: "model",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "INDUSTRY"
    },
    devicename: {
        label: "异步验证",
        prop: "devicename",
    },
    trademark: {
        label: "Radio",
        prop: "trademark",
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
    devicetype: {
        label: FORM_ITEM_TYPE[FORM_ITEM_TYPE.PositionPicker],
        prop: "devicetype",
        type: FORM_ITEM_TYPE.PositionPicker,
        range: {
            prop: "tagno"
        }
        // disabled: true
    },
    dischargestage: {
        label: "InputNumber",
        prop: "dischargestage",
        type: FORM_ITEM_TYPE.InputNumber,
    },
    manufacturer: {
        label: "Money",
        prop: "manufacturer",
        type: FORM_ITEM_TYPE.Money,

    },
    enginetype: {
        label: "input password",
        prop: "enginetype",
        showPassword: true

    },
    factorymonthly: {
        label: "出厂年月",
        prop: "factorymonthly",
        type: FORM_ITEM_TYPE.DatePicker
    },
    propertyunit: {
        label: "after 限制时间",
        prop: "propertyunit",
        type: FORM_ITEM_TYPE.DatePicker,
        limit: ["after", "factorymonthly"]
    },
    creditcode: {
        label: "Avatar",
        prop: "creditcode",
        type: FORM_ITEM_TYPE.Avatar
    },
    memo: {
        label: "Textarea",
        prop: "memo",
        type: FORM_ITEM_TYPE.Textarea,

    },
    creator: {
        label: "密码显示",
        prop: "creator",
        showPassword: true
    },
    createDate: {
        label: "创建时间",
        prop: "createDate"
    },
    updater: {
        label: "更新者",
        prop: "updater"
    },
    updateDate: {
        label: "更新时间",
        prop: "updateDate"
    },
};

/* 详情表单 布局 */
export let DSLFormLayout = [[{
    "p": "creditcode",
    "c": 2,
    "r": 5
}, { "p": "pj0101" }], [{ "p": "devicename" }], [{ "p": "devicetype" }], [{ "p": "propertyunit" }], [{ "p": "creator" }], [{ "p": "createDate" }, { "p": "updater" }, { "p": "dischargestage" }], [{ "p": "updateDate" }, { "p": "trademark" }, { "p": "manufacturer" }], [{ "p": "model" }, { "p": "tagno" }, { "p": "status" }], [{ "p": "enginetype" }, { "p": "factorymonthly" }, { "p": "memo" }]];


/* 详情表单 验证规则 */
export let DSLFormItemRule = {

    cp0101: [required],
    devicename: [required]
};
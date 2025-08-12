import {
    required,number,fnExclude
} from "@/utils/formRule";
import { FORM_ITEM_TYPE } from "@/components/Types";

export const MODULE_BASE_URL = "/enterprise/cp03";

/* 列表 表头 */
export let DSLTableHeader = [
    {
        prop: "yearMonth",
        label: "填报年月"
    },
    {
        label: "企业名称",
        prop: "corpname"
    },
    {
        prop: "areacode",
        label: "注册地区",
        type: FORM_ITEM_TYPE.Cascader,
        bindId: "areacode.china"
    },
    {
        prop: "examineStatus",
        label: "审核状态",
        type: FORM_ITEM_TYPE.PassOrNot
    },
    {
        prop: "lackController",
        label: "管理人员缺工数量"
    },
    {
        prop: "lackOrdWorker",
        label: "普通人员缺工数量",
    }, {
        prop: "lackSkillWorker",
        label: "技能人员缺工数量"
    },
    {
        prop: "lackTechWorker",
        label: "技术人员缺工数量"
    },
    {
        prop: "useController",
        label: "管理人员用工数量"
    },

    {
        prop: "useOrdWorker",
        label: "普通工人用工数量"
    },
    {
        prop: "useSkillWorker",
        label: "技能工人用工数量"
    },
    {
        prop: "useTechWorker",
        label: "技术工人用工数量"
    }
];

/* 查询框 */
export let DSLFormItemsQuery = {
    yearMonth: {
        span: 24,
        prop: "yearMonth",
        label: "填报年月",
        type: FORM_ITEM_TYPE.DatePicker,
        valueFormat: "yyyy-MM",
        dateType: "month",
        format: "yyyy-MM"
    },
    corpname:{
        label: "企业名称",
        prop: 'corpname'
    },
    examineStatus: {
        prop: "examineStatus",
        label: "审核状态",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "CP02STATUS"
    },
    areacode:{
        prop: "areacode",
        label: "注册地区",
        type: FORM_ITEM_TYPE.Cascader,
        bindId: "areacode.china",
        checkStrictly: true
    },
};


/* 详情表单 */
export let DSLFormItems = {
    yearMonth: {
        label: "填报年月",
        prop: "yearMonth",
        type: FORM_ITEM_TYPE.DatePicker,
        valueFormat: "yyyy-MM",
        dateType: "month",
        format: "yyyy-MM"
    },
    corpname: {
        label: "企业名称",
        prop: "corpname"
    },
    lackController: {
        label: "管理人员缺工数量",
        prop: "lackController"
    },
    lackOrdWorker: {
        label: "普通人员缺工数量",
        prop: "lackOrdWorker",
    },
    lackSkillWorker: {
        label: "技能人员缺工数量",
        prop: "lackSkillWorker"
    }, 
    lackTechWorker: {
        label: "技术人员缺工数量",
        prop: "lackTechWorker"
    }, 
    useController: {
        label: "管理人员用工数量",
        prop: "useController"
    }, 
    useOrdWorker: {
        label: "普通工人用工数量",
        prop: "useOrdWorker"
    }, 
    useSkillWorker: {
        label: "技能工人用工数量",
        prop: "useSkillWorker"
    }, 
    useTechWorker: {
        label: "技术工人用工数量",
        prop: "useTechWorker"
    }

};

export let DSLFormItemsStatus = {
    examineStatus: {
        label: "审核状态",
        prop: "examineStatus",
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

export let DSLFormItemRuleStatus = {
    examineStatus: [required,
                fnExclude({
                    value: ["0"],
                    msg: "审核状态为必填项，请选择【通过】或者【不通过】",
                  }),]
}





/* 详情表单 验证规则 */
export let DSLFormItemRule = {
    lackController:[required,number],
    lackOrdWorker:[required,number],
    lackSkillWorker:[required,number],
    lackTechWorker:[required,number],
    useController:[required,number],
    useOrdWorker:[required,number],
    useSkillWorker:[required,number],
    useTechWorker:[required,number],
    yearMonth:[required],
};
import {
    required,
    idCardNumber
} from "@/utils/formRule";

import {
    FORM_ITEM_TYPE
} from "@/components/Types";

import i18n from "@/i18n";

export const MODULE_BASE_URL = "/sys/dept/list";

/* 列表 表头 */
export let DSLTableHeader = [/* {
        label: i18n.t("dept.name"),
        prop: "name"
    }, */
    {
        label: i18n.t("dept.parentName"),
        prop: "parentName"
    },
    {
        label: i18n.t("dept.sort"),
        prop: "sort"
    }
];

/* 查询框 */
export let DSLFormItemsQuery = {
    personName: {
        span: 8,
        prop: "personName",
        label: "姓名"
    },
    tm0101: {
        span: 8,
        prop: "tm0101",
        label: "班组",
        type: FORM_ITEM_TYPE.Select,
        bindUrl: "/common/getTeamInfo"
    }
};

/*基础信息*/
export let DSLFormItemsJCXX = {
    issuecardpicurl: {
        label: "工人头像",
        prop: "issuecardpicurl",
        type: FORM_ITEM_TYPE.Avatar
    },
    ps01DTO_name: {
        label: "姓名",
        prop: "ps01DTO_name"
    },
    ps01DTO_idcardtype: {
        label: "证件类型",
        prop: "ps01DTO_idcardtype",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "IDCARDTYPE"
    },
    ps01DTO_idcardnumber: {
        label: "证件号码",
        prop: "ps01DTO_idcardnumber",
        type: FORM_ITEM_TYPE.InputCardReader,
    },
    ps01DTO_gender: {
        label: "性别",
        prop: "ps01DTO_gender",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "GENDER"
    },
    ps01DTO_nation: {
        label: "民族",
        prop: "ps01DTO_nation",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "NATION"
    },
    ps01DTO_birthday: {
        label: "出生日期",
        prop: "ps01DTO_birthday",
        type: FORM_ITEM_TYPE.DatePicker,
    },
    ps01DTO_address: {
        label: "住址",
        prop: "ps01DTO_address"
    },
    ps01DTO_edulevel: {
        label: "学历",
        prop: "ps01DTO_edulevel",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "EDULEVEL"
    },
    ps01DTO_degree: {
        label: "学位",
        prop: "ps01DTO_degree",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "DEGREE"
    },
    ps01DTO_areacode: {
        label: "籍贯",
        prop: "ps01DTO_areacode",
        type: FORM_ITEM_TYPE.Cascader,
        bindId: "areacode.china"
    },
    ps01DTO_headimageurl: {
        label: "身份证头像",
        prop: "ps01DTO_headimageurl"
    },
    ps01DTO_politicstype: {
        label: "政治面貌",
        prop: "ps01DTO_politicstype",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "POLITICSTYPE"
    },
    ps01DTO_isjoined: {
        label: "是否加入工会",
        prop: "ps01DTO_isjoined",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "WHETHER"
    },
    ps01DTO_joinedtime: {
        label: "加入工会时间",
        prop: "ps01DTO_joinedtime",
        type: FORM_ITEM_TYPE.DatePicker,
    },
    ps01DTO_cellphone: {
        label: "手机号码",
        prop: "ps01DTO_cellphone"
    },
    ps01DTO_cultureleveltype: {
        label: "文化程度",
        prop: "ps01DTO_cultureleveltype",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "CULTURELEVELTYPE"
    },
    ps01DTO_specialty: {
        label: "特长",
        prop: "ps01DTO_specialty"
    },
    ps01DTO_hasbadmedicalhistory: {
        label: "是否有重大病史",
        prop: "ps01DTO_hasbadmedicalhistory",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "WHETHER"
    },
    ps01DTO_urgentlinkman: {
        label: "紧急联系人姓名",
        prop: "ps01DTO_urgentlinkman"
    },
    ps01DTO_urgentlinkmanphone: {
        label: "紧急联系电话",
        prop: "ps01DTO_urgentlinkmanphone"
    },
    ps01DTO_workdate: {
        label: "开始工作日期",
        prop: "ps01DTO_workdate",
        type: FORM_ITEM_TYPE.DatePicker
    },
    ps01DTO_maritalstatus: {
        label: "婚姻状况",
        prop: "ps01DTO_maritalstatus",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "MARITALSTATUS"
    },
    ps01DTO_grantorg: {
        label: "发证机关",
        prop: "ps01DTO_grantorg"
    },
    ps01DTO_positiveidcardimageurl: {
        label: "正面照 URL",
        prop: "ps01DTO_positiveidcardimageurl"
    },
    ps01DTO_negativeidcardimageurl: {
        label: "反面照 URL",
        prop: "ps01DTO_negativeidcardimageurl"
    },
    ps01DTO_startdate: {
        label: "有效期开始日期",
        prop: "ps01DTO_startdate",
        type: FORM_ITEM_TYPE.DatePicker
    },
    ps01DTO_expirydate: {
        label: "有效期结束日期",
        prop: "ps01DTO_expirydate",
        type: FORM_ITEM_TYPE.DatePicker
    }
};
/*参建信息*/
export let DSLFormItemsCJXX = {
    entrytime: {
        prop: "entrytime",
        label: "进场时间",
        type: FORM_ITEM_TYPE.DatePicker,
    },
    tm0101: {
        prop: "tm0101",
        label: "所属班组",
        type: FORM_ITEM_TYPE.Select,
        bindUrl: "/common/getTeamInfo"
    },
    /*   exittime: {
          prop: "exittime",
          label: "退场时间",
          type: FORM_ITEM_TYPE.DatePicker,
      }, */
    worktypecode: {
        prop: "worktypecode",
        label: "工种",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "WORKTYPECODE"
    },
    isteamleader: {
        prop: "isteamleader",
        label: "是否班组长",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "WHETHER"
    },
    hasbuyinsurance: {
        prop: "hasbuyinsurance",
        label: "是否购买工伤或意外伤害保险",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "WHETHER"
    },
    memo: {
        prop: "memo",
        label: "备注",
        span: 24,
        type: FORM_ITEM_TYPE.Textarea
    }
};

/*银行卡信息*/
export let DSLFormItemsYHXX = {
    payrollbankcardnumber: {
        label: "工资卡帐号",
        prop: "payrollbankcardnumber"
    },
    payrollbankname: {
        label: "工资卡开户行名称",
        prop: "payrollbankname"
    },
    payrolltopbankcode: {
        label: "工资卡银行代码",
        prop: "payrolltopbankcode",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "PAY_BANK_CODE"
    },
};

/*合同信息*/
export let DSLFormItemsHTXX = {
    ps03DTO_contractcode: {
        prop: "ps03DTO_contractcode",
        label: "合同编号"
    },
    ps03DTO_contractperiodtype: {
        prop: "ps03DTO_contractperiodtype",
        label: "合同期限类型",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "CONTRACTPERIODTYPE"
    },
    ps03DTO_signdate: {
        prop: "ps03DTO_signdate",
        label: "签订日期",
        type: FORM_ITEM_TYPE.DatePicker
    },
    ps03DTO_startdate: {
        prop: "ps03DTO_startdate",
        label: "开始日期",
        type: FORM_ITEM_TYPE.DatePicker
    },
    ps03DTO_enddate: {
        prop: "ps03DTO_enddate",
        label: "结束时期",
        type: FORM_ITEM_TYPE.DatePicker
    },
    ps03DTO_unit: {
        prop: "ps03DTO_unit",
        label: "计量单位",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "UNIT"
    },
    ps03DTO_unitprice: {
        prop: "ps03DTO_unitprice",
        label: "计量单价"
    },
    ps03DTO_memo: {
        prop: "ps03DTO_memo",
        label: "备注",
        span: 24,
        type: FORM_ITEM_TYPE.Textarea
    },
};


/*基础信息布局信息*/
export let DSLFormLayoutJCXX = [
    [{
        "p": "issuecardpicurl",
        "r": 4,
        "c": 1
    }, {
        "p": "ps01DTO_idcardnumber"
    }, {
        "p": "ps01DTO_name"
    }],
    [{
        "p": "ps01DTO_gender"
    }, {
        "p": "ps01DTO_birthday"
    }],
    [{
        "p": "ps01DTO_idcardtype"
    }, {
        "p": "ps01DTO_address"
    }],
    [{
        "p": "ps01DTO_nation"
    }, {
        "p": "ps01DTO_edulevel"
    }],
    [{
        "p": "ps01DTO_startdate"
    }, {
        "p": "ps01DTO_expirydate"
    }, {
        "p": "ps01DTO_grantorg"
    }],
    [{
        "p": "ps01DTO_cultureleveltype"
    }, {
        "p": "ps01DTO_urgentlinkman"
    }, {

        "p": "ps01DTO_degree"
    }],
    [{
        "p": "ps01DTO_politicstype"
    }, {
        "p": "ps01DTO_workdate"
    }, {
        "p": "ps01DTO_urgentlinkmanphone"
    }],
    [{
        "p": "ps01DTO_joinedtime"
    }, {
        "p": "ps01DTO_specialty"
    }, {
        "p": "ps01DTO_isjoined"
    }],
    [{
        "p": "ps01DTO_cellphone"
    }, {
        "p": "ps01DTO_maritalstatus"
    }, {
        "p": ""
    }]
];


/* 详情表单 验证规则 */

export let DSLFormItemRule = {
    /* 人员基础信息 */
    ps01DTO_address: [required], //	住址
    ps01DTO_areacode: [required], //	籍贯（身份证号前6位）
    ps01DTO_cellphone: [required], //	手机号码
    ps01DTO_gender: [required], //	性别_Select选择器		
    ps01DTO_idcardnumber: [required, idCardNumber], //	证件号码
    ps01DTO_idcardtype: [required], //	证件类型_Select选择器		
    ps01DTO_name: [required], //	姓名		
    ps01DTO_nation: [required], //	民族_Select选择器		
    ps01DTO_politicstype: [required], //	政治面貌
    ps01DTO_urgentlinkmanphone: [required], //	紧急联系电话
    /* 合同信息 */
    ps03DTO_contractcode: [required], //	合同编号
    ps03DTO_contractperiodtype: [required], //	合同期限类型
    ps03DTO_enddate: [required], //	结束时期,
    ps03DTO_signdate: [required], //	签订日期
    ps03DTO_startdate: [required], //	开始日期		
    /* 参建信息 */
    tm0101: [required], //	进场时间		
    entrytime: [required], //	进场时间
    isteamleader: [required], //	是否班组长		
    payrollbankcardnumber: [required], //	工资卡帐号		
    payrollbankname: [required], //	工资卡开户行名称	
    payrolltopbankcode: [required], //	工资卡银行代码	
    worktypecode: [required] //工种
};
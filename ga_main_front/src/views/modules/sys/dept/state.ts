import {flattenToDefaultFormData} from "@/utils/transformData";

export const defaultFormQueryData = {
    personName: "",
    tm0101: ""
};

export const originApi = {
    entrytime: "", //	进场时间
    isteamleader: "", //	是否班组长
    payrollbankcardnumber: "", //	工资卡帐号
    payrollbankname: "", //	工资卡开户行名称
    payrolltopbankcode: "", //	工资卡银行代码
    tm0101: "", //	班组ID
    exittime: "", //	退场时间
    hasbuyinsurance: "", //	是否购买工伤或意外伤害保险
    issuecardpicurl: "", //	工人头像
    memo: "", //	备注
    pj0101: "", //	项目ID
    ps0101: "", //	人员ID
    ps0201: "", //	主键ID
    worktypecode: "", //工种
    ps01DTO: {
        address: "", //	住址
        areacode: "", //	籍贯（身份证号前6位）
        cellphone: "", //	手机号码
        gender: "", //	性别_Select选择器
        idcardnumber: "", //	证件号码
        idcardtype: "", //	证件类型_Select选择器
        name: "", //	姓名
        nation: "", //	民族_Select选择器
        politicstype: "", //	政治面貌
        birthday: "", //	出生日期
        cultureleveltype: "", //	文化程度_Select选择器
        degree: "", //	学位_Select选择器
        edulevel: "", //	学历_Select选择器
        expirydate: "", //	有效期结束日期
        grantorg: "", //	发证机关
        hasbadmedicalhistory: "", //	是否有重大病史_Select选择器
        headimageurl: "", //	身份证头像
        isjoined: "", //	是否加入工会_Select选择器
        joinedtime: "", //	加入工会时间
        maritalstatus: "", //	婚姻状况
        negativeidcardimageurl: "", //	反面照 URL
        positiveidcardimageurl: "", //	正面照 URL
        ps0101: "", //	主键ID
        specialty: "", //	特长
        startdate: "", //	有效期开始日期
        urgentlinkman: "", //	紧急联系人姓名
        urgentlinkmanphone: "", //	紧急联系电话
        workdate: "", //	开始工作日期
    }, //	人员基础信息
    ps03DTO: {
        contractcode: "", //	合同编号
        contractperiodtype: "", //	合同期限类型
        enddate: "", //	结束时期
        signdate: "", //	签订日期
        startdate: "", //	开始日期
        memo: "", //	备注
        ot01DTOList: [], //	合同附件
        ps0201: "", //	建筑工人ID
        ps0301: "", //	主键ID
        unit: "", //	计量单位
        unitprice: "", //	计量单价
    }, //	合同信息
};


export const defaultFormData = flattenToDefaultFormData({}, originApi);


export let state = {
    state: {
        formQueryData: {
            ...defaultFormQueryData
        },
        formData: {
            ...defaultFormData
        },
    },
    getters: {},
    mutations: {},
    actions: {}
};
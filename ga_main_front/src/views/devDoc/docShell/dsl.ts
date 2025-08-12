import {
    required
} from "@/utils/formRule";

export const MODULE_BASE_URL = "/enterprise/pj02";

/* 列表 表头 */
export let DSLTableHeader = [{
        prop: "pj0201",
        label: "主键ID"
    }, {
        prop: "pj0101",
        label: "项目ID"
    }, {
        prop: "prjname",
        label: "工程名称"
    }, {
        prop: "builderlicensenum",
        label: "施工许可证号"
    }, {
        prop: "organname",
        label: "发证机关"
    }, {
        prop: "organdate",
        label: "发证日期"
    }, {
        prop: "creator",
        label: "创建者"
    }, {
        prop: "createDate",
        label: "创建时间"
    }, {
        prop: "updater",
        label: "更新者"
    }, {
        prop: "updateDate",
        label: "更新时间"
    }

];

/* 查询框 */
export let DSLFormItemsQuery = {
    pj0201: {
        span: 8,
        prop: "pj0201",
        label: "主键ID"
    }
};

/* 详情表单 */
export let DSLFormItems = {
    pj0201: {
        label: "主键ID",
        prop: "pj0201"
    },
    pj0101: {
        label: "项目ID",
        prop: "pj0101"
    },
    prjname: {
        label: "工程名称",
        prop: "prjname"
    },
    builderlicensenum: {
        label: "施工许可证号",
        prop: "builderlicensenum"
    },
    organname: {
        label: "发证机关",
        prop: "organname"
    },
    organdate: {
        label: "发证日期",
        prop: "organdate"
    },
    creator: {
        label: "创建者",
        prop: "creator"
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
    }
};


/* 详情表单 验证规则 */

export let DSLFormItemRule = {
    pj0201: [required],
    pj0101: [required],
    prjname: [required],
    builderlicensenum: [required],
    organname: [required],
    organdate: [required],
    creator: [required],
    createDate: [required],
    updater: [required],
    updateDate: [required]
};
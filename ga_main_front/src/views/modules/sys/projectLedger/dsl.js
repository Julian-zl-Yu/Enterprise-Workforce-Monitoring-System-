import {
	FORM_ITEM_TYPE
} from "@/components/Types";

export const MODULE_BASE_URL = "/sys/projectLedger";

/* 列表 表头 */
export let DSLTableHeader = [

	{
		prop: "pj0101",
		label: "项目id"
	},
	{
		prop: "projectName",
		label: "项目名称"
	},
	{
		prop: "areaCode",
		label: "项目所属地",
		type: FORM_ITEM_TYPE.Cascader,
		bindId: "areacode.china"
	},
	{
		prop: "linkMan",
		label: "联系人"
	},
	{
		prop: "linkCellphone",
		label: "联系电话"
	},
	{
		prop: "equipmentNum",
		label: "设备数量"
	},
	//项目总体情况
	{
		prop: "totalWorkerInNum",
		label: "总在场工人"
	}, {
		prop: "totalWorkerAttendanceNum",
		label: "工人考勤数"
	}, {
		prop: "totalWorkerAttendanceRate",
		label: "工人考勤率"
	}, {
		prop: "totalManagerInNum",
		label: "总在场管理人员"
	}, {
		prop: "totalManagerAttendanceNum",
		label: "管理人员考勤数"
	}, {
		prop: "totalManagerAttendanceRate",
		label: "管理人员考勤率"
	},
	//总包单位情况
	{
		prop: "workerInNumZb",
		label: "在场工人"
	},
	{
		prop: "workerAttendanceNumZb",
		label: "工人考勤数"
	},
	{
		prop: "workerAttendanceRateZb",
		label: "工人考勤率"
	}, {
		prop: "managerInNumZb",
		label: "总在场管理人员"
	}, {
		prop: "managerAttendanceNumZb",
		label: "管理人员考勤数"
	}, {
		prop: "managerAttendanceRateZb",
		label: "管理人员考勤率"
	}, {
		prop: "projectManagerAttendanceRateZb",
		label: "项目经理到岗率"
	},
//监理单位情况
	{
		prop: "workerInNumJl",
		label: "在场工人"
	}, {
		prop: "workerAttendanceNumJl",
		label: "工人考勤数"
	}, {
		prop: "workerAttendanceRateJl",
		label: "工人考勤率"
	}, {
		prop: "managerInNumJl",
		label: "总在场管理人员"
	}, {
		prop: "managerAttendanceNumJl",
		label: "管理人员考勤数"
	}, {
		prop: "managerAttendanceRateJl",
		label: "管理人员考勤率"
	}, {
		prop: "supervisorAttendanceRateJl",
		label: "总监理考到岗率"
	},
//建设单位情况
	{
		prop: "workerInNumJs",
		label: "在场工人"
	},
	{
		prop: "workerAttendanceNumJs",
		label: "工人考勤数"
	}, {
		prop: "workerAttendanceRateJs",
		label: "工人考勤率"
	}, {
		prop: "managerInNumJs",
		label: "总在场管理人员"
	}, {
		prop: "managerAttendanceNumJs",
		label: "管理人员考勤数"
	}, {
		prop: "managerAttendanceRateJs",
		label: "管理人员考勤率"
	},
//其他单位情况
	{
		prop: "workerInNumQt",
		label: "在场工人"
	}, {
		prop: "workerAttendanceNumQt",
		label: "工人考勤数"
	}, {
		prop: "workerAttendanceRateQt",
		label: "工人考勤率"
	}, {
		prop: "managerInNumQt",
		label: "总在场管理人员"
	}, {
		prop: "managerAttendanceNumQt",
		label: "管理人员考勤数"
	}, {
		prop: "managerAttendanceRateQt",
		label: "管理人员考勤率"
	}


];
/* 查询框 */
export let DSLFormItemsQuery = {
	projectName: {
		span: 8,
		prop: "projectName",
		label: "项目名称",
		type: FORM_ITEM_TYPE.Input
	},
	areaCode: {
		span: 8,
		prop: "areaCode",
		label: "项目所在地",
		// type: FORM_ITEM_TYPE.Cascader,
		// checkStrictly: true,
		// bindId: "areacode.china"
		type: FORM_ITEM_TYPE.Select,
		bindUrl: "/common/selectAreaByDept"
	},
};
export let DSLFormItemsQuery2 = {
	projectName: {
		span: 8,
		prop: "projectName",
		label: "项目名称",
		type: FORM_ITEM_TYPE.Input
	},
	areaCode: {
		span: 8,
		prop: "areaCode",
		label: "项目所在地",
		type: FORM_ITEM_TYPE.Cascader,
		checkStrictly: true,
		bindId: "areacode.china"
	},
};


/* 详情表单 验证规则 */
export let DSLFormItemRule = {};

<template>
    <MainPage :configs="configsMainPage">
        <div v-show="currentView==='main'" class="view mod-enterprise__excelExp">
            <CFormQuery v-model="formDataQuery" :configs="configsCFormQuery" @query="getTableList"/>
            <Card>
                <div class="option-btn-wrapper">
                    <BtnGroup :configs="configsBtns" :can-delete="dataListSelections"/>
                </div>
                <el-table v-loading="dataListLoading" :data="dataList" border style="width: 100%;"
                          @selection-change="dataListSelectionChangeHandle">
                    <!-- 业务列 -->
                    <!--          <TableColumn v-for="(header) in DSLTableHeader" :key="header.prop" :configs="header"/>-->
                    <el-table-column label="项目信息" header-align="center" align="center">
                        <!--            <el-table-column label="项目ID" prop="pj0101" header-align="center" align="center"  width="200" />-->
                        <el-table-column label="项目名称" prop="projectName" header-align="center" align="center"
                                         show-overflow-tooltip width="200"/>
                        <el-table-column label="项目所属地" prop="areaCode" header-align="center" align="center"
                                         width="100"/>
                        <el-table-column label="在线设备数" prop="equipmentNum" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="项目联系人" prop="linkMan" header-align="center" align="center" width="80"/>
                        <el-table-column label="联系电话" prop="linkCellphone" header-align="center" align="center"
                                         width="200"/>
                    </el-table-column>
                    <el-table-column label="项目总体今日考勤情况" header-align="center" align="center">
                        <el-table-column label="在场工人总数" prop="totalWorkerInNum" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="工人考勤总数" prop="totalWorkerAttendanceNum" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="工人考勤率" prop="totalWorkerAttendanceRate" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="在场管理人员总数" prop="totalManagerInNum" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="管理人员考勤总数" prop="totalManagerAttendanceNum" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="管理人员考勤率" prop="totalManagerAttendanceRate" header-align="center"
                                         align="center" width="50"/>
                    </el-table-column>
                    <el-table-column label="总包单位今日考勤情况" header-align="center" align="center">
                        <el-table-column label="在场工人数" prop="workerInNumZb" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="工人考勤数" prop="workerAttendanceNumZb" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="工人考勤率" prop="workerAttendanceRateZb" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="在场管理人员数" prop="managerInNumZb" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="管理人员考勤数" prop="managerAttendanceNumZb" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="管理人员考勤率" prop="managerAttendanceRateZb" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="项目经理到岗率" prop="projectManagerAttendanceRateZb" header-align="center"
                                         align="center" width="50"/>
                    </el-table-column>
                    <el-table-column label="监理单位今日考勤情况" header-align="center" align="center">
                        <el-table-column label="在场工人数" prop="workerInNumJl" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="工人考勤数" prop="workerAttendanceNumJl" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="工人考勤率" prop="workerAttendanceRateJl" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="在场管理人员数" prop="managerInNumJl" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="管理人员考勤数" prop="managerAttendanceNumJl" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="管理人员考勤率" prop="managerAttendanceRateJl" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="总监理到岗率" prop="supervisorAttendanceRateJl" header-align="center"
                                         align="center" width="50"/>
                    </el-table-column>
                    <el-table-column label="建设单位今日考勤情况" header-align="center" align="center">
                        <el-table-column label="在场工人数" prop="workerInNumJs" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="工人考勤数" prop="workerAttendanceNumJs" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="工人考勤率" prop="workerAttendanceRateJs" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="在场管理人员数" prop="managerInNumJs" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="管理人员考勤数" prop="managerAttendanceNumJs" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="管理人员考勤率" prop="managerAttendanceRateJs" header-align="center"
                                         align="center" width="50"/>
                    </el-table-column>
                    <el-table-column label="其他单位今日考勤情况" header-align="center" align="center">
                        <el-table-column label="在场工人数" prop="workerInNumQt" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="工人考勤数" prop="workerAttendanceNumQt" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="工人考勤率" prop="workerAttendanceRateQt" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="在场管理人员数" prop="managerInNumQt" header-align="center" align="center"
                                         width="50"/>
                        <el-table-column label="管理人员考勤数" prop="managerAttendanceNumQt" header-align="center"
                                         align="center" width="50"/>
                        <el-table-column label="管理人员考勤率" prop="managerAttendanceRateQt" header-align="center"
                                         align="center" width="50"/>
                    </el-table-column>

                    <!-- 固定列 -->
                </el-table>
                <Paginationbar v-model="pageInfo" @query="query"/>
            </Card>
        </div>
    </MainPage>
</template>
<script>
	/* 混入基类方法 */
	import mixinsBasePage from "@/mixins/basePage.ts";
	import mixinsMainPage from "@/mixins/mainPage.ts";
	/* 应用状态和页面描述信息 */
	import {state, defaultFormQueryData} from "./state";
	import {DSLFormItemsQuery,DSLFormItemsQuery2, DSLTableHeader, MODULE_BASE_URL} from "./dsl";
	import {BTN_TYPE, BTN_GROUP_TYPE} from "@/components/Types";
	/*模块名*/
	const moduleName = "sys-projectLedger";
	/* 查询框store名 */
	const queryFormKeyName = "formQueryData";

	export default {
		mixins: [mixinsBasePage, mixinsMainPage],
		data() {
			const this_ = this;
			return {
				moduleName,
				queryFormKeyName,
				formDataQuery: {...defaultFormQueryData},
				/*列表外操作*/
				configsBtns: {
					btnType: BTN_GROUP_TYPE.Btn,
					btnList: [
						{
							type: BTN_TYPE.export,
							click: () => this_.excelExp(),
						}
					],
				},
				configsMainPage: {moduleName, state, shiro: ["sys:projectLedger:page"]},
				configsCFormQuery: {
					moduleName,
					keyForm: queryFormKeyName, //在state里面对应的表单索引字段
					DSLFormItems: this.$store.state.user.superAdmin==1?DSLFormItemsQuery2:DSLFormItemsQuery, //查询表单的查询项描述
				},
				DSLTableHeader,
				moduleOptions: {
					getDataListURL: MODULE_BASE_URL + "/page",
					getDataListIsPage: true,
					exportURL: MODULE_BASE_URL + "/export",
					deleteIsBatch: false,
				},
			};
		},
		methods: {
			excelExp: function () {
				const this_ = this;
				// if (!this_.formDataQuery.type ||  !this_.formDataQuery.month) {
				//   return this_.$message.error("请先选择操作类型和年月");
				// }
				this_.exportHandler();
			},
		}
	};
</script>

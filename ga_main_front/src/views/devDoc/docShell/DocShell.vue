<template>
  <MainPage :configs="configsMainPage">
    <div class="view">
      <CFormQuery
        :configs="configsCFormQuery"
        @query="getTableList"
      />
      <el-card
        shadow="never"
        class="aui-card--fill"
      >
        <div class="mod-enterprise__pj02">
          <div class="option-btn-wrapper">
            <BtnGroup
              :configs="configsBtns"
              :can-delete="dataListSelections"
            />
          </div>
          <el-table
            v-loading="dataListLoading"
            :data="dataList"
            border
            style="width: 100%;"
            @selection-change="dataListSelectionChangeHandle"
          >
            <!-- 选择列 -->
            <el-table-column
              type="selection"
              header-align="center"
              align="center"
              width="50"
            />
            <!-- 业务列 -->
            <template v-for="(header) in DSLTableHeader">
              <TableColumn
                :key="header.prop"
                :configs="header"
              />
            </template>
            <!-- 固定列 -->
            <el-table-column
              :label="$t('handle')"
              fixed="right"
              header-align="center"
              align="center"
              width="150"
            >
              <template slot-scope="scope">
                <BtnGroup
                  :configs="configsListBtns"
                  :scope="scope"
                />
              </template>
            </el-table-column>
          </el-table>
          <Paginationbar
            v-model="pageInfo"
            @query="query"
          />
        </div>
      </el-card>
    </div>
    <!-- 弹窗, 新增 / 修改 -->
    <Pj02Sub
      v-if="currentView==='add'"
      ref="addOrUpdate"
      @refreshDataList="getTableList"
    />
    <Pj02Sub
      v-if="currentView==='update'"
      ref="addOrUpdate"
      @refreshDataList="getTableList"
    />
  </MainPage>
</template>

<script>
/* 混入基类方法 */
import mixinsMainPage from "@/mixins/mainPage.ts";
import mixinsBasePage from "@/mixins/basePage.ts";
/* 应用状态和页面描述信息 */
import { state, defaultFormQueryData } from "./state";
import { DSLFormItemsQuery, DSLTableHeader, MODULE_BASE_URL } from "./dsl";
import { BTN_TYPE, BTN_GROUP_TYPE } from "@/components/Types";
/* 组件 */
import Pj02Sub from "./Pj02Sub";

/*模块名*/
const moduleName = "EnterprisePj02";
/* 查询框store名 */
const queryFormKeyName = "formQueryData";

export default {
  components: { Pj02Sub },
  mixins: [mixinsBasePage, mixinsMainPage],
  data() {
    var _this = this;
    return {
      moduleName,
      queryFormKeyName,
      /*列表外操作*/
      configsBtns: {
        btnType: BTN_GROUP_TYPE.Btn,
        btnList: [
          {
            type: BTN_TYPE.export,
            click: () => _this.exportHandler(),
          },
          {
            type: BTN_TYPE.add,
            shiro: "enterprise:pj02:save",
            click: () => _this.goSub({ v: "add" }),
          },
          // {
          //   type: BTN_TYPE.delete,
          //   shiro: "enterprise:pj02:delete",
          //   click: () => _this.deleteBy(),
          // },
        ],
      },
      /*列表内操作按钮*/
      configsListBtns: {
        btnType: BTN_GROUP_TYPE.Btnmini,
        btnList: [
          {
            type: BTN_TYPE.update,
            shiro: "enterprise:pj02:update",

            click: (scope) =>
              _this.goSub({ v: "update", id: scope.row.pj0201 }),
          },
          // {
          //   type: BTN_TYPE.delete,
          //   shiro: "enterprise:pj02:delete",

          //   click: (scope) => _this.deleteBy(scope.row.pj0201),
          // },
        ],
      },
      configsMainPage: { moduleName, state },
      configsCFormQuery: {
        moduleName,
        keyForm: queryFormKeyName, //在state里面对应的表单索引字段
        DSLFormItems: DSLFormItemsQuery, //查询表单的查询项描述
      },
      DSLTableHeader,
      moduleOptions: {
        getDataListURL: MODULE_BASE_URL + "/page",
        getDataListIsPage: true,
        exportURL: MODULE_BASE_URL + "/export",
        deleteURL: MODULE_BASE_URL,
        deleteIsBatch: true,
      },
    };
  },
};
</script>

<template>
  <MainPage :configs="configsMainPage">
    <ViewMain v-show="currentView == 'main'" />
    <!-- 弹窗, 新增 / 修改 -->
    <ViewSub
      v-if="currentView === 'update'"
      ref="addOrUpdate"
      :current-view="currentView"
      @refreshDataList="getTableList"
    />
    <ViewSub
      v-if="currentView === 'detail'"
      ref="addOrUpdate"
      :current-view="currentView"
      @refreshDataList="getTableList"
    />
  </MainPage>
</template>

<script>
/* 混入基类方法 */
import mixinsBasePage from "@/mixins/basePage.ts";
import mixinsMainPage from "@/mixins/mainPage.ts";
/* 应用状态和页面描述信息 */
import { state, defaultFormQueryData } from "./state";
import { DSLFormItemsQuery, DSLTableHeader, MODULE_BASE_URL } from "./dsl";
import { BTN_TYPE, BTN_GROUP_TYPE } from "@/components/Types";
/* 组件 */
import ViewMain from "./ViewMain.vue";
import ViewSub from "./ViewSub";
import localStorageHelper from '@/utils/localStorageHelper';

/*模块名*/
const moduleName = "enterprise-cp01-cp01";
/* 查询框store名 */
const queryFormKeyName = "formQueryData";

export default {
  components: { ViewSub, ViewMain },
  mixins: [mixinsBasePage, mixinsMainPage],
  provide() {
    const cp01 = this;
    return {
      cp01,
    };
  },
  data() {
    var _this = this;
    return {
      moduleName,
      queryFormKeyName,
      formDataQuery: { ...defaultFormQueryData },
      /*列表内操作按钮*/
      configsListBtns: {
        btnType: BTN_GROUP_TYPE.Btnmini,
        btnList: [
          {
            type: BTN_TYPE.detail,
            shiro: "enterprise:cp01:info",
            click: (scope) =>
              _this.goSub({ v: "detail", id: scope.row.cp0101 }),
          },
          {
            type: BTN_TYPE.update,
            shiro: "enterprise:cp01:update",
            click: (scope) =>
              _this.goSub({ v: "update", id: scope.row.cp0101 }),
          }
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
  mounted(){
    console.log(this.currentView);
  }
};
</script>

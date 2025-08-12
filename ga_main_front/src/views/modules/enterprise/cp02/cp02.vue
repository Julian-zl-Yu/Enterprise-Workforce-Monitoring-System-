<template>
  <MainPage
    :configs="configsMainPage"
  >
    <ViewMain v-show="currentView === 'main'" />

    <ViewSub
    v-if="currentView === 'detail'"
    ref="addOrUpdate"
    :current-view="currentView"
    @refreshDataList="getTableList"
  />

    <ViewSub
    v-if="currentView === 'update'"
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
import ViewMain from "./ViewMain";
import ViewSub from "./ViewSub";

/*模块名*/
const moduleName = "enterprise-cp02-cp02";
/* 查询框store名 */
const queryFormKeyName = "formQueryData";

export default {
  components: { ViewMain, ViewSub },
  mixins: [mixinsBasePage, mixinsMainPage],
  provide() {
    const cp02 = this;
    return {
      cp02,
    };
  },
  data() {
    var _this = this;
 
    return {
      cp0201: "",
      moduleName,
      queryFormKeyName,
      formDataQuery: { ...defaultFormQueryData },
     
      /*列表内操作按钮*/
      configsListBtns: {
        btnType: BTN_GROUP_TYPE.Btnmini,
        btnList: [
          {
            type: BTN_TYPE.detail,
            shiro: "enterprise:cp02:info",
            click: (scope) => {
                _this.goSub({ v: "detail", id: scope.row.cp0201 });
            },
          },
          {
            type: BTN_TYPE.update,
            shiro: "enterprise:cp02:update",
            text: "审核",
            click: (scope) => {
                _this.goSub({ v: "update", id: scope.row.cp0201 });
            },
          },
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
  methods: {
  
  },
};
</script>
<style lang="scss" scoped>
.checkBoxHeight {
  height: 100% !important;
}
</style>

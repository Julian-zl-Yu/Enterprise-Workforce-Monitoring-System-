<template>
  <MainPage :configs="configsMainPage">
    <ViewMain v-show="currentView === 'main'" />
    <ViewSub
      v-if="currentView === 'add'"
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
    <ViewSub
      v-if="currentView === 'saveDeviceCopy'"
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
const moduleName = "sys-frequentlyAskedQuestion-frequentlyAskedQuestion";
/* 查询框store名 */
const queryFormKeyName = "formQueryData";

export default {
  components: { ViewMain, ViewSub },
  mixins: [mixinsBasePage, mixinsMainPage],
  provide() {
    const kq01 = this;
    return {
      kq01,
    };
  },

  data() {
    var _this = this;
    return {
      moduleName,
      queryFormKeyName,
      formDataQuery: { ...defaultFormQueryData },
      /*列表外操作*/
      configsBtns: {
        btnType: BTN_GROUP_TYPE.Btn,
        btnList: [
          {
            type: BTN_TYPE.add,
            shiro: "enterprise:kq01:save",
            click: () => _this.goSub({ v: "add" }),
          },
          // {
          //   type: BTN_TYPE.delete,
          //   shiro: "enterprise:kq01:delete",
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
            shiro: "enterprise:kq01:update",

            click: (scope) =>
              _this.goSub({ v: "update", id: scope.row.id }),
          },
          {
            type: BTN_TYPE.delete,
            shiro: "enterprise:kq01:delete",

            click: (scope) => _this.deleteBy(scope.row.id),
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
        deleteIsBatchKey: "kq0101",
      },
    };
  },
};
</script>

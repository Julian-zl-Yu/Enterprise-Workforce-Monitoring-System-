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
      v-if="currentView === 'add'"
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
import { SuccessOrFailed } from "@/utils/index.ts";

/*模块名*/
const moduleName = "enterprise-cp03-cp03";
/* 查询框store名 */
const queryFormKeyName = "formQueryData";

export default {
  components: { ViewSub, ViewMain },
  mixins: [mixinsBasePage, mixinsMainPage],
  provide() {
    const cp03 = this;
    return {
      cp03,
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
            shiro: "enterprise:cp03:save",
            click: async () =>{
              // 判断能否进行新增
              let { data: res } = await this.$http.get(`${MODULE_BASE_URL}/canAdd`);
              SuccessOrFailed(
                res,
                {
                  /*成功 没有msg属性则不弹出提示信息*/
                  fn: () => {
                    _this.goSub({ v: "add" });
                  },
                },
                /*失败 msg必填*/
                { msg: res.msg }
              );  
            }
          }, 
        ],
      },
      /*列表内操作按钮*/
      configsListBtns: {
        btnType: BTN_GROUP_TYPE.Btnmini,
        btnList: [
          {
            type: BTN_TYPE.detail,
            shiro: "enterprise:cp03:info",
            click: (scope) =>
              _this.goSub({ v: "detail", id: scope.row.cp0301 }),
          },
          {
            type: BTN_TYPE.update,
            shiro: "enterprise:cp03:update",
            click: (scope) =>
              _this.goSub({ v: "update", id: scope.row.cp0301 }),
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

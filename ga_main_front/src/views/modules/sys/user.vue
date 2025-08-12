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
  </MainPage>
</template>

<script>
/* 混入基类方法 */
import mixinsBasePage from "@/mixins/basePage.ts";
import mixinsMainPage from "@/mixins/mainPage.ts";
import { FORM_ITEM_TYPE, BTN_TYPE, BTN_GROUP_TYPE } from "@/components/Types";

import ViewMain from "./UserViewMain";
import ViewSub from "./UserViewSub";

import Vue from "vue";
export default Vue.extend({
  components: { ViewMain, ViewSub },
  mixins: [mixinsBasePage, mixinsMainPage],
  provide() {
    const userModule = this;
    return {
      userModule,
    };
  },
  data() {
    var _this = this;
    return {
      moduleName: "sys-user",
      configsMainPage: { moduleName: "sys-user" },
      /* 查询表单集合 */
      formDataQuery: {
        username: "",
        deptId: "",
        gender: "",
      },
      configsCFormQuery: {
        DSLFormItems: {
          username: {
            prop: "username",
            label: _this.$t("user.username"),
          },
          gender: {
            prop: "gender",
            label: _this.$t("user.gender"),
            type: FORM_ITEM_TYPE.Dict,
            bindId: "GENDER",
          },
          deptId: {
            prop: "deptId",
            label: _this.$t("dept.title"),
            type: FORM_ITEM_TYPE.Dept,
          },
        }, //查询表单的查询项描述
      },
      configsBtns: {
        btnType: BTN_GROUP_TYPE.Btn,
        btnList: [
          {
            type: BTN_TYPE.add,
            shiro: "sys:user:save",
            click: () => _this.goSub({ v: "add" }),
          },
          {
            type: BTN_TYPE.export,
            shiro: "sys:user:export",
            click: () => _this.exportHandler(),
          },
          {
            text: "密码重置",
            shiro: "sys:user:resetPassword",
            click: () => _this.resetPassword(),
          },
          // {
          //   type: BTN_TYPE.delete,
          //   shiro: "sys:user:delete",
          //   click: (scope) => _this.deleteBy(scope.row.cp0201),
          // },
        ],
      },
      configsListBtns: {
        btnType: BTN_GROUP_TYPE.Btnmini,
        btnList: [
          {
            type: BTN_TYPE.update,
            shiro: "sys:user:update",
            click: (scope) => _this.goSub({ v: "update", id: scope.row.id }),
          },
          // {
          //   type: BTN_TYPE.delete,
          //   shiro: "sys:user:delete",
          //   click: (scope) => _this.deleteBy(scope.row.id),
          // },
        ],
      },
      moduleOptions: {
        getDataListURL: "/sys/user/page",
        getDataListIsPage: true,
        deleteURL: "/sys/user",
        deleteIsBatch: true,
        exportURL: "/sys/user/export",
      },
    };
  },
  methods: {
    async resetPassword() {
      try {
        if (this.dataListSelections.length <= 0) {
          return this.$message({
            message: this.$t("请至少选择一条数据"),
            type: "warning",
          });
        }
        let { data: res } = await this.$http.put(
          "/sys/user/resetPassword/",
          this.dataListSelections.map(
            (item) => item[this.moduleOptions.deleteIsBatchKey]
          )
        );
        if (res.code !== 0) {
          return this.$message.error(res.msg);
        }
        this.$message({
          message: this.$t("密码重置成功"),
          type: "success",
          duration: 500,
          onClose: () => {
            this.query();
          },
        });
      } catch (error) {
        console.error(error);
      }
    },
  },
});
</script>

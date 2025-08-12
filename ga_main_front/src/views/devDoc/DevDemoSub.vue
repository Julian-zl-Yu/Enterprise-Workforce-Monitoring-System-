<template>
  <SubPage
    v-loading="isFormLoading"
    :configs="configsSubPage"
  >
    <!--<Card header="基础信息——flex默认布局">
          <jsx-example>test jsx example</jsx-example>
          <CForm
            ref="form"
            :configs="configsCForm"
            @enter="$refs.submitBtn.submit()"
          />
    </Card>-->

    <Card header="基础信息——以Table布局">
      <CForm
        ref="form"
        :configs="configsCFormWithLayout"
        @enter="$refs.submitBtn.submit()"
      />
    </Card>
    <!--<Card header="施工许可证">
            <AreaPicker/>
    </Card>-->
    <!--<Card header="项目合同">-->
    <!--d-->
    <!--</Card>-->
    <template slot="footer">
      <Btn :configs="{type:BTN_TYPE.back,click:goMain}" />
      <Btn
        ref="submitBtn"
        :configs="configsBtnDebounce"
      />
    </template>
  </SubPage>
</template>

<script>
import basePage from "@/mixins/basePage.ts";
import { mixinsSubPage, subPropCurrentView } from "@/mixins/subPage.ts";
import {
  DSLFormItems,
  DSLFormItemRule,
  MODULE_BASE_URL,
  DSLFormLayout,
} from "./dsl";

import { SuccessOrFailed, checkFormData, Loading } from "@/utils/index.ts";
import { defaultFormData } from "./state";
import cloneDeep from "lodash/cloneDeep";
import { fnAsyncValidator } from "@/utils/formRule";
import { BTN_TYPE } from "@/components/Types";

/*模块名*/
const moduleName = "DevDemo";

export default {
  name: moduleName + "Sub",
  mixins: [basePage, mixinsSubPage, subPropCurrentView],
  data() {
    const handleFormSubmit = this.handleFormSubmit.bind(this);
    const AsyncValidatorRequired = {
      vm: this /*当前vue实例*/,
      form: "form" /*CForm 的ref name */,
      prop: "devicename" /*需要校验的字段prop name*/,
    };
    const _DSLFormItemRule = cloneDeep(DSLFormItemRule);

    _DSLFormItemRule.devicename.push(
      fnAsyncValidator({
        ...AsyncValidatorRequired,
        validateRequest: (value /* 需要验证的值 */) =>
          this.$http.post("/sys/user/info", value).then(function () {}),
      })
    );

    return {
      isFormLoading: false,
      configsSubPage: { moduleName },
      /*提交按钮*/
      configsBtnDebounce: {
        type: BTN_TYPE.save,
        textLoading: "正在提交...",
        debounceClick: handleFormSubmit,
      },
      configsCForm: {
        moduleName,
        DSLFormItems: cloneDeep(DSLFormItems),
        DSLFormItemRule: cloneDeep(_DSLFormItemRule),
        readOnly: true,
      },
      configsCFormWithLayout: {
        divider: "table方式布局表单",
        moduleName,
        DSLFormItems,
        DSLFormItemRule: _DSLFormItemRule,
        tableLayout: true,
        DSLFormLayout,
      },
    };
  },
  
  async created() {
    window.DevApp = this;
    this.init();
  },
  methods: {
    init() {
      if (["update"].includes(this.currentView)) {
        this.getInfo(this.$route.query.id);
      }
      /* 如果是view 只读视图 */
      this.$watch(
        "currentView",
        (viewName) => {
          Loading(
            (isLoading) => {
              this.isFormLoading = isLoading;
            },
            () => {
              const isShowDetail = "detail" === viewName ? true : false;
              if (this.configsCForm.readOnly !== isShowDetail) {
                this.configsCForm.readOnly = !this.configsCForm.readOnly;
              }
            }
          );
        },
        { immediate: true }
      );

      setTimeout(() => {
        console.log(`this.configsCForm.disabled${this.configsCForm.disabled}`);
      }, 1000);
    },
    // 获取表单信息
    async getInfo(id) {
      this.isFormLoading = true;
      try {
        let { data: res } = await this.$http.get(`${MODULE_BASE_URL}/${id}`);
        SuccessOrFailed(
          res,
          {
            /*成功 没有msg属性则不弹出提示信息*/
            fn: () => {
              this.formData = checkFormData(defaultFormData, res.data);
            },
          },
          /*失败 msg必填*/
          { msg: res.msg }
        );
      } catch (error) {
        console.error(error);
      } finally {
        this.isFormLoading = false;
      }
    },
    // 表单提交
    async handleFormSubmit() {
      await this.$refs["form"]?.formValidate();
      let { data: res } = await this.$http[
        !this.formData.pj0101 ? "post" : "put"
      ](MODULE_BASE_URL, this.formData);
      return SuccessOrFailed(
        res,
        {
          msg: "表单信息已保存",
          fn: () => {
            this.$emit("refreshDataList");
            this.goMain();
          },
        },
        { msg: res.msg }
      );
    },
  },
};
</script>

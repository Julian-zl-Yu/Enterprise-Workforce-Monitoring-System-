<template>
  <SubPage>
    <CForm
      ref="form"
      :configs="configsCForm"
      @enter="$refs.submitBtn.submit()"
    />
    <template slot="footer">
      <Btn :configs="{type:BTN_TYPE.back,click:goMain}" />
      <Btn
        v-if="~['update','add'].indexOf(currentView)"
        ref="submitBtn"
        :configs="configsBtnDebounce"
      />
    </template>
  </SubPage>
</template>

<script>
import { DSLFormItems, DSLFormItemRule, MODULE_BASE_URL } from "./dsl";

import basePage from "@/mixins/basePage.ts";
import { SuccessOrFailed } from "@/utils/index.ts";
import _merge from "lodash/merge";
import { BTN_TYPE } from "@/components/Types";


/*模块名*/
const moduleName = "EnterprisePj02";

export default {
  name: "EnterprisePj02Sub",
  mixins: [basePage],
  data() {
    const handleFormSubmit = this.handleFormSubmit.bind(this);
    return {
      configsSubPage:{moduleName},
      /*提交按钮*/
      configsBtnDebounce: {
        type: BTN_TYPE.save,
        textLoading: "正在提交...",
        debounceClick: handleFormSubmit,
      },
      configsCForm: {
        moduleName,
        DSLFormItems,
        DSLFormItemRule
      }
    };
  },

  created() {
    this.init();
  },
  methods: {
    init() {
      if(this.$route.query.id){
        this.getInfo(this.$route.query.id);
      }
    },
    // 获取表单信息
    async getInfo(id) {
      try {
        let { data: res } = await this.$http.get(`${MODULE_BASE_URL}/${id}`);
        SuccessOrFailed(
          res,
          {/*成功 没有msg属性则不弹出提示信息*/
            fn: () => {this.formData = _merge({ ...res.data });}
          },
          /*失败 msg必填*/
          { msg: res.msg }
        );
      } catch (error) {
        console.error(error);
      }
    },
    // 表单提交
    async handleFormSubmit() {
      await this.$refs["form"]?.formValidate();
      let { data: res } = await this.$http[!this.formData.pj0201 ? "post" : "put"](MODULE_BASE_URL, this.formData);
      return SuccessOrFailed(
        res,
        {
          msg: "表单信息已保存",
          fn: () => {
            this.$emit("refreshDataList");
            this.goMain();
          }
        },
        { msg: res.msg }
      );
    }
  }
};
</script>

<template>
  <SubPage>
    <Card header="基本信息">
      <CForm
        v-model="formData"
        v-loading="isFormLoading"
        :configs="configsCForm"
      />
    </Card>

    <Card header="审核" v-if="~['update', 'add'].indexOf(currentView)">
      <CForm
        ref="form"
        v-model="formData"
        v-loading="isFormLoading"
        :configs="configsCFormSH"
        @enter="$refs.submitBtn.submit()"
      />
    </Card>

    <template slot="footer">
      <Btn :configs="{ type: BTN_TYPE.back, click: goMain }" />
      <Btn
        v-if="~['update', 'add'].indexOf(currentView)"
        ref="submitBtn"
        :configs="configsBtnDebounce"
      />
    </template>
  </SubPage>
</template>

<script>
import basePage from "@/mixins/basePage.ts";
import { mixinsSubPage, subPropCurrentView } from "@/mixins/subPage.ts";
import { DSLFormItems, DSLFormItemsStatus, DSLFormItemRule, DSLFormItemRuleStatus, MODULE_BASE_URL } from "./dsl";
import { SuccessOrFailed, checkFormData, checkFile } from "@/utils/index.ts";
import { defaultFormData } from "./state";
import { BTN_TYPE, FILE_TYPES } from "@/components/Types";
import * as echarts from 'echarts';

/*模块名*/
const moduleName = "EnterpriseCp03";

export default {
  name: "EnterpriseCp03Sub",
  mixins: [basePage, mixinsSubPage, subPropCurrentView],
  data() {
    const handleFormSubmit = this.handleFormSubmit.bind(this);

    if(this.currentView !== 'add'){
      DSLFormItems.yearMonth.disabled = true
    } else {
      DSLFormItems.yearMonth.disabled = false
    }


    return {
      configsSubPage: { moduleName },
      /*提交按钮*/
      configsBtnDebounce: {
        type: BTN_TYPE.save,
        textLoading: "正在提交...",
        debounceClick: handleFormSubmit,
      },
      formData: window._.cloneDeep(defaultFormData),
      configsCForm: {
        moduleName,
        DSLFormItems,
        DSLFormItemRule,
        readOnly: true,
      },
      configsCFormSH:{
        moduleName,
        DSLFormItems: DSLFormItemsStatus,
        DSLFormItemRule :DSLFormItemRuleStatus,
        readOnly: !~['update', 'add'].indexOf(this.currentView),
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
        !this.formData.cp0101 ? "post" : "put"
      ](MODULE_BASE_URL, this.formData);
      return SuccessOrFailed(
        res,
        {
          msg: res.msg,
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

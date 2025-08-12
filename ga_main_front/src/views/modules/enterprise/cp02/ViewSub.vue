<template>
  <SubPage>
    <Card header="企业信息">
      <CForm ref="formJCXX" v-model="formData" v-loading="isFormLoading" :configs="configsCForm" />
    </Card>

    <Card header="企业附件">
      <Viewer v-model="formData.ot01DTOList" :configs="configsViewer" />
    </Card>

    <!-- 审核必须填，当不通过时出现备注,且必须填 -->
    <Card header="审核" v-if="~['update', 'add'].indexOf(currentView)">
      <CForm ref="form" v-model="formDataSH" :configs="configsCFormStatus" @enter="$refs.submitBtn.submit()" />
    </Card>

    <template slot="footer">
      <Btn :configs="{ type: BTN_TYPE.back, click: goMain }" />
      <Btn ref="submitBtn" :configs="configsBtnDebounce"  v-if="~['update', 'add'].indexOf(currentView)"/>
    </template>
  </SubPage>
</template>

<script lang="ts">
  import merge from "lodash/merge";
  import basePage from "@/mixins/basePage.ts";
  import debounce from "lodash/debounce";
  import {
    BTN_TYPE
  } from "@/components/Types";
  import {
    isCorpcode
  } from "@/utils/validate.ts";
  import {
    mixinsSubPage,
    subPropCurrentView
  } from "@/mixins/subPage.ts";
  import {
    defaultFormData
  } from "./state";
  import {
    SuccessOrFailed,
    checkFormData
  } from "@/utils/index.ts";
  import {
    DSLFormItemsStatus,
    DSLFormItemsStatus2,
    DSLFormItems,
    DSLFormItemRule,
    MODULE_BASE_URL,
    DSLFormLayoutJCXX,
    DSLFormItemRuleStatus,
    DSLFormItemRuleStatus2,
  } from "./dsl";
  import Vue from "vue";
  import {
    required
} from "@/utils/formRule";
  /*模块名*/
  const moduleName = "EnterpriseCp02";

  export default Vue.extend({
    name: "EnterpriseCp02Sub",
    mixins: [basePage, mixinsSubPage, subPropCurrentView],
    data() {
      const handleFormSubmit = this.handleFormSubmit.bind(this);
      return {
        configsSubPage: {
          moduleName
        },
        /*提交按钮*/
        configsBtnDebounce: {
          type: BTN_TYPE.save,
          textLoading: "正在提交...",
          debounceClick: handleFormSubmit,
        },
        formData: window._.cloneDeep(defaultFormData),
        configsCForm: {
          moduleName,
          DSLFormItems: DSLFormItems,
          DSLFormItemRule,
          DSLFormLayout: DSLFormLayoutJCXX,
          readOnly: true
        },
        configsCFormStatus: {
          moduleName,
          DSLFormItems: DSLFormItemsStatus,
          DSLFormItemRule: DSLFormItemRuleStatus,
        },
        configsViewer: {
          fileType: "01",
          tips: "只允许jpg、png、pdf",
          readOnly: true,
          uploader: false,
          check(file) {
            return checkFile(file, [
              FILE_TYPES.jpg,
              FILE_TYPES.png,
              FILE_TYPES.pdf,
            ]);
          },
        },
        formDataSH: {
          cp02Status: ''
        }
      };
    },
    created() {
      this.init();
    },
    mounted() {
      this.$watch(
        "formDataSH.cp02Status",
        (value) => {
          if (value == '2') {
            this.configsCFormStatus.DSLFormItems = DSLFormItemsStatus2
            this.configsCFormStatus.DSLFormItemRule = DSLFormItemRuleStatus2
          } else if (value == '1') {
            this.configsCFormStatus.DSLFormItems = DSLFormItemsStatus
            this.configsCFormStatus.DSLFormItemRule = DSLFormItemRuleStatus
          }
        }, {
          immediate: true
        }
      );
    },
    methods: {
      init() {
        this.getInfo(this.$route.query.id);
      },
      // 获取表单信息
      async getInfo(id) {
        this.isFormLoading = true;
        try {
          let {
            data: res
          } = await this.$http.get(`${MODULE_BASE_URL}/${id}`);
          SuccessOrFailed(
            res, {
              /*成功 没有msg属性则不弹出提示信息*/
              fn: () => {
                this.formData = checkFormData(defaultFormData, res.data);
              },
            },
            /*失败 msg必填*/
            {
              msg: res.msg
            }
          );
        } catch (error) {
          console.error(error);
        } finally {
          setTimeout(async () => (this.isFormLoading = false), 30);
        }
      },

      // 表单提交
      async handleFormSubmit() {
        await this.$refs["form"]?.formValidate();


        let url = '';
        if (this.formDataSH.cp02Status == '1') {
          //通过
          url = MODULE_BASE_URL + '/agree'
        } else {
          // 不通过
          url = MODULE_BASE_URL + '/disagree'
          this.formData.reason = this.formDataSH.reason;
        }

        let {
          data: res
        } = await this.$http["put"](url, this.formData);
        return SuccessOrFailed(
          res, {
            msg: "表单信息已保存",
            fn: () => {
              this.$emit("refreshDataList");
              this.goMain();
            },
          }, {
            msg: res.msg
          }
        );
      }
    },
  });
</script>
<style lang="scss" scoped>
  .checkBoxHeight {
    height: 100% !important;
  }
</style>
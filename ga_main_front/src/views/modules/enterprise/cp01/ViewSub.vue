<template>
  <SubPage>
    <Card header="基本信息">
      <CForm
        ref="form"
        v-model="formData"
        v-loading="isFormLoading"
        :configs="configsCForm"
        @enter="$refs.submitBtn.submit()"
      />
    </Card>

    <Card header="企业附件">
      <Viewer v-model="formData.ot01DTOList" :configs="configsViewer" />
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
import { DSLFormItems, DSLFormItemRule, MODULE_BASE_URL,MODULE_BASE_URL_TYC} from "./dsl";
import { SuccessOrFailed, checkFormData, checkFile } from "@/utils/index.ts";
import { defaultFormData } from "./state";
import { BTN_TYPE, FILE_TYPES } from "@/components/Types";

/*模块名*/
const moduleName = "EnterpriseCp01";

export default {
  name: "EnterpriseCp01Sub",
  mixins: [basePage, mixinsSubPage, subPropCurrentView],
  data() {
    const handleFormSubmit = this.handleFormSubmit.bind(this);
    DSLFormItems.corpcode.done = this.handTycClickDone.bind(this);
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
        readOnly: !~['update', 'add'].indexOf(this.currentView),
      },
      configsViewer: {
        fileType: "01",
        tips: "只允许jpg、png、pdf",
        readOnly: !~['update', 'add'].indexOf(this.currentView),
        uploader: !!~['update', 'add'].indexOf(this.currentView),
        check(file) {
          return checkFile(file, [
            FILE_TYPES.jpg,
            FILE_TYPES.png,
            FILE_TYPES.pdf,
          ]);
        },
      },
    };
  },

  created() {
    this.init();
  },
  watch: {
    'formData.industry': function(val){
         if(val){
          if(val == '1'){
            this.formData.estate = '1'
          } else if(['2','3','4','5'].includes(val)){
            this.formData.estate = '2'
          } else {
            this.formData.estate = '3'
          }
        } else {
          this.formData.estate = ''
        } 
    }
  },
  methods: {
    init() {
        // 企业用户，状态不可编辑
        if(this.$store.state.user.userType == '2'){
          DSLFormItems.ablestatus.disabled = true;
        }

        this.getInfo(this.$route.query.id);
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
   async handTycClickDone(val){
      this.isFormLoading = true;
      try {
        let { data: res } = await this.$http.get(`${MODULE_BASE_URL_TYC}/${val}`);

        SuccessOrFailed(res,
        {
          fn: ()=>{
            res = res.data;
            for(let i in res){
                if(res[i]){
                  this.formData[i] = res[i];
                }
            }
          }
        },
        { msg: res.msg })
      } catch (error) {
        
      } finally {
        this.isFormLoading = false;
      }


     
    }
  },
};
</script>

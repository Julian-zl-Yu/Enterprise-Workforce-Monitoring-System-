<!--  流程业务表单   -->
<template>
  <el-card 
    shadow="never" 
    class="app-card--fill"
  >
    <el-form 
      ref="dataForm" 
      :model="dataForm" 
      :rules="dataRule" 
      :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'" 
      @keyup.enter.native="dataFormSubmitHandle()"
    >
      <el-form-item 
        :label="$t('correction.post')" 
        prop="applyPost"
      >
        <el-input 
          v-model="dataForm.applyPost" 
          :disabled="fieldDisabled" 
          :placeholder="$t('correction.post')"
        />
      </el-form-item>
      <el-row :gutter="40">
        <el-col :span="12">
          <el-form-item 
            :label="$t('correction.entryDate')" 
            prop="entryDate"
          >
            <el-date-picker 
              v-model="dataForm.entryDate" 
              :disabled="fieldDisabled" 
              :placeholder="$t('correction.entryDate')" 
              value-format="yyyy-MM-dd" 
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item 
            :label="$t('correction.correctionDate')" 
            prop="correctionDate"
          >
            <el-date-picker 
              v-model="dataForm.correctionDate" 
              :disabled="fieldDisabled" 
              :placeholder="$t('correction.correctionDate')" 
              value-format="yyyy-MM-dd" 
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item 
        :label="$t('correction.workContent')" 
        prop="workContent"
      >
        <el-input 
          v-model="dataForm.workContent" 
          :disabled="fieldDisabled" 
          :placeholder="$t('correction.workContent')" 
          type="textarea"
        />
      </el-form-item>
      <el-form-item 
        :label="$t('correction.achievement')" 
        prop="achievement"
      >
        <el-input 
          v-model="dataForm.achievement" 
          :disabled="fieldDisabled" 
          :placeholder="$t('correction.achievement')" 
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <!--  流程综合组件   -->
    <ren-process-multiple 
      v-if="processVisible" 
      ref="renProcessMultiple" 
      update-instance-id-url="/act/demo/correction/updateInstanceId" 
      save-form-url="/act/demo/correction" 
      data-form-name="dataForm"
    />
  </el-card>
</template>

<script>
// 引入工作流公共方法
import processModule from "@/mixins/process-module";
export default {
    // 注入公共方法
    mixins: [processModule],
    data () {
        return {
            visible: false,
            // 表单属性是否可编辑
            fieldDisabled: false,
            dataForm: {
                id: "",
                applyPost: "",
                entryDate: "",
                correctionDate: "",
                workContent: "",
                achievement: "",
                creator: "",
                createDate: ""
            }
        };
    },
    computed: {
        dataRule () {
            return {
                applyPost: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                entryDate: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                correctionDate: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                workContent: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                achievement: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                createTime: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ]
            };
        }
    },
    created () {
    // 将业务KEY赋值给表单
        this.dataForm.id = this.$route.params.businessKey;
        this.init();
        // 流程回调
        var callbacks = {
            startProcessSuccessCallback: this.closeCurrentTab,
            startProcessErrorCallback: this.startProcessErrorCallback,
            taskHandleSuccessCallback: this.closeCurrentTab,
            taskHandleErrorCallback: this.taskHandleErrorCallback,
            formSaveSuccessCallback: null,
            formSaveErrorCallback: null
        };
        // 初始化综合组件
        this.initProcessMultiple(callbacks);
    },
    methods: {
        init () {
            this.visible = true;
            this.$nextTick(() => {
                this.$refs["dataForm"].resetFields();
                if (this.dataForm.id) {
                    // 如业务KEY已存在，不允许编辑
                    this.fieldDisabled = true;
                    this.getInfo();
                }
            });
        },
        // 获取信息
        getInfo () {
            this.$http.get(`/act/demo/correction/${this.dataForm.id}`).then(({ data: res }) => {
                if (res.code !== 0) {
                    return this.$message.error(res.msg);
                }
                this.dataForm = {
                    ...this.dataForm,
                    ...res.data
                };
            }).catch(error => console.error(error));
        },
        // 启动流程出错回调
        startProcessErrorCallback (data) {
            console.log(data);
        },
        // 任务处理出错回调
        taskHandleErrorCallback (data) {
        }
    }
};
</script>

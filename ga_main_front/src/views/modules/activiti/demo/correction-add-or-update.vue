<template>
  <el-dialog 
    :visible.sync="visible" 
    :title="!dataForm.id ? $t('add') : $t('update')" 
    :close-on-click-modal="false" 
    :close-on-press-escape="false"
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
          :placeholder="$t('correction.achievement')" 
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <template slot="footer">
      <el-button @click="visible = false">
        {{ $t('cancel') }}
      </el-button>
      <!--  流程启动组件  -->
      <ren-process-start 
        v-if="processVisible" 
        ref="renProcessStart" 
        update-instance-id-url="/act/demo/correction/updateInstanceId" 
        save-form-url="/act/demo/correction" 
        data-form-name="dataForm"
      />
    </template>
  </el-dialog>
</template>

<script>
// 引入工作流公共方法
import processModule from "@/mixins/process-module";
export default {
    // 注入公共方法
    mixins: [processModule],
    data () {
        return {
            // 是否显示流程启动组件
            processVisible: true,
            visible: false,
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
    methods: {
        init () {
            this.visible = true;
            this.$nextTick(() => {
                this.$refs["dataForm"].resetFields();
                if (this.dataForm.id) {
                    this.getInfo();
                }
                // 将业务组件对象赋值给流程（回调时需要用到）
                this.$refs.renProcessStart.rootObj = this;
                // 配置回调函数
                this.$refs.renProcessStart.callbacks = {
                    startProcessSuccessCallback: this.closeCurrentDialog,
                    startProcessErrorCallback: this.startProcessErrorCallback,
                    formSaveSuccessCallback: null,
                    formSaveErrorCallback: null
                };
                // 配置流程定义KEY
                this.$refs.renProcessStart.dataForm.processDefinitionKey = "correctionprocess";
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
        closeCurrentDialog () {
            this.visible = false;
            this.$emit("refreshDataList");
        }
    }
};
</script>

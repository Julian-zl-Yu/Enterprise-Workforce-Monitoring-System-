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
      label-width="120px" 
      @keyup.enter.native="dataFormSubmitHandle()"
    >
      <el-form-item 
        :label="$t('sms.smsCode')" 
        prop="smsCode"
      >
        <el-input 
          v-model="dataForm.smsCode" 
          :placeholder="$t('sms.smsCode')"
        />
      </el-form-item>
      <el-form-item 
        :label="$t('sms.remark')" 
        prop="remark"
      >
        <el-input 
          v-model="dataForm.remark" 
          :placeholder="$t('sms.remark')"
        />
      </el-form-item>
      <el-divider />
      <el-form-item 
        :label="$t('sms.platform')" 
        size="mini"
      >
        <el-radio-group v-model="dataForm.platform">
          <el-radio :label="1">
            {{ $t('sms.platform1') }}
          </el-radio>
          <el-radio :label="2">
            {{ $t('sms.platform2') }}
          </el-radio>
          <el-radio :label="3">
            {{ $t('sms.platform3') }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <template v-if="dataForm.platform === 1">
        <el-form-item 
          :label="$t('sms.aliyunAccessKeyId')" 
          prop="config.aliyunAccessKeyId"
        >
          <el-input 
            v-model="dataForm.config.aliyunAccessKeyId" 
            :placeholder="$t('sms.aliyunAccessKeyIdTips')"
          />
        </el-form-item>
        <el-form-item 
          :label="$t('sms.aliyunAccessKeySecret')" 
          prop="config.aliyunAccessKeySecret"
        >
          <el-input 
            v-model="dataForm.config.aliyunAccessKeySecret" 
            :placeholder="$t('sms.aliyunAccessKeySecretTips')"
          />
        </el-form-item>
        <el-form-item 
          :label="$t('sms.aliyunSignName')" 
          prop="config.aliyunSignName"
        >
          <el-input 
            v-model="dataForm.config.aliyunSignName" 
            :placeholder="$t('sms.aliyunSignName')"
          />
        </el-form-item>
        <el-form-item 
          :label="$t('sms.aliyunTemplateCode')" 
          prop="config.aliyunTemplateCode"
        >
          <el-input 
            v-model="dataForm.config.aliyunTemplateCode" 
            :placeholder="$t('sms.aliyunTemplateCodeTips')"
          />
        </el-form-item>
      </template>
      <template v-else-if="dataForm.platform === 2">
        <el-form-item 
          :label="$t('sms.qcloudAppId')" 
          prop="config.qcloudAppId"
        >
          <el-input 
            v-model="dataForm.config.qcloudAppId" 
            :placeholder="$t('sms.qcloudAppIdTips')"
          />
        </el-form-item>
        <el-form-item 
          :label="$t('sms.qcloudAppKey')" 
          prop="config.qcloudAppKey"
        >
          <el-input 
            v-model="dataForm.config.qcloudAppKey" 
            :placeholder="$t('sms.qcloudAppKeyTips')"
          />
        </el-form-item>
        <el-form-item 
          :label="$t('sms.qcloudSignName')" 
          prop="config.qcloudSignName"
        >
          <el-input 
            v-model="dataForm.config.qcloudSignName" 
            :placeholder="$t('sms.qcloudSignName')"
          />
        </el-form-item>
        <el-form-item 
          :label="$t('sms.qcloudTemplateId')" 
          prop="config.qcloudTemplateId"
        >
          <el-input 
            v-model="dataForm.config.qcloudTemplateId" 
            :placeholder="$t('sms.qcloudTemplateIdTips')"
          />
        </el-form-item>
      </template>
      <template v-else-if="dataForm.platform === 3">
        <el-form-item 
          :label="$t('sms.qiniuAccessKey')" 
          prop="config.qiniuAccessKey"
        >
          <el-input 
            v-model="dataForm.config.qiniuAccessKey" 
            :placeholder="$t('sms.qiniuAccessKeyTips')"
          />
        </el-form-item>
        <el-form-item 
          :label="$t('sms.qiniuSecretKey')" 
          prop="config.qiniuSecretKey"
        >
          <el-input 
            v-model="dataForm.config.qiniuSecretKey" 
            :placeholder="$t('sms.qiniuSecretKeyTips')"
          />
        </el-form-item>
        <el-form-item 
          :label="$t('sms.qiniuTemplateId')" 
          prop="config.qiniuTemplateId"
        >
          <el-input 
            v-model="dataForm.config.qiniuTemplateId" 
            :placeholder="$t('sms.qiniuTemplateIdTips')"
          />
        </el-form-item>
      </template>
    </el-form>
    <template slot="footer">
      <el-button @click="visible = false">
        {{ $t('cancel') }}
      </el-button>
      <el-button 
        type="primary" 
        @click="dataFormSubmitHandle()"
      >
        {{ $t('confirm') }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import debounce from "lodash/debounce";
export default {
    data () {
        return {
            visible: false,
            dataForm: {
                smsCode: "",
                remark: "",
                platform: 1,
                config: {
                    aliyunAccessKeyId: "",
                    aliyunAccessKeySecret: "",
                    aliyunSignName: "",
                    aliyunTemplateCode: "",
                    qcloudAppId: "",
                    qcloudAppKey: "",
                    qcloudSignName: "",
                    qcloudTemplateId: "",
                    qiniuAccessKey: "",
                    qiniuSecretKey: "",
                    qiniuTemplateId: ""
                }
            }
        };
    },
    computed: {
        dataRule () {
            return {
                smsCode: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                "config.aliyunAccessKeyId": [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                "config.aliyunAccessKeySecret": [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                "config.aliyunSignName": [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                "config.aliyunTemplateCode": [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                "config.qcloudAppId": [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                "config.qcloudAppKey": [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                "config.qcloudSignName": [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                "config.qcloudTemplateId": [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                "config.qiniuAccessKey": [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                "config.qiniuSecretKey": [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                "config.qiniuTemplateId": [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ]
            };
        }
    },
    watch: {
        "dataForm.platform" (val) {
            this.$refs["dataForm"].clearValidate();
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
            });
        },
        // 获取信息
        getInfo () {
            this.$http.get(`/sys/sms/${this.dataForm.id}`).then(({ data: res }) => {
                if (res.code !== 0) {
                    return this.$message.error(res.msg);
                }
                this.dataForm = {
                    ...this.dataForm,
                    ...res.data
                };
            }).catch(error => console.error(error));
        },
        // 表单提交
        dataFormSubmitHandle: debounce(function () {
            this.$refs["dataForm"].validate((valid) => {
                if (!valid) {
                    return false;
                }
                this.$http[!this.dataForm.id ? "post" : "put"]("/sys/sms", this.dataForm).then(({ data: res }) => {
                    if (res.code !== 0) {
                        return this.$message.error(res.msg);
                    }
                    this.$message({
                        message: this.$t("prompt.success"),
                        type: "success",
                        duration: 500,
                        onClose: () => {
                            this.visible = false;
                            this.$emit("refreshDataList");
                        }
                    });
                }).catch(error => console.error(error));
            });
        }, 1000, { "leading": true, "trailing": false })
    }
};
</script>

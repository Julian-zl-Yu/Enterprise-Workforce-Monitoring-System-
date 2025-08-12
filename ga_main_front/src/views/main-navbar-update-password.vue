<template>
  <el-dialog
    :visible.sync="visible"
    :title="$t('updatePassword.title')"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :append-to-body="true"
  >
    <el-form 
      ref="dataForm" 
      :model="dataForm" 
      :rules="dataRule" 
      label-width="120px" 
      @keyup.enter.native="dataFormSubmitHandle()"
    >
      <el-form-item :label="$t('updatePassword.username')">
        <span>{{ $store.state.user.name }}</span>
      </el-form-item>
      <el-form-item 
        :label="$t('updatePassword.password')" 
        prop="password"
      >
        <el-input 
          v-model="dataForm.password" 
          :placeholder="$t('updatePassword.password')" 
          type="password"
        />
      </el-form-item>
      <el-form-item 
        :label="$t('updatePassword.newPassword')" 
        prop="newPassword"
      >
        <el-input 
          v-model="dataForm.newPassword" 
          :placeholder="$t('updatePassword.newPassword')" 
          type="password"
        />
      </el-form-item>
      <el-form-item 
        :label="$t('updatePassword.confirmPassword')" 
        prop="confirmPassword"
      >
        <el-input 
          v-model="dataForm.confirmPassword" 
          :placeholder="$t('updatePassword.confirmPassword')" 
          type="password"
        />
      </el-form-item>
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
import { clearLoginInfo } from "@/utils/index.ts";
export default {
    data () {
        return {
            visible: false,
            dataForm: {
                password: "",
                newPassword: "",
                confirmPassword: ""
            }
        };
    },
    computed: {
        dataRule () {
            var validateConfirmPassword = (rule, value, callback) => {
                if (this.dataForm.newPassword !== value) {
                    return callback(new Error(this.$t("updatePassword.validate.confirmPassword")));
                }
                callback();
            };
            return {
                password: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                newPassword: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                confirmPassword: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" },
                    { validator: validateConfirmPassword, trigger: "blur" }
                ]
            };
        }
    },
    methods: {
        init () {
            this.visible = true;
            this.$nextTick(() => {
                this.$refs["dataForm"].resetFields();
            });
        },
        // 表单提交
        dataFormSubmitHandle: debounce(function () {
            this.$refs["dataForm"].validate((valid) => {
                if (!valid) {
                    return false;
                }
                this.$http.put("/sys/user/password", this.dataForm).then(({ data: res }) => {
                    if (res.code !== 0) {
                        return this.$message.error(res.msg);
                    }
                    this.$message({
                        message: this.$t("prompt.success"),
                        type: "success",
                        duration: 500,
                        onClose: () => {
                            this.visible = false;
                            clearLoginInfo();
                            this.$router.replace({ name: "login" });
                        }
                    });
                }).catch(error => console.error(error));
            });
        }, 1000, { "leading": true, "trailing": false })
    }
};
</script>

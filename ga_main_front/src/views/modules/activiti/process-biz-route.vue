<template>
  <el-dialog 
    :visible.sync="visible" 
    :title="$t('process.bizRouteSet')" 
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
        :label="$t('process.bizRoute')" 
        prop="bizRoute"
      >
        <el-input 
          v-model="dataForm.bizRoute" 
          :placeholder="$t('process.bizRoute')"
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
export default {
    data () {
        return {
            visible: false,
            dataForm: {
                id: "",
                bizRoute: "",
                procDefId: "",
                procDefKey: "",
                version: null
            }
        };
    },
    computed: {
        dataRule () {
            return {
                bizRoute: [
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
                this.dataForm.id = null;
                if (this.dataForm.procDefId) {
                    this.getInfo();
                }
            });
        },
        // 获取信息
        getInfo () {
            this.$http.get(`/act/process/getProcDefBizRoute/${this.dataForm.procDefId}`).then(({ data: res }) => {
                if (res.code !== 0) {
                    return this.$message.error(res.msg);
                }
                this.dataForm = {
                    ...this.dataForm,
                    ...res.data
                };
                console.log(this.dataForm);
            }).catch(error => console.error(error));
        },
        // 表单提交
        dataFormSubmitHandle: debounce(function () {
            this.$refs["dataForm"].validate((valid) => {
                if (!valid) {
                    return false;
                }
                this.$http[!this.dataForm.id ? "post" : "put"]("/act/process/saveProcBizRoute", this.dataForm).then(({ data: res }) => {
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

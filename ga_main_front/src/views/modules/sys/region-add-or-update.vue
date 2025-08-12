<template>
  <el-dialog 
    :visible.sync="visible" 
    :title="insert ? $t('add') : $t('update')" 
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
        prop="parentName" 
        label="上级区域"
      >
        <ren-region-tree 
          v-model="dataForm.pid" 
          :parent-name.sync="dataForm.parentName" 
          placeholder="选择区域"
        />
      </el-form-item>
      <el-form-item 
        :label="$t('region.name')" 
        prop="name"
      >
        <el-input 
          v-model="dataForm.name" 
          :placeholder="$t('region.name')"
        />
      </el-form-item>
      <el-form-item 
        :label="$t('region.id')" 
        prop="id"
      >
        <el-input 
          v-model="dataForm.id" 
          :disabled="!insert" 
          :placeholder="$t('region.id')"
        />
      </el-form-item>
      <el-form-item 
        :label="$t('region.sort')" 
        prop="sort"
      >
        <el-input-number 
          v-model="dataForm.sort" 
          :min="0" 
          :label="$t('region.sort')" 
          controls-position="right"
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
            insert: true,
            regionList: [],
            regionListVisible: false,
            dataForm: {
                id: "",
                name: "",
                pid: "0",
                parentName: "",
                sort: 0
            }
        };
    },
    computed: {
        dataRule () {
            return {
                id: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                name: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ],
                sort: [
                    { required: true, message: this.$t("validate.required"), trigger: "blur" }
                ]
            };
        }
    },
    methods: {
        init (id) {
            this.insert = true;
            this.visible = true;
            this.dataForm.pid = "0";
            this.$nextTick(() => {
                this.$refs["dataForm"].resetFields();
                this.dataForm.id = id;
                if (this.dataForm.id) {
                    this.insert = false;
                    this.getInfo();
                }
            });
        },
        // 获取信息
        getInfo () {
            this.$http.get(`/sys/region/${this.dataForm.id}`).then(({ data: res }) => {
                if (res.code !== 0) {
                    return this.$message.error(res.msg);
                }
                this.dataForm = {
                    ...this.dataForm,
                    ...res.data
                };
                this.$refs.regionListTree.setCurrentKey(this.dataForm.pid);
            }).catch(error => console.error(error));
        },
        // 表单提交
        dataFormSubmitHandle: debounce(function () {
            this.$refs["dataForm"].validate((valid) => {
                if (!valid) {
                    return false;
                }
                this.$http[this.insert ? "post" : "put"]("/sys/region", this.dataForm).then(({ data: res }) => {
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

<style lang="scss">
.mod-sys__region {
  .region-list {
    .el-input__inner,
    .el-input__suffix {
      cursor: pointer;
    }
  }
}
</style>

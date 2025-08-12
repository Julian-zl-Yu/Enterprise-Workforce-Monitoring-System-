<template>
  <div
    v-loading="isLoading"
    :class="['el-upload-dragger uploader-wrapper', { 'is-dragover': dragover }]"
    @drop.prevent="onDrop"
    @dragover.prevent="onDragover"
    @dragleave.prevent="dragover = false"
  >
    <input
      ref="input"
      class="uploader-input"
      type="file"
      :name="name"
      :multiple="multiple"
      accept="accept"
      @change="handleInputChange"
    />
    <i class="el-icon-upload" />
    <div class="el-upload__text">
      <div>将文件拖到此处</div>
      <div>或</div>
      <el-button type="primary" @click.prevent.stop="handleClick">
        选择文件
      </el-button>
    </div>
  </div>
</template>
<script>
import { API_URL } from "@/main.config";
import forEach from "lodash/forEach";
import merge from "lodash/merge";
import { getViewTypeBy, SuccessOrFailed } from "@/utils/index.ts";
import isFunction from "lodash/isFunction";

export default {
  name: "Uploader",
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    configs: {
      required: false,
      type: Object,
      default() {
        return { };
      },
    },
    multiple:  {
      type: Boolean,
      default() {
        return true;
      },
    },
    value: {
      type: Boolean,
      default: false,
    },
    /* 用于判断该上传的业务模块 */
    fileType: {
      type: String,
      required: true,
    },
    disabled: Boolean,
    name: {
      type: String,
      default() {
        return "file";
      },
    },
  },
  data() {
    return {
      dragover: false,
      requestCount: 0,
    };
  },
  computed: {
    isLoading() {
      /*TODO:progress进度条*/
      return this.requestCount !== 0;
    },
  },
  methods: {
    handleClick() {
      this.$refs.input.value = null;
      this.$refs.input.click();
    },
    async deleteById(idArray = []) {
      this.$emit("change", true);
      try {
        const res = await this.$http.delete(API_URL.delete, { data: idArray });
        console.log(idArray);
        return SuccessOrFailed(
          res,
          { msg: "删除成功", fn: () => this.$emit("delete-success", idArray) },
          { msg: res.msg }
        );
      } catch (error) {
        console.error(error);
      } finally {
        this.$emit("change", false);
      }
    },
    uploadFiles(files) {
      forEach(files, async (file) => {
        if (isFunction(this.configs.check)) {
          const isPass = this.configs.check(file);
          if (!isPass) return;
        }
        this.requestCount++;
        let formData = new FormData();
        const viewType = getViewTypeBy(file.type);

        formData.append("file", file);
        formData.append("viewType", viewType);
        formData.append("fileType", this.fileType);

        try {
          const res = await this.$http.post(API_URL.upload, formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          });

          if (res?.data?.code !== 0) {
            throw Error(res?.data?.msg);
          } else {
            /* 需要返回:ViewItem */
            let targetFile = merge(
              file,
              {
                originalName: file.name,
              },
              {
                viewType,
                id: res?.data?.data,
                ot0101: res?.data?.data,
                src: (function (item) {
                  let src = item.url || item.src;
                  try {
                    if (!src) {
                      src = window.URL.createObjectURL(file);
                    }
                  } catch (error) {
                    console.error(error);
                  }
                  return src;
                })(res?.data || {}),
              },
              res?.data
            );
            this.handleUploadSuccess(targetFile);
          }
        } catch (error) {
          console.error(error);
        } finally {
          this.requestCount--;
        }
      });
    },
    handleUploadSuccess(file) {
      this.$emit("upload-success", file);
    },
    /*通过点击按钮选择文件上传*/
    handleInputChange(ev) {
      const files = ev.target.files;
      if (!files) return;

      this.uploadFiles(files);
    },
    onDragover() {
      if (!this.disabled) {
        this.dragover = true;
      }
    },
    /*通过拖拽直接上传*/
    onDrop(e) {
      this.dragover = false;
      const files = e.dataTransfer.files;
      if (!files) return;
      this.uploadFiles(files);
    },
  },
};
</script>
<style lang="scss" scoped>
.el-upload-dragger.uploader-wrapper { 
  margin: 12px;
  width: 128px;
  display: inline-block;
  position: relative;
  display: flex;
  flex-flow: column nowrap;
  justify-content: center;
  align-items: center;

  &.el-upload-dragger {
    cursor: unset;
  }

  .el-icon-upload {
    margin: 0 0 16px;
  }

  .el-upload__text {
    > div {
      margin: 2px 0;
    }
  }

  .uploader-input {
    visibility: hidden;
    position: absolute;
    top: 0;
    right: 0;
    width: 0;
    height: 0;
    z-index: -1;
  }
}
</style>
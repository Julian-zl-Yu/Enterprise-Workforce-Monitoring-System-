<template>
  <div class="viewer">
    <div class="option-btn-wrapper">
      <BtnGroup
        v-if="shiro"
        :configs="configsBtns"
        :can-delete="dataListSelections"
      />
    </div>

    <el-alert
      v-show="isShowAlert"
      title="文件要求"
      type="info"
      :description="configs.tips"
      show-icon
    >
    </el-alert>

    <!--  -->
    <ViewerWrapper v-loading="isLoading" :viewer-items="viewerItems">
      <Uploader
        v-show="isShowUploader"
        ref="uploader"
        v-model="isLoading"
        :file-type="fileType"
        :configs="configs"
        :multiple="configs.multiple"
        @upload-success="handleUploadSuccess"
        @delete-success="handleDeleteSuccess"
      />

      <template>
        <ViewerItem
          v-for="(item, index) in viewerItems"
          :key="item.ot0101"
          :data-viewer-id="item.ot0101"
          :item="item"
          :index="index"
          :shiro="shiro"
          @request-delete="deleteByItem"
          @delete-change="handleViewerItemDeleteStateChange"
        />
      </template>
      <NoData
        v-if="viewerItems.length === 0"
        style="width: 100%; height: 64px"
      />
    </ViewerWrapper>
  </div>
</template>
<script>
import isBoolean from "lodash/isBoolean";
import { BTN_GROUP_TYPE, BTN_TYPE, STATUS_TYPE } from "@/components/Types";
import isArray from "lodash/isArray";

export default {
  name: "Viewer",
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    value: {
      required: true,
      type: Array,
      default() {
        return [];
      },
    },
    configs: {
      required: true,
      type: Object,
    },
  },
  data() {
    const _this = this;
    return {
      description: "",
      isLoading: false,
      dataListSelections: [],
      configsBtns: {
        btnType: BTN_GROUP_TYPE.Btn,
        btnList: [
          {
            type: BTN_TYPE.delete,
            click: () => _this.deleteByItem(_this.dataListSelections),
          },
        ],
      },
    };
  },
  computed: {
    isShowAlert() {
      return !!this.configs.tips;
    },
    viewerItems: {
      get() {
        return this.value;
      },
      set(_viewerItems) {
        this.$emit("change", _viewerItems);
      },
    },
    fileType() {
      const fileType = this.configs?.fileType;
      if (!fileType) {
        const message = "未提供上传文件必要的fileType";
        this.$notify({
          title: "错误提示",
          message,
          type: STATUS_TYPE.error,
        });

        console.error(message);
      }
      return fileType;
    },
    shiro() {
      if (this.isReadOnly) {
        return false;
      }
      const _shiro = this?.configs?.shiro;
      const typeOfShiro = typeof _shiro;
      /*TODO:*/
      /*如果设置了shiro，以shiro权限为准，否则直接显示*/
      return "string" === typeOfShiro ? this.$hasPermission(_shiro) : true;
    },
    isShowUploader() {
      return isBoolean(this.configs?.uploader) ? this.configs?.uploader : true;
    },
    isReadOnly() {
      return isBoolean(this.configs?.readOnly) ? this.configs?.readOnly : false;
    },
  },
  mounted() {},
  methods: {
    handleViewerItemDeleteStateChange(isChecked, item) {
      const dataListSelections = new Set(this.dataListSelections);
      dataListSelections[isChecked ? "add" : "delete"](item.ot0101);
      this.dataListSelections = Array.from(dataListSelections);
    },
    deleteByItem(item) {
      console.log("item", item);
      this.$refs.uploader.deleteById(isArray(item) ? item : [item.ot0101]);
    },
    handleDeleteSuccess(idArray) {
      /*TODO:*/
      const _viewerItems = this.viewerItems.filter(
        (item) => !~idArray.indexOf(item.ot0101)
      );
      this.viewerItems = _viewerItems;
    },
    handleUploadSuccess(file) {
      /*TODO:*/
      this.viewerItems.splice(0, 0, file);
    },
  },
};
</script>
<style lang="scss" scoped>
.optional {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>
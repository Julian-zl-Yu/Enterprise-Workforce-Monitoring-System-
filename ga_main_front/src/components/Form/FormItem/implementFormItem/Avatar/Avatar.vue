<template>
  <div v-loading="isLoading" class="avatar-default-img" @click="getStream">
    <!--<div
      v-if="isShowTips"
      :id="'show-tips_' + _uid"
      class="require-tips"
      @mouseenter="handleShowTips"
      @mouseleave="handleCloseTips"
    />-->
    <!-- <div class="avatar-tips-image" /> -->
    <div v-if="isShowTips" class="require-tips">
      <el-popover v-if="canEdit" placement="right" trigger="hover">
        <div class="avatar-tips-image" />
        <ul slot="reference" class="tips">
          <!-- <li>图片中只有一个人脸</li>
          <li>图片中人脸端正</li>
          <li>图片中人脸占图片30%以上</li> -->
        </ul>
      </el-popover>
    </div>
    <div v-else ref="image" class="avatar-camera image" :style="styleAvatar" />
  </div>
</template>

<script>
import {
  model as baseModel,
  subCreated,
  subComputed,
  subProps,
} from "@/components/Form/FormItem/mixinsFormItemBase";
import $ from "jquery";
import AvatarCamera from "./AvatarCamera";
import { $id } from "@/utils/iQuery";
import emitter from "element-ui/src/mixins/emitter";
import { checkImgExists } from "@/utils/index";

export default {
  name: "ImplInput",
  mixins: [subCreated, baseModel, subProps, subComputed, emitter],
  data() {
    return {
      isLoading: false,
      isOpenCamera: false,
      theStream: false,
      theImageCapturer: false,
      imgBase64: "",
      isEffective: false,
    };
  },
  computed: {
    isShowTips() {
      return !(this.imgBase64 || this.value);
    },
    styleAvatar() {
      const url = this.imgBase64 || this.value;
      checkImgExists(url)
        .then(() => {
          this.isEffective = true;
        })
        .catch(() => {
          this.isEffective = false;
        });
      // return this.isEffective
      //   ? { backgroundImage: `url(${url})` }
      //   : { backgroundImage: "url(" + require("./avatar.png") + ")" };
        return url
        ? { backgroundImage: `url(${url})` }
        : { backgroundImage: "" };
    },
  },
  mounted() {
    this.$watch(
      "value",
      (value) => {
        this.imgBase64 = value;
      },
      { immediate: true }
    );
    const $ele = $(this.$el);
    /*如果不是table布局 父元素没有td 是不起作用的*/
    /*2020年10月26日更新grid布局：td作废*/
    const $eleTd = $ele.parents(".customize-col");
    if ($eleTd.length === 1) {
      $eleTd.addClass("form-item-avatar-container");
      $ele.addClass("form-item-avatar-wrapper");
    }
  },
  methods: {
    handleCloseTips() {
      // if (!this.canEdit) return;
      if (this.handleShowTips.id) {
        this.$layer.close(this.handleShowTips.id);
      }
    },
    handleShowTips() {
      // if (!this.canEdit) return;
      this.handleShowTips.id = this.$layer.tips(
        '<div class="avatar-tips-image"></div>',
        `#show-tips_${this._uid}`,
        {
          tips: 1,
          time: 1000,
          noClass: true,
        }
      );
    },
    handleEnter(e) {
      if ("Enter" === e.code) {
        this.camera.takePhoto();
      }
    },
    removeZIndex() {
      //关闭事件
      const rootElement = $id("root-element");
      if (rootElement && rootElement.style) {
        /*open dialog*/
        rootElement.style.zIndex = "unset";
      } else {
        /* FIXED: 修复modal 的index 覆盖问题 */
        console.log(" 当前页面没有id为root-element的根元素");
      }
      $(document).off("keypress.avatar");
    },
    addZIndex() {
      const rootElement = $id("root-element");
      if (rootElement && rootElement.style) {
        /*open dialog*/
        rootElement.style.zIndex = "-1";
      } else {
        /* FIXED: 修复modal 的index 覆盖问题 */
        console.log(" 当前页面没有id为root-element的根元素");
      }
      $(document).on("keypress.avatar", this.handleEnter);
    },
    async getStream() {
      /* readOnly 不可编辑 */
      if (!this.canEdit) return;
      this.isOpenCamera = true;
      this.isLoading = true;
      this.$layer.iframe({
        content: {
          content: AvatarCamera, //传递的组件对象
          parent: this, //当前的vue对象
          data: {}, //props
        },
        area: ["660px", "540px"],
        title: "点击拍照按钮或者回车键拍照",
        shadeClose: false,
        success: () => {
          this.addZIndex();
        },
        cancel: () => {
          const isParentCall = true;
          this?.camera?.close(isParentCall);
          this.removeZIndex();
        },
      });
    },
    init() {},
    handleValueChange(val) {
      this.$emit("change", val);
      /*触发数据校验*/
      this.dispatch("ElFormItem", "el.form.change", [val]);
    },
  },
};
</script>

<style lang="scss" scoped>
.avatar-default-img {
  .avatar-oprations {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 2;
  }
  .require-tips {
    height: 100%;
    width: 100%;
    overflow: hidden;
    background: url(./avatar.png) center center/contain no-repeat;
    .tips {
      &:hover {
        cursor: pointer;
      }
      height: 100%;
      width: 100%;
      padding: 0 20px;
      margin: 0;
    }
  }
}

.avatar-camera {
  z-index: 1;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  transform: scaleX(-1);
  &.image {
    background: url(./avatar.png) center center/contain no-repeat;
  }
}
</style>

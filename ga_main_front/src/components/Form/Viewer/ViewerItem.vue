<template>
  <div :class="['el-card viewer-item is-always-shadow', { delete: isChecked }]">
    <div class="el-card__body">
      <div
        class="viewer-item_img"
        :style="imgStyle"
      >
        <div class="actions-wrapper">
          <el-checkbox
            v-if="shiro"
            v-model="isChecked"
            class="delete-checkbox"
            @change="$emit('delete-change', $event, viewItem)"
          />
          <span
            class="btn-action preview coursor"
            @click="preview"
          >
            <i class="el-icon-zoom-in" />
          </span>

          <Btnmini
            v-if="shiro"
            :configs="configsBtnDelete"
          >
            <span class="btn-action delete coursor">
              <i class="el-icon-delete" />
            </span>
          </Btnmini>
        </div>
        <div class="file-name">
          {{ item.originalName }}
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { BTN_TYPE, FILE_TYPE } from "@/components/Types";
import emitter from "element-ui/src/mixins/emitter";
import { makeViewerItem, ViewerItem } from "@/components/Form/Viewer/index";

/* https://developer.mozilla.org/zh-CN/docs/Web/API/Intersection_Observer_API */

const componentName = "ViewerItem";
export default {
  componentName,
  name: componentName,
  mixins: [emitter],
  inject: {
    ViewerWrapper: {
      default() {
        return { items: {} };
      },
    },
  },
  props: {
    item: { required: true },
    index: { required: true },
    shiro: {
      default() {
        return false;
      },
    },
  },
  data() {
    const _this = this;
    return {
      isChecked: false,
      viewItem: {},
      FILE_TYPE,
      viewType: "",
      imgSrc: "",
      configsBtnDelete: {
        type: BTN_TYPE.delete,
        shiro: this.shiro,
        /* 删除不是ViewerItem的职责：ViewerItem只负责展示、分发请求 */
        click: () => _this.$emit("request-delete", _this.viewItem),
      },
    };
  },
  computed: {
    imgStyle() {
      const STYLE_MAP = {
        [FILE_TYPE.img]: { "background-image": `url(${this.imgSrc})` },
      };
      const imgStyle = STYLE_MAP[FILE_TYPE[this.viewItem.viewType]] || {};
      return imgStyle;
    },
  },
  mounted() {
    this.initViewer();
    /* 预先将 ViewerItem 的每个实例缓存在 ViewerWrapper 中 */
    this.dispatch("ViewerWrapper", "ViewerWrapper.add", [this]);
  },
  beforeDestroy() {
    /* 组件销毁前，将实例从 ViewerWrapper 的缓存中移除 */
    this.dispatch("ViewerWrapper", "ViewerWrapper.remove", [this]);
  },
  methods: {
    preview() {
      const items = makeViewerItem(this.ViewerWrapper.viewerItems);
      this.$viewer(items, this.index);
    },
    initViewer() {
      /*TODO: check imgSrc*/
      this.viewItem = new ViewerItem(this.item);
      this.viewType = FILE_TYPE[this.viewItem.viewType];
      this.imgSrc = `${this.viewItem.imgSrc}`;
    },
  },
};
</script>

<style lang="scss" scoped>
.el-card.viewer-item.is-always-shadow {
  overflow: hidden;
  display: inline-block;
  width: 128px;
  height: 180px;
  margin: 12px;
  &:hover {
    border-color: $--color-primary;
  }

  &.delete {
    border-color: $--color-danger;
  }

  .el-card__body {
    padding: 0;
    height: 100%;
    width: 100%;
  }

  .viewer-item_img {
    height: 100%;
    width: 100%;
    background: center center/cover no-repeat;
    display: flex;
    flex-flow: column nowrap;
    justify-content: flex-end;
    align-items: center;

    div.file-name {
      margin: 6px;
      font-size: $font-size-b;
      line-height: 18px;
      background: white;
      width: 100%;
      max-height: 999px;

      text-align: center;
    }

    .actions-wrapper {
      display: none;
      position: relative;

      .delete-checkbox {
        position: absolute;
        top: 6px;
        left: 6px;
      }

      .btn-action {
        transform: scale(0.1);
      }
    }

    &:hover {
      justify-content: center;

      div.file-name {
        max-height: 0;
        margin: 0;
        background-color: white;
      }

      .actions-wrapper {
        background-color: rgba(0, 0, 0, 0.3);
        height: 100%;
        width: 100%;
        display: flex;
        flex-flow: row nowrap;
        justify-content: space-around;
        align-items: center;

        .btn-action {
          color: white;
          font-size: $font-size-m;
          text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.5);
          display: inline-block;
          margin: 16px;
          transform: scale(1);

          &:hover {
            transform: scale(1.2);
            transform-origin: center center;
            cursor: pointer;
          }
        }
      }
    }
    /*&:hover */
  }
}
</style>
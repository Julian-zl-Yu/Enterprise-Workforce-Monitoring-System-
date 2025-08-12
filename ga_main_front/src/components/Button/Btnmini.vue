<template>
  <span
    v-if="shiro&&checkShowByScope(shiro)"
    :class="['order-btn mini',{'btn-disabled':attrIsDisabled}]"
  >
    <el-popover
      v-if="configs.type===BTN_TYPE.delete"
      v-model="visible"
      trigger="manual"
      placement="top"
      width="160"
    >
      <p>是否确认此操作？</p>
      <div style="text-align: right; margin: 0">
        <el-button
          size="mini"
          type="text"
          :class="typeClass"
          @click="visible = false"
        >取消</el-button>
        <el-button
          size="mini"
          type="primary"
          @click="ensureDoDelete"
        >确定</el-button>
      </div>
      <el-button
        slot="reference"
        type="text"
        :class="['order-btn mini',typeClass]"
        size="small"
        @click="clickBtn(true)"
      >
        <slot>{{ text }}</slot>
      </el-button>
    </el-popover>
    <el-button
      v-else
      v-bind="$attrs"
      type="text"
      size="small"
      :class="['order-btn mini',typeClass]"
      @click="clickBtn(false,scope)"
    >
      <slot>{{ text }}</slot>
    </el-button>
  </span>
</template>
<script>
import { BtnMixins } from "./Btn.mixins";

export default {
  name: "Btnmini",
  mixins: [BtnMixins],
  data() {
    return {
      visible: false,
    };
  },
  methods: {
    clickBtn(del, scope) {
      /* 如果禁用，点击无效 */
      if (this.attrIsDisabled) return;
      /* 如果是删除按钮，弹出popver */
      if (del) {
        this.visible = true;
      } else {
        this.configs.click(scope);
      }
    },
    ensureDoDelete() {
      this.visible = false;
      this.configs.click(this.scope);
    },
  },
};
</script>
<style lang="scss">
.btn-span-wrapper {
  display: inline-block;
}
.order-btn {
  // margin: 0 6px;
  & + .order-btn {
    margin-left: 12px;
  }
  &.mini {
    &.btn-disabled {
      position: relative;
      opacity: 0.5;
      &::before {
          content: " ";
          display: block;
          position: absolute;
          top: -10px;
          bottom: -10px;
          right: -2px;
          left: -2px;
          z-index: 1;
          border-radius: 4px;
      }
      &:hover {
        cursor: not-allowed;
      }
    }
    & + .order-btn {
      margin-left: 4px;
    }
  }
}
</style>
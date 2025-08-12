<template>
  <span
    v-if="shiro&&checkShowByScope(shiro)"
    class="order-btn"
  >
    <el-button
      v-if="configs.debounceClick"
      :loading="isLoading"
      type="primary"
      :class="typeClass"
      @click="debounceClick"
    >
      <slot
        v-if="isLoading"
        name="loading"
      >{{ loadingText }}</slot>
      <slot
        v-else
        name="normal"
      >{{ text }}</slot>
    </el-button>
    <el-button
      v-else-if="configs.type===BTN_TYPE.delete"
      v-tips
      :data-tips="deleteBtnTips"
      v-bind="$attrs"
      type="danger"
      :class="typeClass"
      :disabled="configs.disabled||isDisableDelete"
      @click="ensureDoDelete"
    >
      <slot>{{ text }}</slot>
    </el-button>

    <el-button
      v-else
      v-bind="$attrs"
      :disabled="configs.disabled"
      @click="configs.click()"
    >
      <slot>{{ text }}</slot>
    </el-button>
  </span>
</template>
<script>
import { BtnMixins } from "./Btn.mixins";

export default {
  name: "Btn",
  mixins: [BtnMixins],
  computed: {
    deleteBtnTips() {
      if (this.configs.disabled || this.isDisableDelete) {
        return "请选择需要删除的记录";
      } else {
        return "";
      }
    },
    isDisableDelete() {
      const check = typeof this.canDelete;
      if (check !== "undefined") {
        return this.canDelete.length < 1;
      } else {
        return false;
      }
    },
  },
  methods: {
    async ensureDoDelete() {
      this.$confirm(this.configs.confirmMsg||"确定要执行删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "error",
      })
        .then(() => this.configs.click(this.scope))
        .catch(() => {});
    },
  },
};
</script>
<style lang="scss">
</style>
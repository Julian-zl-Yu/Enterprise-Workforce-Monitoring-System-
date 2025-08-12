<template>
  <div
    v-if="!isShow401"
    class="main-page"
  >
    <slot />
    <slot name="main" />
    <slot name="sub" />
  </div>
  <View401 v-else />
</template>
<script>
import isString from "lodash/isString";
import View401 from "@/views/pages/401";

export default {
  name: "MainPage",
  components: {
    View401,
  },
  props: {
    configs: {
      required: false,
      type: Object,
      default() {
        return {};
      },
    },
  },
  data() {
    return {};
  },
  computed: {
    isShow401() {
      return !this.auth;
    },
    /* 权限 */
    auth() {
      // return true;
      const shiro = this.configs?.shiro || false;
      if (!shiro) return true;
      if (isString(shiro)) {
        return this.$hasPermission(shiro);
      }
      if (_.isArray(shiro)) {
        return shiro.some((token) => this.$hasPermission(token));
      }
      return false;
    },
  },
};
</script>
<style lang="scss" scoped>
.main-page {
  display: flex;
  overflow: hidden;
  flex-flow: column nowrap;
}
</style>
<template>
  <div :is="component">
    {{ name }}
  </div>
</template>
<script lang="ts">
import { getNameMap } from "@/router/vue-router";
import Vue from "vue";
export default Vue.extend({
  name: "DynamicView",
  props: {
    name: {
      required: true,
      type: String,
    },
  },
  computed: {
    component() {
      const components = getNameMap()[this.name]?.components || {};
      return components.default ? components.default : "div";
    },
  },
});
</script>
<style lang="scss">
</style>
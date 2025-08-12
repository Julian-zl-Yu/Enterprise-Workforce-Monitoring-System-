<template>
  <el-link
    type="primary"
    :underline="false"
    @click="$emit('click',$event)"
  >
    {{ label }}
  </el-link>
</template>
<style>
</style>
<script>
export default {
  name:"CLink",
  props: {
    scope: {
      type: Object,
      default() {
        return null;
      },
    },
    type: {
      type: String,
      default() {
        return "";
      },
    },
    text: {
      type: String,
      default() {
        return "";
      },
    },
  },
  computed: {
    label() {
      return this.scope?.row[this.type] || this.text;
    },
  },
};
</script>;
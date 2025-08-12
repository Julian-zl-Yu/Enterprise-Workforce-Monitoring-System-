<template>
  <div class="pass-or-not">
    <el-tag
      v-if="String(value) === '0'"
      size="small"
    >
      待审核
    </el-tag>
    <el-tag
      v-if="String(value) === '1'"
      size="small"
      type="success"
    >
      通过
    </el-tag>
    <el-tag
      v-if="String(value) === '2'"
      size="small"
      type="danger"
    >
      不通过
    </el-tag>
  </div>
</template>
<script>
export default {
  name: "PassOrNot",
  props: {
    value: {
      type: [String,Number],
      required:true
    }
  },
  data() {
    return {};
  },
  mounted() {},
  methods: {
      initPassOrNot(){

      }
  }
};
</script>
<style lang="scss">
</style>
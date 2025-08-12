<template>
  <el-pagination
    class="pagination-bar"
    :current-page="info.page"
    :page-sizes="pageSizes"
    :page-size="info.limit"
    :total="info.total"
    :layout="layout"
    @size-change="pageSizeChangeHandle"
    @current-change="pageCurrentChangeHandle"
  />
</template>
<script>
export default {
  name: "Paginationbar",
  model: {
    prop: "info",
    event: "change",
  },
  props: {
    pageSizes: {
      type: Array,
      default() {
        return [10, 20, 50, 100];
      },
    },
    info: {
      required: true,
      type: [Object],
    },
  },
  data() {
    return {
      layout:this.info.layout || 'total, sizes, prev, pager, next, jumper'
    };
  },
  mounted() {},
  methods: {
    // 分页, 每页条数
    pageSizeChangeHandle(limit) {
      this.info.page = 1;
      this.info.limit = limit;

      this.$emit("query", this.info);
      this.$emit("change", this.info);
    },
    // 分页, 当前页
    pageCurrentChangeHandle(page) {
      this.info.page = page;
      this.$emit("query", this.info);
      this.$emit("change", this.info);
    },
  },
};
</script>
<style lang="scss" scoped>
</style>
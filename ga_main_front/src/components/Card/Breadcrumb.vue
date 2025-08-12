<template>
  <el-breadcrumb
    separator-class="el-icon-arrow-right"
    class="aui-card top-breadcrumb"
    style="margin-bottom:0;"
  >
    <el-breadcrumb-item
      v-for="(breadcrumb,index) in breadcrumbStack"
      :key="breadcrumb.areacode"
      :class="['cursor',{'current-breadcrumb':breadcrumbStack.length-1===index},'data-breadcrumb']"
      @click.native="handleClick(breadcrumb,index)"
    >
      {{ breadcrumb.label }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script>
export default {
  name: "CBreadcrumb",
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    value: {
      required: true,
      type: Array,
    },
  },
  computed: {
    breadcrumbStack: {
      set(val) {
        this.$emit("change", val);
      },
      get() {
        return this.value;
      },
    },
  },
  methods: {
    handleClick(breadcrumb, index) {
      this.$emit("click-item", { item: breadcrumb, index });
    },
  },
};
</script>

<style lang="scss">
.top-breadcrumb {
  .data-breadcrumb {
    &:hover,
    &.current-breadcrumb {
      > span {
        font-weight: 600;
      }
    }
    &:hover {
      cursor: pointer;
    }
  }
}
</style>
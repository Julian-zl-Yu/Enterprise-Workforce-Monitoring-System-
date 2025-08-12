<template>
  <div class="viewer-wrapper">
    <slot name="uploader" />
    <slot />
  </div>
</template>
<script>
/* ViewerItem需要全部的文件列表 所以需要有高一级的组件提供信息 */
const componentName = "ViewerWrapper";
export default {
  componentName,
  name: componentName,
  props:["viewerItems"],
  data() {
    return {
      items: []
    };
  },
  provide() {
    return {
      ViewerWrapper: this
    };
  },
  created() {
    /* Vue.js 的组件渲染顺序是由内而外的，
    所以 FormItem 要先于 Form 渲染，
    在 FormItem 的 mounted 触发时，
    向 Form 派发了事件 on-form-item-add，
    并将当前 FormItem 的实例（this）传递给了 Form，而此时，Form 的 mounted 尚未触发，
    因为 Form 在最外层，如果在 Form 的 mounted 里监听事件，是不可以的，所以要在其 created 内监听自定义事件，Form 的 created 要先于 FormItem 的 mounted。 */
    this.$on("ViewerWrapper.add", item => {
      if (item) {
        this.items.push(item);
      }
    });
    /* istanbul ignore next */
    this.$on("ViewerWrapper.remove", item => {
      if (item.src) {
        this.items.splice(this.items.indexOf(item), 1);
      }
    });
  },
  mounted() {},
  methods: {}
};
</script>
<style lang="scss" scoped>
.viewer-wrapper {
  display: flex;
  flex-flow: row wrap;
  justify-content: flex-start;
  align-items: flex-start;
  align-content: flex-start;
  margin: 0 -12px;
}
</style>
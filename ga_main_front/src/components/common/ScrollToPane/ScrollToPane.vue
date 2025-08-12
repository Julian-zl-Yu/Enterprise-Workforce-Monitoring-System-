<template>
  <Card
    v-if="contents.length > 1"
    class="scrollto-pane"
    header="索引"
    :data-hook="setParentClass()"
  >
    <div v-for="content in contents" :key="content.prop">
      <el-button class="btn" size="mini" round @click="scrollTo(content)">
        {{ content.title }}
      </el-button>
    </div>
  </Card>
</template>

<script>
import reduce from "lodash/reduce";
export default {
  name: "ScrollToPane",
  props: {
    configs: {
      required: true,
      type: Object,
    },
    wrapper: {
      required: true,
      type: [Object, String],
    },
  },
  computed: {
    contents() {
      return this.configs.contents || [];
    },
  },
  methods: {
    setParentClass() {
      this.configs.parent.classList.add("fixed-parent");
      return "done";
    },
    scrollTo({ prop }) {
      const dom = window.document.querySelector(`#${this.wrapper}`);
      if (!this.scrollTo.tops) {
        const tops = { total: 0 };
        this.scrollTo.tops = reduce(
          this.configs.contents,
          (_tops, { prop: contentProp }) => {
            const target = this.configs.refs[contentProp].$el;
            const rect = target.getBoundingClientRect();
            _tops[contentProp] = _tops.total;
            _tops.total += rect.height;
            return _tops;
          },
          tops
        );
      }

      dom.scrollTo({ top: this.scrollTo.tops[prop], behavior: "smooth" });
    },
  },
};
</script>

<style scoped lang="scss">
.scrollto-pane {
  z-index: 1;
  position: fixed;
  right: 0;
  top: 0;
  margin: 8px;
  max-height: 32px;
  max-width: 54px;
  overflow: hidden;
  background: white;
  @include elevation2();
  transition: all 0.3s ease-in-out;
  &:hover {
    cursor: pointer;
    max-width: 700px;
    max-height: 999px;
  }
  //   max-height:32px;
  .btn {
    margin: 8px 0;
  }
}
</style>
<style lang="scss">
.scrollto-pane {
  .el-card__header.card__header {
    padding: 8px;
  }
}
</style>
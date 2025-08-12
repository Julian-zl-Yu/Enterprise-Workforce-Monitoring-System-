<script lang="ts" >
import Vue from "vue";
import { VNode } from "vue";
export default {
  name: "Card",
  props: {
    header: {},
    bodyStyle: {},
    shadow: {
      type: String,
    },
  },
  render(createVNode) {
    let { shadow, $slots, $attrs, header, bodyStyle } = this;
    /* 基本函数 */
    /* this 指向必须是 vm实例 否则函数内无法调用实例属性 */
    const toString = this._s.bind(this);
    const renderSlot = this._t.bind(this);
    const createTextVNode = this._v.bind(this);
    const createEmptyVNode = this._e.bind(this);

    return createVNode(
      "div",
      {
        staticClass: "card",
        class: shadow ? "is-" + shadow + "-shadow" : "is-always-shadow",
      },
      (() => {
        const children: VNode[] = [];
        /* header */
        const headVNode = (() => {
          if ($slots.header || header) {
            return createVNode(
              "div",
              { staticClass: "el-card__header card__header" },
              (() => {
                if ($slots.header) return $slots.header;
                return [
                  renderSlot("header", [createTextVNode(toString(header))]),
                ];
              })(),
              2
            );
          }
          return false;
        })();

        if (headVNode) {
          children.push(headVNode as VNode);
        }

        /* body */
        let bodyClass = "el-card__body card__body ";
        bodyClass += $attrs.bodyClass ? $attrs.bodyClass : "";

        const bodyVNode = createVNode(
          "div",
          { staticClass: bodyClass, style: bodyStyle },
          (() => {
            const { default: slotDefault } = $slots;
            if (slotDefault) return slotDefault;
            return [];
          })(),
          1
        );

        children.push(bodyVNode);
        return children;
      })()
    );
  },
};
</script>

<style lang="scss" scoped>
.card {
  margin: 24px;
  border-radius: 4px;
  border: 1px solid #ebeef5;
  background-color: #fff;
  color: #303133;
  transition: 0.3s;
  & + .card {
    margin-top: 0;
  }
  .card__header {
    font-weight: 600;
    border-left: 6px solid $--color-primary;
  }
  .card__body {
    padding: 8px;
  }
}
</style>

<template>
  <div
    v-show="visible"
    class="el-select-dropdown__item"
    :class="{
      selected: itemSelected,
      'is-disabled': disabled || groupDisabled || limitReached,
      hover: hover,
    }"
    @mouseenter="hoverItem"
    @click.stop="selectOptionClick"
  >
    <slot>
      <span :title="source.label">{{ source.label | title }}</span>
    </slot>
  </div>
</template>

<script>
import mixin from "./mixinOption";
export default {
  name: "SelectVirtualItem",
  filters: {
    title(label) {
      return label.length > 25 ? label.slice(0, 25) + "..." : label;
    },
  },
  mixins: [mixin],
  xins: [mixin],
  props: {
    source: {
      // here is: {uid: 'unique_1', text: 'abc'}
      type: Object,
      default() {
        return {};
      },
    },
  },
  computed: {
    value() {
      return this.source.value;
    },
  },
};
</script>
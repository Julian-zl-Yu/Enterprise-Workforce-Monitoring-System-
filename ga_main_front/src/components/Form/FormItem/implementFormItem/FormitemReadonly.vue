<template>
  <fragment>
    <div
      v-tips
      v-bind="$attrs"
      :data-show="isTable"
      :data-tips="value"
      :title="value"
      style="overflow: hidden;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 1;position: relative;"
      :class="[
        'item__label now-you-see-me-readonly add-weight',
        isTable ? 'in-table-readonly' : 'in-form-readonly',
        this.propClass,
      ]"
    >
      {{ value }}
    </div>
    <span class="cell-span-hidden">{{ value }}</span>
  </fragment>
</template>

<script>
export default {
  name: "FormitemReadOnly",
  props: {
    propClass: {
      required: false,
      default() {
        return "";
      },
    },
    value: {
      required: false,
      /*TODO: 到底做不做null验证？*/
      /* if (!(value || isNumberWhenInputing(value) || isString(value))) { console.log("FormItemReadonly value: ", value); } */
      validator: () => true,
      default() {
        return "";
      },
    },
    /*是否是列表模式，与isEdit配合使用*/
    isTable: {
      required: true,
      type: Boolean,
    },
  },
};
</script>

<style scoped lang="scss" src="./FormitemReadonly.scss"></style>

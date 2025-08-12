<template>
  <fragment>
    <div
      v-tips
      v-bind="$attrs"
      :data-show="isTable"
      :data-tips="value"
      :class="[
        'item__label now-you-see-me-readonly add-weight',
        isTable ? 'in-table-readonly' : 'in-form-readonly',
        propClass,
      ]"
    >
      <template v-if="configs.bindId === 'PERSONSTATUS'">
        <el-tag
          size="mini"
          :type="tagType"
        >
          {{ value }}
        </el-tag>
      </template>
      <template v-else>
        {{ value }}
      </template>
    </div>
    <span class="cell-span-hidden">{{ value }}</span>
  </fragment>
</template>

<script>
export default {
  name: "FormitemReadOnly",
  props: {
    propClass: {
      type: String,
      required: false,
      default() {
        return "";
      },
    },
    originValue: {
      default() {
        return "";
      },
    },
    configs: {
      required: true,
      type: Object,
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
  computed: {
    tagType() {
      if (this.configs.bindId === "PERSONSTATUS") {
        return this.originValue === "0" ? "" : "warning";
      }
      return "";
    },
  },
};
</script>

<style scoped lang="scss" src="./FormitemReadonly.scss">
</style>
<template>
  <fragment>
    <!--isEdit {{ isEdit }} -->
    <!--canEdit {{canEdit}}-->
    <template v-if="canEdit">
      <el-input-number
        v-if="minValue && maxValue"
        ref="formItem"
        v-bind="$attrs"
        :placeholder="attrPlaceholder"
        clearable
        :precision="precision"
        :disabled="attrDisabled"
        :value="value"
        :min="minValue"
        :max="maxValue"
        @input="handleNumberChange"
      />
      <el-input-number
        v-else-if="minValue"
        ref="formItem"
        v-bind="$attrs"
        :placeholder="attrPlaceholder"
        clearable
        :precision="precision"
        :disabled="attrDisabled"
        :value="value"
        :min="minValue"
        @input="handleNumberChange"
      />
      <el-input-number
        v-else-if="maxValue"
        ref="formItem"
        v-bind="$attrs"
        :placeholder="attrPlaceholder"
        clearable
        :precision="precision"
        :disabled="attrDisabled"
        :value="value"
        :max="maxValue"
        @input="handleNumberChange"
      />
      <el-input-number
        v-else
        ref="formItem"
        v-bind="$attrs"
        :placeholder="attrPlaceholder"
        clearable
        :precision="precision"
        :disabled="attrDisabled"
        :value="value"
        @input="handleNumberChange"
      />
    </template>
    <FormItemReadonly
      v-else
      :is-table="isTable"
      :value="setReadonlyText(value) || value"
    />
  </fragment>
</template>

<script>
import { handleNumberChange } from "@/components/Form/FormItem/commonFormItem";

import {
  model as baseModel,
  subCreated,
  subComputed,
  subProps,
  subComponents,
  precision,
  maxValue,
  minValue,
} from "@/components/Form/FormItem/mixinsFormItemBase";

export default {
  name: "ImplInputNumber",
  mixins: [subCreated, baseModel, subProps, subComputed, subComponents],
  computed: {
    formatter() {
      if (this.configs.formatter) {
        return this.configs.formatter;
      } else {
        return false;
      }
    },
    maxValue,
    minValue,
    precision,
  },
  data() {
    return {
      isShow: false,
    };
  },
  methods: {
    init() {},
    handleNumberChange,
    setReadonlyText(value) {
      this.textWhenReadOnly = value;
      return false;
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-input__inner {
  text-align: left !important;
  padding: 0 30px 0 15px !important;
}
::v-deep .el-input-number__decrease {
  display: none;
}
::v-deep .el-input-number__increase {
  display: none;
}
</style>

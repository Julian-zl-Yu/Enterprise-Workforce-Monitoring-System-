<template>
  <fragment>
    <!--isEdit {{ isEdit }} -->
    <!--canEdit {{canEdit}}-->
    <ren-dept-tree
      v-if="canEdit"
      ref="formItem"
      :dept-name.sync="deptName"
      clearable
      v-bind="$attrs"
      :value="value"
      :placeholder="attrPlaceholder || $t('dept.title')"
      :disabled="attrDisabled"
      @input="handleValueChange"
    />
    <FormItemReadonly
      v-else
      :prop-class="inputType + '-readonly'"
      :is-table="isTable"
      :value="value"
    />
  </fragment>
</template>

<script lang="ts">
import { handleValueChange } from "@/components/Form/FormItem/commonFormItem";
import {
  model as baseModel,
  subCreated,
  subComputed,
  subProps,
  subComponents,
} from "@/components/Form/FormItem/mixinsFormItemBase";

/*disable是否禁用*/
export default {
  name: "ImplDept",
  mixins: [subCreated, baseModel, subProps, subComputed, subComponents],
  data() {
    return {
      deptName: "",
    };
  },
  computed: {},
  methods: {
    init() {},
    handleValueChange,
  },
};
</script>
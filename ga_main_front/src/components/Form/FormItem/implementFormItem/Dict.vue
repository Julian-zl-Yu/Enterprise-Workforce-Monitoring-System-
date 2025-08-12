<template>
  <fragment>
    <template v-if="canEdit">
      <el-select
        ref="formItem"
        v-bind="$attrs"
        :value="value"
        :disabled="attrDisabled"
        :placeholder="attrPlaceholder"
        :multiple="attrMultiple"
        filterable
        :clearable="clearable"
        @change="handleValueChange"
      >
        <el-option
          v-for="(d, index) in selectOptions"
          :key="String(d.dictValue) + String(index)"
          :label="d.label"
          :value="d.value"
        />
      </el-select>
    </template>
    <FormItemReadonlyDict
      v-else
      :configs="configs"
      :origin-value="value"
      :is-table="isTable"
      :data-trigger="setReadonlyText(value)"
      :value="textWhenReadOnly"
    />
  </fragment>
</template>

<script>
import { handleValueChange } from "@/components/Form/FormItem/commonFormItem";
import {
  model as baseModel,
  subCreated,
  subComputed,
  subProps,
  subComponents,
  subData,
  attrMultiple,
  bindId,
  clearable,
  isCache
} from "@/components/Form/FormItem/mixinsFormItemBase";
import find from "lodash/find";
import remove from "lodash/remove";
import { getDictDataListAsync } from "@/utils/index.ts";
import FormItemReadonlyDict from "./FormitemReadonlyDict";
export default {
  name: "ImplSelect",
  components: { FormItemReadonlyDict },
  mixins: [
    subCreated,
    subData,
    baseModel,
    subProps,
    subComputed,
    subComponents,
  ],
  data() {
    return {
      selectOptions: [],
    };
  },
  computed: {
    /*Select 也有*/
    attrMultiple,
    bindId,
    clearable,
    isCache
  },
  methods: {
    handleValueChange,
    async init() {
      try {
        /* dictType:字典数据； */
        this.selectOptions = await getDictDataListAsync(this.bindId, this.isCache);
        if (this.configs?.removeValues) {
          this.selectOptions = remove(this.selectOptions, (n) => {
            if (!this.configs?.removeValues.includes(n.value)) return n;
          });
        }
      } catch (error) {
        console.error(error);
      }
    },
    setReadonlyText(value) {
      const target = find(this.selectOptions, (i) => i.value === value);
      if (target && target.label) {
        this.textWhenReadOnly = target.label;
      }else{
        this.textWhenReadOnly = ''
      }
      return  false;
    },
  },
};
</script>

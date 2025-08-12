<template>
  <!-- 
 * @FileDescription: 区域选择器
 * @fileName: Cascader.vue 
 * @LastEditTime: 2021-07-29 10:04:57
!-->
  <fragment>
    <template v-if="canEdit">
      <el-cascader
        ref="formItem"
        filterable
        clearable
        :value="value"
        :disabled="attrDisabled"
        :options="cascaderTree"
        :props="cascaderProps"
        @change="handleValueChange"
      />
    </template>
    <FormItemReadonly
      v-else
      :is-table="isTable"
      :data-trigger="setReadonlyText(value)"
      :value="textWhenReadOnly"
    />
  </fragment>
</template>

<script>
import {
  model as baseModel,
  subCreated,
  subComputed,
  subProps,
  subComponents,
  subData,
} from "@/components/Form/FormItem/mixinsFormItemBase";
import { getAreaCodeTree } from "@/utils/index.ts";
import { getAreaCodeNodeFromDB } from "@/utils/dataBase.ts";
import isArray from "lodash/isArray";
import last from "lodash/last";

export default {
  name: "ImplCascader",
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
      cascaderProps: {
        expandTrigger: "hover",
        checkStrictly: this.configs?.checkStrictly ? true : false,
      },
      cascaderTree: [],
    };
  },
  computed: {
    bindId() {
      return this.configs?.bindId;
    },
  },
  methods: {
    async init() {
      if (this.bindId === "areacode.china" && this.canEdit) {
        this.cascaderProps.label = "name";
        this.cascaderProps.value = "id";
        const cascaderTree = await this.getBindIdIsAreaCode();
        this.cascaderTree = cascaderTree;
      }
    },
    handleValueChange(value) {
      if (isArray(value)) {
        value = last(value);
      }
      this.$refs.formItem.toggleDropDownVisible();
      this.$emit("change", value);
    },
    async getBindIdIsAreaCode() {
      try {
        let cascaderTree = await getAreaCodeTree();
        return cascaderTree;
      } catch (error) {
        return [];
      }
    },
    async setReadonlyText(value) {
      if (!value) return "";
      const result = await getAreaCodeNodeFromDB(value);
      if (isArray(result)) {
        this.textWhenReadOnly = result
          .map((i) => i.name || i.areaName)
          .join("/");
      }
      return false;
    },
  },
};
</script>

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
        clearable
        @change="handleValueChange"
      >
        <el-option
          v-for="(d, index) in selectOptions"
          :key="String(d.dictValue) + String(index)"
          :label="d.label"
          :value="d.value"
        >
          <el-option
            v-for="(d, index) in selectOptions"
            :key="String(d.dictValue) + String(index)"
          >
            <span
              style="float: left"
              :title="d.label"
            >{{
              d.label | title
            }}</span>
          </el-option>
        </el-option>
      </el-select>
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
} from "@/components/Form/FormItem/mixinsFormItemBase";
import find from "lodash/find";
import camelCase from "lodash/camelCase";
import isArray from "lodash/isArray";
import { STATUS_TYPE } from "@/components/Types";
import Resolver from "@/utils/Resolver";
import debounce from "lodash/debounce";

export const optionsCollection = {};
const resolverCollection = {};

export default {
  name: "ImplSelect",
  filters: {
    title(label) {
      return label && label.length > 25 ? label.slice(0, 25) + "..." : label;
    },
  },
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
    attrMultiple,
    bindId,
  },
  watch: {
    value() {
      this.setReadonlyText(this.vlaue);
    },
    selectOptions() {
      this.setReadonlyText(this.vlaue);
    },
  },
  methods: {
    handleValueChange,
    async init() {
      /* selectArray:[]； */
      try {
        if (this?.configs?.selectArray || false) {
          this.selectOptions = this.configs.selectArray;
        } else if (this?.configs?.bindUrl || false) {
          const keyBybindUrl = camelCase(this?.configs?.bindUrl);
          if (isArray(optionsCollection[keyBybindUrl])) {
            this.selectOptions = optionsCollection[keyBybindUrl];
          } else if (STATUS_TYPE.pendding === optionsCollection[keyBybindUrl]) {
            resolverCollection[keyBybindUrl].push(
              (data) => (this.selectOptions = data)
            );
          } else {
            resolverCollection[keyBybindUrl] =
              resolverCollection[keyBybindUrl] || new Resolver();
            resolverCollection[keyBybindUrl].push(
              (data) => (this.selectOptions = data)
            );
            optionsCollection[keyBybindUrl] = STATUS_TYPE.pendding;

            let { data: res } = await this.$http.get(this.configs.bindUrl);

            if (res.code !== 0) {
              throw new Error(res);
            } else {
              optionsCollection[keyBybindUrl] = res.data.splice(0,10);
              resolverCollection[keyBybindUrl].trigger(res.data);
              delete resolverCollection[keyBybindUrl];
            }
          }
        }
        // this.setSelectReadOnlyText();
      } catch (error) {
        console.error(error);
      }
    },
    setReadonlyText(value) {
      if (
        this.scope &&
        this.scope.row &&
        this.scope.row[`#${this.configs.prop}`]
      ) {
        this.textWhenReadOnly = this.scope.row[`#${this.configs.prop}`];
        return false;
      }
      if (this.selectOptions.length === 0) return;
      const target = find(this.selectOptions, (i) => i.value === value);
      if (target && target.label) {
        this.textWhenReadOnly = target.label;
        this.scope.row = this.scope.row || {};
        this.scope.row[`#${this.configs.prop}`] = target.label;
      }
      return "";
    },
  },
};
</script>
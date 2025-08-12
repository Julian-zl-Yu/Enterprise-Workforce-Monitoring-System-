<template>
  <fragment>
    <template v-if="canEdit">
      <SelectVirtual
        v-if="configs.lazyLoad"
        ref="formItem"
        v-bind="$attrs"
        :value="value"
        :disabled="attrDisabled"
        :placeholder="attrPlaceholder"
        :multiple="attrMultiple"
        filterable
        remote
        :remote-method="remoteMethod"
        clearable
        :loading="loading"
        :select-options="selectOptions"
        @change="handleValueChange"
      >
        <el-option
          v-for="(d, index) in selectOptions"
          :key="String(d.dictValue) + String(index)"
          :label="d.label"
          :value="d.value"
        />
      </SelectVirtual>
      <el-select
        v-else
        ref="formItem"
        v-bind="$attrs"
        :value="value"
        :disabled="attrDisabled"
        :placeholder="attrPlaceholder"
        :multiple="attrMultiple"
        filterable
        clearable
        @focus="selectFocus"
        :filter-method="dataFilter"
        @change="handleValueChange"
      >
        <el-option
          v-for="(d, index) in selectOptions"
          :key="String(d.dictValue) + String(index)"
          :label="d.label || d.LABEL || d[configs.SelectLabel]"
          :value="d.value || d.VALUE || d[configs.SelectValue]"
        />
      </el-select>
      <el-button
        v-show="userCustom && showCustomBtn"
        @click="goToAdd"
        id="custom-btn"
        type="primary"
        size="mini"
        plain
        >+</el-button
      >
    </template>
    <FormitemReadonlySelect
      v-else-if="multipleArr != [] && multipleValueArr != []"
      :configs="configs"
      :origin-value="value"
      :multipleArr="multipleArr"
      :multipleValueArr="multipleValueArr"
      :is-table="isTable"
      :data-trigger="setReadonlyText(value)"
      :value="textWhenReadOnly"
    />
  </fragment>
</template>

<script>
import FormitemReadonlySelect from "./FormitemReadonlySelect.vue";
import { handleValueChange } from "@/components/Form/FormItem/commonFormItem";
import SelectVirtual from "@/components/Form/FormItem/implementFormItem/SelectVirtual/SelectVirtual";
import {
  model as baseModel,
  subCreated,
  subComputed,
  subProps,
  subComponents,
  subData,
  attrMultiple,
  userCustom,
  bindId,
} from "@/components/Form/FormItem/mixinsFormItemBase";
import find from "lodash/find";
import camelCase from "lodash/camelCase";
import isArray from "lodash/isArray";
import { STATUS_TYPE } from "@/components/Types";
import Resolver from "@/utils/Resolver";
import filter from "lodash/filter";
import findIndex from "lodash/findIndex";

export const optionsCollection = {};
const resolverCollection = {};

export default {
  name: "ImplSelect",
  components: { SelectVirtual, FormitemReadonlySelect },
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
      loading: false,
      showCustomBtn: false,
      selectOptions: [],
      selectOptionsCopy: [],
      multipleArr: [],
      multipleValueArr: [],
    };
  },
  computed: {
    attrMultiple,
    bindId,
    userCustom,
  },
  created() {
    setTimeout(() => {
      if (isArray(this.selectOptions)) {
        this.multipleArr = this.selectOptions;
      }
      if (isArray(this.value)) {
        this.multipleValueArr = this.value;
      }
    }, 499);
  },
  mounted() {},
  watch: {
    value(val) {
      this.setReadonlyText(val);
    },
    selectOptions() {
      this.setReadonlyText(this.vlaue);
    },
    "configs.selectArray": function (value) {
      this.init();
      this.selectOptions = value;
    },
  },
  methods: {
    remoteMethod(query) {
      const keyBybindUrl = camelCase(this?.configs?.bindUrl);
      if (!query) {
        this.selectOptions = optionsCollection[keyBybindUrl];
        return;
      }
      this.loading = true;
      this.$nextTick(() => {
        this.loading = false;
        const reg = new RegExp(`${query}`, "g");
        this.selectOptions = filter(optionsCollection[keyBybindUrl], (option) =>
          reg.test(option.label)
        );
      });
    },
    handleValueChange,
    async init() {
      /* selectArray:[]； */
      try {
        if (this?.configs?.bindUrl && this?.configs?.noCache) {
          let { data: res } = await this.$http.get(this.configs.bindUrl);
          if (res.code !== 0) {
            throw new Error(res);
          } else {
            this.selectOptions = res.data;
            return;
          }
        }

        if (this?.configs?.selectArray || false) {
          this.selectOptions = this.configs.selectArray;
        } else if (this?.configs?.bindUrl || false) {
          const keyBybindUrl = camelCase(this?.configs?.bindUrl);
          if (
            isArray(optionsCollection[keyBybindUrl]) &&
            this.$store.state.selectDataKey[keyBybindUrl]
          ) {
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
              const optionRes = res.data;
              optionsCollection[keyBybindUrl] = optionRes;
              this.$store.state.selectDataKey[keyBybindUrl] = keyBybindUrl;
              resolverCollection[keyBybindUrl].trigger(optionRes);
              delete resolverCollection[keyBybindUrl];
            }
          }
        }
        // this.setSelectReadOnlyText();
      } catch (error) {
        console.error(error);
      }
      this.selectOptionsCopy = this.selectOptions;
    },
    setScopeRowPropValue(label) {
      this.textWhenReadOnly = label;
      this.scope = this.scope || {};
      this.scope.row = this.scope.row || {};
      this.scope.row[`#${this.configs.prop}`] = label;
      return label;
    },
    async setReadonlyText(value) {
      /* 缓存 */
      if (
        value &&
        this.scope &&
        this.scope.row &&
        this.scope.row[`#${this.configs.prop}`]
      ) {
        this.textWhenReadOnly = this.scope.row[`#${this.configs.prop}`];
        return this.textWhenReadOnly;
      }
      if (this.selectOptions.length === 0) return "";
      const target = find(this.selectOptions, (i) => i.value === value);
      return this.setScopeRowPropValue((target && target.label) || "");
    },
    selectFocus(e) {
      this.selectOptions = this.selectOptionsCopy;
      if (this.selectOptions?.length == 0) {
        this.showCustomBtn = true;
      }
    },
    dataFilter(val) {
      this.$refs.formItem.$children[0].$refs.input.value = val;
      let num = 0;
      if (val) {
        //val存在
        this.selectOptions = this.selectOptionsCopy.filter((item) => {
          if (
            !!~item.label.indexOf(val) ||
            !!~item.label.toUpperCase().indexOf(val.toUpperCase())
          ) {
            num++;
            return true;
          }
          // else {
          //   return false
          // }
        });
        this.showCustomBtn = false;
        if (num == 0) {
          this.showCustomBtn = true;
        }
      } else {
        //val为空时，还原数组
        this.selectOptions = this.selectOptionsCopy;
      }
    },
    goToAdd() {
      let query = {};
      query._t = Date.now();
      query.v = "add";
      query.fromName = "slagtruck-yzst01-yzst01";
      this.$router.push({
        name: "enterprise-cp02-cp02",
        query: {
          ...query,
        },
      });
    },
  },
};
</script>
<style lang="scss" scoped>
#custom-btn {
  position: absolute;
  top: 4px;
  right: -48px;
}
</style>

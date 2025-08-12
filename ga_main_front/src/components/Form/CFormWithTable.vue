<template>
  <div class="form-wrapper">
    <el-divider v-if="configs.divider">
      {{ configs.divider }}
    </el-divider>
    <el-form
      ref="refCForm"
      :class="['customize-form', 'label-position-right']"
      :model="formModel"
      :rules="DSLFormItemRule"
      status-icon
      :disabled="attrDisabled"
      @keyup.enter.native="$emit('enter')"
    >
      <!-- {{ formModel }} -->
      <!-- table 固定布局 -->
      <table
        v-if="tableLayout"
        class="customize-row"
      >
        <tr
          v-for="(trItem, index) in DSLFormLayout"
          :key="index"
        >
          <td
            v-for="({ p: prop, c, r }, tdIndex) in trItem"
            :key="prop || index + '_' + tdIndex"
            :colspan="c || 1"
            :rowspan="r || 1"
          >
            <el-form-item
              v-if="prop"
              :ref="'item_' + prop"
              :prop="prop"
            >
              <!--{{ $log("CForm.vue: ",stateModuleName, keyForm, prop,formModel[prop]) }}-->
              <label
                :for="DSLFormItems[prop].prop"
                :title="DSLFormItems[prop].label"
                class="el-form-item__label"
              >
                <span class="required">*</span>
                {{ checkLabel(DSLFormItems, prop) || DSLFormItems[prop].label }}
              </label>
              <FormItem
                v-model="formModel[prop]"
                :form-data="formModel"
                :configs="DSLFormItems[prop]"
              />
            </el-form-item>
            <span v-else />
          </td>
        </tr>
      </table>

      <!-- flex 流式布局 -->
      <el-row
        v-else
        :gutter="24"
        class="customize-row"
      >
        <el-col
          v-for="prop in DSLFormLayout"
          :key="prop"
          :span="getSpan(DSLFormItems[prop])"
          :offset="getOffset(DSLFormItems[prop])"
          :push="getPush(DSLFormItems[prop])"
        >
          <!-- {{DSLFormItems[prop]}} -->
          <el-form-item
            :ref="'item_' + prop"
            :prop="prop"
          >
            <!--{{ $log("$log in CForm.vue",stateModuleName, keyForm, prop,formModel[prop]) }}-->
            <!--readOnly_{{configs.readOnly}}-->
            <label
              :for="DSLFormItems[prop].prop"
              :title="DSLFormItems[prop].label"
              class="el-form-item__label"
            >
              <span class="required">*</span>
              {{ checkLabel(DSLFormItems, prop) || DSLFormItems[prop].label }}
            </label>
            <FormItem
              v-model="formModel[prop]"
              :form-data="formModel"
              :configs="DSLFormItems[prop]"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import _map from "lodash/map";
import isPlainObject from "lodash/isPlainObject";

export default {
  name: "CForm",
  provide() {
    const _this = this;
    return {
      CForm: _this,
    };
  },
  model: {
    prop: "value",
    event: "change",
  },

  props: {
    value: {
      type: [Object, Boolean],
      default() {
        return false;
      },
    },

    configs: {
      required: true,
      type: Object,
    },
  },
  data() {
    return {
      labelPosition: "top",
      formRules: {},
    };
  },
  computed: {
    formModel: {
      get() {
        if (this.stateModuleName) {
          return this.$store.state[this.stateModuleName][this.keyForm];
        } else if (isPlainObject(this.value)) {
          return this.value;
        } else {
          console.error("未提供必需的CForm v-model 或者 store 模块");
        }
      },
      set(formModel) {
        if (this.stateModuleName) {
          this.$store.state[this.stateModuleName][this.keyForm] = formModel;
        } else if (isPlainObject(this.value)) {
          this.$emit("change", formModel);
        } else {
          console.error("未提供必需的CFormQuery v-model 或者 store 模块");
        }
      },
    },
    tableLayout() {
      return this.configs?.DSLFormLayout || false;
    },
    itemRefs() {
      return this.$refs;
    },
    attrDisabled() {
      return (
        /* 当前=》 表单 =》 默认 */
        this?.configs?.disabled || false
      );
    },
    /* state 对应当前表单model对象：默认是formData */
    keyForm() {
      return this.configs?.keyForm || "formData";
    },
    /* @namespace state 模块名字*/
    stateModuleName() {
      return this.configs?.moduleName || "";
    },
    DSLFormLayout() {
      /*console.log(this.configs);*/
      let layout = this.configs?.DSLFormLayout || [];
      if (layout.length < 1) {
        layout = _map(this.configs?.DSLFormItems || [], (i) => i.prop);
      }
      // /*console.log("DSLFormLayout -> layout", layout.join("->"));*/
      return layout;
    },
    DSLFormItems() {
      return this.configs?.DSLFormItems || [];
    },
    DSLFormItemRule() {
      return this.configs?.DSLFormItemRule || {};
    },
  },
  methods: {
    checkLabel(item, prop) {
      let res = item[prop];
      if (!res) {
        console.error("checkLabel -> item,prop", item, prop);
        return `DSLFormItems 【${prop}】 ERROR`;
      } else {
        return false;
      }
    },
    formReset() {
      this.$refs?.refCForm?.resetFields();
    },
    formValidate() {
      return new Promise((resolve, reject) => {
        this.$refs?.refCForm?.validate((valid) => {
          if (valid) {
            resolve(true);
          } else {
            reject("未通过表单验证");
          }
        });
      });
    },
    getAttr(attr, item) {
      const DEFAULT_MAP = {
        col: 1,
      };
      return item[attr] || DEFAULT_MAP[attr];
    },
    getSpan(item) {
      return item?.span || 8;
    },
    getOffset(item) {
      return item?.offset || 0;
    },
    getPush(item) {
      return item?.push || 0;
    },
  },
};
</script>
<style lang="scss">
.customize-form {
  .customize-row {
    width: 100%;
  }

  .el-form-item {
    .el-form-item__label {
      .required {
        display: none;
      }
    }

    &.is-required {
      .el-form-item__label {
        position: relative;
        .required {
          display: inline-block;
          position: absolute;
          left: 130px;
          color: red;
          top: 50%;
          transform: translateY(-42%);
        }
      }
    }
    .el-form-item__content {
      display: flex;
      flex-flow: row nowrap;
      justify-content: center;
      align-items: center;

      .form-item {
        flex: 1;
      }
      .el-form-item__error {
        left: 140px;
      }
    }
  }

  &.label-position {
    &-left {
      .el-form-item {
        .el-form-item__content {
          .el-form-item__label {
            text-align: left;
          }
        }
      }
    }
    &-right {
      .el-form-item {
        .el-form-item__content {
          .el-form-item__label {
            text-align: right;
          }
        }
      }
    }
  }
}
</style>
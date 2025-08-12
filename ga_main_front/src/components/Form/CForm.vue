<template>
  <div class="form-wrapper">
    <el-divider v-if="configs.divider">
      {{ configs.divider }}
    </el-divider>
    <el-form
      ref="refCForm"
      :class="[
        'customize-form label-position',
        labelPosition,
        { 'is-readonly': isReadonly },
      ]"
      :model="formModel"
      :rules="DSLFormItemRule"
      status-icon
      :disabled="attrDisabled"
      @keyup.enter.native="$emit('enter')"
    >
      <!-- {{ formModel }} -->
      <!-- table 固定布局 -->
      <div class="customize-row" :style="layoutInfo.containerInfo.style">
        <div
          v-for="{ prop, style: itemStyle } in layoutInfo.itemInfo"
          :key="prop"
          class="customize-col"
          :style="itemStyle"
        >
          <el-form-item v-if="prop" :ref="'item_' + prop" :prop="prop">
            <!--{{ $log("CForm.vue: ",stateModuleName, keyForm, prop,formModel[prop]) }}-->
            <label
              :for="DSLFormItems[prop].prop"
              :title="DSLFormItems[prop].label"
              class="el-form-item__label"
              :style="{width:configs.labelWidth || '140px'}"
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
        </div>
      </div>
    </el-form>
  </div>
</template>

<script>
import each from "lodash/each";
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
      formRules: {},
    };
  },
  computed: {
    labelPosition() {
      return this?.configs?.labelPosition || "right";
    },
    formModel: {
      get() {
        if (isPlainObject(this.value)) {
          return this.value;
        } else if (this.stateModuleName) {
          return this.$store.state[this.stateModuleName][this.keyForm];
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
      let layout = this.configs?.DSLFormLayout || [];
      if (layout.length < 1) {
        const colTotal = 3;
        let cellArray = [];
        each(this.configs?.DSLFormItems || [], (item) => {
          cellArray.push(item);
          if (cellArray.length === colTotal) {
            layout.push(cellArray);
            cellArray = [];
          }
        });
        if (cellArray.length > 0) {
          layout.push(cellArray);
        }
      }
      return layout;
    },
    /* 依赖布局对象DSLFormLayout 产出 grid布局style */
    layoutInfo() {
      const itemInfo = [];
      /* 布局列总数 */
      let colTotal = 0;
      let rowTotal = this.DSLFormLayout.length;
      this.DSLFormLayout.forEach((rows, rowIndex) => {
        rows.forEach((item) => {
          if (rowIndex === 0) {
            colTotal += item.c || 1;
          }
          itemInfo.push({
            prop: item.p || item.prop,
            style: {
              gridColumnStart: `span ${item.c || 1}`,
              gridRowStart: `span ${item.r || 1}`,
            },
          });
        });
      });
      const containerInfo = {
        style: {
          display: "grid",
          gridColumnGap: "24px",
          gridTemplateColumns: `repeat(${colTotal}, 1fr)`,
          gridTemplateRows: `repeat(${rowTotal},minmax(32px,auto))`,
        },
      };
      return { containerInfo, itemInfo };
    },
    isReadonly() {
      return this.configs?.readOnly || false;
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
/* .form-no-rule {
  .el-form.customize-form {
    .customize-col {
      .el-form-item {
        border-bottom: 0px;
        outline: 1px solid red;
      }
    }
  }
}
 */
.customize-form {
  /* 只读状态下 required红心不显示 */
  &.is-readonly {
    .el-form-item {
      margin-bottom: 2px;
      &.is-required {
        .el-form-item__label {
          .required {
            display: none;
          }
        }
      }
    }
  }
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
          color: red;
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
    &.right {
      .el-form-item__label {
        text-align: right;
      }
    }
    &.left {
      .el-form-item__label {
        text-align: left;
      }
    }
    .el-form-item {
      .el-form-item__content {
        .el-form-item__label {
          width: 140px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
  }
}
</style>
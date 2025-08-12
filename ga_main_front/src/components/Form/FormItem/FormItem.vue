<template>
  <div
    :class="[
      'form-item all-item wrapper',
      { 'no-el-form-item__error': attrFormReadOnly },
    ]"
    :data-identify="`dci-${configs.prop}-${scope.$index || 1}`"
  >
    <!--identify:dynamic-column-item-->
    <!--currentFormItem {{ currentFormItem }}-->
    <!--isTable {{ typeof isTable }}-->
    <!--isEdit {{ isEdit }}-->
    <!--value {{ value }}-->
    <!--attrFormReadOnly {{ attrFormReadOnly }}-->
    <!--区别disabled readOnly就是只加载展示的信息，没有element控件的渲染-->
    <!-- "form-item all-item wrapper"与tips 超长才显示有关联，应该保持一致 v-tips.js if (elP.className === "form-item all-item wrapper") -->
    <div
      ref="formItem"
      :is="currentFormItem"
      :component-type="componentType"
      :attr-disabled="attrDisabled"
      :attr-form-read-only="attrFormReadOnly"
      :attr-placeholder="attrPlaceholder"
      :configs="configs"
      :value="value"
      :form-data="formData"
      :is-edit="isEdit"
      :is-table="isTable"
      :row-index="rowIndex"
      :scope="scope"
      @change="handleValueChange"
      @hook:mounted="handleMounted"
    />
  </div>
</template>

<script>
import {
  props as baseProps,
  model as baseModel,
} from "@/components/Form/FormItem/mixinsFormItemBase";
import ImplInputCardReader from "@/components/Form/FormItem/implementFormItem/InputCardReader";
import { FORM_ITEM_TYPE } from "@/components/Types";
import ImplInputNumber from "@/components/Form/FormItem/implementFormItem/InputNumber";
import ImplPassOrNot from "@/components/Table/TableColumnItems/PassOrNot";
import ImplRYGcode from "@/components/Table/TableColumnItems/RYGcode";
import ImplRegion from "@/components/Form/FormItem/implementFormItem/Region";
import ImplAvatar from "@/components/Form/FormItem/implementFormItem/Avatar/Avatar";
import ImplInput from "@/components/Form/FormItem/implementFormItem/Input";
import ImplDept from "@/components/Form/FormItem/implementFormItem/Dept.vue";
import ImplDict from "@/components/Form/FormItem/implementFormItem/Dict";
// import ImplDict2 from "@/components/Form/FormItem/implementFormItem/Dict2";
import ImplMoney from "@/components/Form/FormItem/implementFormItem/Money";
import ImplRadio from "@/components/Form/FormItem/implementFormItem/Radio";
import ImplSelect from "@/components/Form/FormItem/implementFormItem/Select";
import ImplCascader from "@/components/Form/FormItem/implementFormItem/Cascader";
import ImplDatePicker from "@/components/Form/FormItem/implementFormItem/DatePicker";
import ImplPositionPicker from "@/components/Form/FormItem/implementFormItem/PositionPicker/PositionPicker";
import { handleValueChange } from "@/components/Form/FormItem/commonFormItem";
// import ImplAreaPicker from "@/components/Form/FormItem/implementFormItem/AreaPicker/AreaPicker";
import ImplDateRange from "@/components/Form/FormItem/implementFormItem/DateRange";
import ImplInputFocus from "@/components/Form/FormItem/implementFormItem/inputFocus";
import ImplSMScode from "@/components/Form/FormItem/implementFormItem/SMScode";
import ImplInputBtn from "@/components/Form/FormItem/implementFormItem/InputBtn";
export default {
  name: "FormItem",
  components: {
    ImplTextarea: ImplInput,
    ImplAreaPicker: ImplInput,
    ImplRegion,
    ImplDict,
    ImplDept,
    ImplInput,
    ImplInputFocus,
    ImplMoney,
    ImplRadio,
    ImplSelect,
    ImplAvatar,
    ImplCascader,
    ImplPassOrNot,
    ImplRYGcode,
    ImplDatePicker,
    ImplDateRange,
    ImplInputNumber,
    ImplPositionPicker,
    ImplInputCardReader,
    ImplSMScode,
    ImplInputBtn
  },
  mixins: [baseProps, baseModel],
  data() {
    return {
      count: 0,
    };
  },
  computed: {
    /** 组件类型 默认Input 可以通过其他属性计算*/
    componentType() {
      return this?.configs?.type || FORM_ITEM_TYPE.Input;
    },
    currentFormItem() {
      /*const currentFormItem = `Impl${FORM_ITEM_TYPE[this.componentType]}`;
                                    console.log(currentFormItem);
                                    return currentFormItem;*/
      return `Impl${FORM_ITEM_TYPE[this.componentType]}`;
    },
    attrPlaceholder() {
      return this?.configs?.placeholder || "";
    },
    attrDisabled() {
      return (
        /*item自己的disabled权重较低 当前=》 表单 =》 默认 */
        this?.configs?.disabled || this?.$parent?.elForm?.disabled || false
      );
    },
    /* 表单只读 权重高于item自己的disabled*/
    attrFormReadOnly() {
      /*只通过CForm configs 配置*/
      return this.CForm?.configs?.readOnly || false;
    },
  },
  inject: {
    CForm: {
      default() {
        return {};
      },
    },
  },
  beforeUpdate() {
    // console.log("beforeUpdate", this.configs.prop, this.count++);
  },
  updated() {
    // console.log("updated", this.configs.label, this.count++);
  },
  methods: {
    handleValueChange,
    handleMounted() {
      // console.log(`${this.configs.label} subItem mounted!`);
    },
  },
};
</script>

<style lang="scss">
// @include nowYouCanNotSeeMe();

.cell-span-hidden {
  overflow: hidden;
  position: fixed;
  height: 0;
  word-break: keep-all;
  z-index: -1;
  bottom: 0;
  right: 0;
}
.cell-span-show {
  word-break: keep-all;
  position:absolute;
  top: 0;
  right: 0;
}
.form-item.all-item.wrapper {
  position: relative;

  &.no-el-form-item__error {
    & + .cell-span-hidden {
      & + .el-form-item__error {
        display: none;
      }
    }
    & + .el-form-item__error {
      display: none;
    }
  }

  $color-border: #b2b5bb;
  /*只读模式下的下划线*/
  .now-you-see-me-readonly {
    padding: 0;
    min-height: 37px;
    cursor: not-allowed;
    width: 100%;
    display: inline-block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    &.el-form-item__label {
      width: 140px;
      text-align: right;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    &.in-form-readonly {
      width: 100%;
      text-align: left;
      text-indent: 1rem;
      white-space: break-spaces;
      overflow: auto;
      height: 41px;
    }
    &.textarea-readonly {
      /*只读模式下textarea全框*/
      border: 1px solid $color-border;
      border-radius: 4px;
      min-height: 74px;
      line-height: 24px;
      padding: 8px 16px;
      white-space: break-spaces;
      max-height: 150px;
      overflow: auto;
    }
    /* 列表居中 */
    &.in-table-readonly {
      border-bottom: unset;
      cursor: unset;
      text-align: center;
    }
  }
  &.now-you-can-not-see-me {
    display: block;
    position: fixed;
    bottom: 0;
    right: 0;
    z-index: -2;
    width: 0;
    height: 0;
  }
  > .cell-span {
    width: 100%;
    display: inline-block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  > div {
    width: 100%;

    &.money.input-money {
      > input.el-input__inner {
        padding-right: 70px;
        text-align: right;
      }
    }
  }
}
</style>

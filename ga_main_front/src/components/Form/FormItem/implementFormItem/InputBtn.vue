<template>
  <fragment>
    <!--isEdit {{ isEdit }} -->
    <!--canEdit {{canEdit}}-->
    <template v-if="canEdit">
      <div style="display: flex;">
          <el-input
            ref="formItem"
            clearable
            v-bind="$attrs"
            :type="inputType"
            :value="value"
            :placeholder="attrPlaceholder"
            :show-password="attrShowPassword"
            :disabled="attrDisabled"
            @input="handleValueChange"
            :autosize='autosize' 
          />
          <el-button icon="el-icon-search" @click="btnClick"></el-button>
      </div>
    </template>
    <FormItemReadonly
      v-else
      :prop-class="inputType + '-readonly'"
      :is-table="isTable"
      :value="value"
    />
  </fragment>
</template>

<script>
import { FORM_ITEM_TYPE } from "@/components/Types";
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
  name: "ImplInputBtn",
  mixins: [subCreated, baseModel, subProps, subComputed, subComponents],
  computed: {
    autosize(){
       if (this.componentType === FORM_ITEM_TYPE.Textarea && this.configs?.autosize) {
        return true;
      }else{
        return false
      }
    },
    inputType() {
      if (this.componentType === FORM_ITEM_TYPE.Textarea) {
        return "textarea";
      } else if (this.attrShowPassword) {
        return "password";
      } else {
        return "text";
      }
    },
    attrShowPassword() {
      return this?.configs?.showPassword || false;
    },
  },
  methods: {
    init() {},
    handleValueChange,
    btnClick(){
      if (!this?.configs?.done) {
        alert("未覆盖done方法");
        console.error("未覆盖done方法");
        console.error(this.configs);
      } else {
        this?.configs?.done(this.value);
      }
    }
  },
};
</script>
<template>
  <fragment>
    <!--isEdit {{ isEdit }}-->
    <!--canEdit {{canEdit}}-->
    <template v-if="canEdit">
      <el-radio-group
        ref="formItem"
        :value="value"
        :disabled="attrDisabled"
        @input="handleValueChange"
      >
        <div
          :is="radioIs"
          v-for="radioItem in radioArray"
          :key="radioItem.value"
          :border="radioItemNeedBorder"
          :label="radioItem.value"
        >
          {{ radioItem.label }}
        </div>
      </el-radio-group>
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
  subData
} from "@/components/Form/FormItem/mixinsFormItemBase";

export default {
  name: "ImplInput",
  mixins: [
    subCreated,
    subData,
    baseModel,
    subProps,
    subComputed,
    subComponents
  ],
  data() {
    return {
      radioIs: "ElRadio",
      radioArray: []
    };
  },
  computed: {
    radioItemNeedBorder() {
      return this.configs?.itemBorder || false;
    }
  },
  methods: {
    init() {
      this.radioCheckRadioIs();
      let radioArray = this.configs?.radioArray || [];
      this.radioArray = radioArray;
    },
    handleValueChange,
    /* radio 不同的类型 */
    radioCheckRadioIs() {
      if (this.configs?.radioIs) {
        this.radioIs = this.configs.radioIs;
      }
    },
    setReadonlyText(value) {
      for (let i = 0; i < this.radioArray.length; i++) {
        if (this.radioArray[i].value === value) {
          this.textWhenReadOnly = this.radioArray[i].label;
          break;
        }
      }
      return false;
    }
  }
};
</script>
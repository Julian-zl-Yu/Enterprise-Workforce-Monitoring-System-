<template>
  <fragment>
    <template v-if="canEdit">
      <el-date-picker
        ref="formItem"
        v-bind="$attrs"
        clearable
        :type="attrDateType"
        :value="value"
        :editable="false"
        :disabled="attrDisabled"
        :value-format="attrValueFormat"
        :format="attrFormat"
        @input="handleDateChange"
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
import dayjs from "dayjs";
import { STATUS_TYPE } from "@/components/Types";

export default {
  name: "ImplDatePicker",
  mixins: [
    subCreated,
    subData,
    baseModel,
    subProps,
    subComputed,
    subComponents,
  ],
  computed: {
    attrLimit() {
      return this?.configs?.limit || false;
    },
    attrValueFormat() {
      return this?.configs?.valueFormat || "yyyy-MM-dd";
    },
    attrFormat() {
      return this?.configs?.format || "yyyy-MM-dd";
    },
    attrDateType() {
      return this?.configs?.dateType || "date";
    },
  },
  methods: {
    init() {},
    handleDateChange(selectedDateValue) {
      if (!selectedDateValue) {
        return this.$emit("change", selectedDateValue);
      }
      /* 日期选择是否有限制，必须是满足 */
      let shouldUpdateDate = true;
      if (this.attrLimit) {
        /*TODO:formatSting由配置选项传入*/
        const formatString = "YYYY-MM-DD";
        const [command, targetProp] = this.attrLimit;
        const target = this.formData[targetProp] || targetProp;
        const targetDate = dayjs(target);
        const selectedDate = dayjs(selectedDateValue);
        const targetDateString = targetDate.format(formatString);

        let message = "未录入前置日期";
        if ("Invalid Date" === targetDateString) {
          shouldUpdateDate = false;
        } else {
          switch (command) {
            case "after": {
              shouldUpdateDate = targetDate.isBefore(selectedDate);
              message = `所选日期需在${targetDateString}之后`;
              break;
            }
            case "before": {
              shouldUpdateDate = selectedDate.isBefore(targetDate);
              message = `所选日期需在${targetDateString}之前`;
              break;
            }
            default: {
              shouldUpdateDate = false;
              message = "配置指令有误:['before','after']";
              break;
            }
          }
        }
        if (!shouldUpdateDate) {
          this.$notify({
            title: "警告",
            message,
            type: STATUS_TYPE.warning,
          });
        }
      }
      if (shouldUpdateDate) {
        this.$emit("change", selectedDateValue);
      }
    },
    async setReadonlyText(value) {
      if (this.format) {
        this.textWhenReadOnly = dayjs(value).format(this.format);
      } else {
        this.textWhenReadOnly = value;
      }
      return value;
    },
  },
};
</script>

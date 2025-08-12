<template>
  <fragment>
    <template v-if="canEdit">
      <el-date-picker
        ref="formItem"
        v-bind="$attrs"
        clearable
        type="daterange"
        :value="dateValue"
        :editable="false"
        unlink-panels
        :disabled="attrDisabled"
        :value-format="attrValueFormat"
        :picker-options="triPickerOptions"
        :format="attrFormat"
        @input="handleDateChange"
      />
    </template>
    <FormItemReadonly
      v-else
      :is-table="isTable"
      :data-trigger="setReadonlyText(dateValue)"
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

export default {
  name: "ImplDateRange",
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
      triPickerOptions: {
        // disabledDate是一个函数,参数是当前选中的日期值,这个函数需要返回一个Boolean值,
        disabledDate: (time) => {
          const [condition, date] = this.disabledDate;
          switch (condition) {
            case "after":
              if (date) {
                return time.getTime() < new Date(date).getTime();
              } else {
                return time.getTime() < new Date().getTime();
              }
            case "before":
              if (date) {
                return time.getTime() > new Date(date).getTime();
              } else {
                return time.getTime() > new Date().getTime();
              }
          }
        },
      },
      dateValue: "" || [],
    };
  },
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
    disabledDate() {
      return this?.configs?.disabledDate || "";
    },
  },
  watch: {
    value(newVal, oldVal) {
      if (newVal == null) {
        this.dateValue = ['', ''];
      } else {
        this.dateValue = this.value;
      }
    },
  },
  created() {
    Object.prototype.toString.call(this.value) === "[object Array]"
      ? (this.dateValue = this.value)
      : (this.dateValue = [this.value || "", this.value || ""]);
  },
  methods: {
    init() {
      // 初始化时间
      Object.prototype.toString.call(this.value) === "[object Array]"
        ? (this.dateValue = this.value)
        : (this.dateValue = [this.value || "", this.value || ""]);
    },
    handleDateChange(selectedDateValue) {
      if (!selectedDateValue) {
        return this.$emit("change", ['','']);
      }
      this.dateValue = selectedDateValue;
      this.$emit("change", selectedDateValue);
    },
    async setReadonlyText(value) {
      this.textWhenReadOnly = dayjs(value).format(this.format);
      return value;
    },
  },
};
</script>

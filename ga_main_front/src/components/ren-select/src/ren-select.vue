<template>
  <el-select
    v-if="!readOnly"
    :value="value + ''"
    :placeholder="placeholder"
    clearable
    @input="$emit('input', $event)"
  >
    <el-option
      v-for="data in dataList"
      :key="data.dictValue"
      :label="data.dictLabel"
      :value="data.dictValue"
    >
      {{ data.dictLabel }}
    </el-option>
  </el-select>
  <span v-else>
    {{ valueLabel }}
  </span>
</template>
<script>
import { getDictDataList, getDictDataListAsync } from "@/utils/index.ts";

export default {
  name: "RenSelect",
  props: {
    // eslint-disable-next-line vue/require-default-prop
    value: [Number, String],
    dictType: String,
    placeholder: String,
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      dataList: [],
      valueLabel: "",
    };
  },
  mounted() {
    getDictDataListAsync(this.dictType).then((res) => {
      this.dataList = res;
      if (this.readOnly) {
        this.dataList.filter((item) => {
          if (item.dictValue == this.value) {
            this.valueLabel = item.dictLabel;
          }
        });
      }
    });
  },
};
</script>

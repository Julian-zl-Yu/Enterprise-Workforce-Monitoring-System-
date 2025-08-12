<template>
  <el-radio-group :value="value + ''" @input="$emit('input', $event)">
    <el-radio
      v-for="data in dataList"
      :key="data.dictValue"
      :label="data.dictValue"
    >
      {{ data.dictLabel }}
    </el-radio>
  </el-radio-group>
</template>
<script>
import { getDictDataList,getDictDataListAsync } from "@/utils/index.ts";
export default {
  name: "RenRadioGroup",
  props: {
    value: [Number, String],
    dictType: String,
  },
  data() {
    return {
      dataList: [],
    };
  },
  mounted() {
    getDictDataListAsync(this.dictType).then((res) => {
      this.dataList = res;
    });
  },
};
</script>

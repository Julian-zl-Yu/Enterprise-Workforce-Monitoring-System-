<template>
  <router-view />
  <!-- <div class="container">
    <h1>{{ inputValue }}</h1>
    <h3 class="region">
      Region
    </h3>
    <FormItem
      v-model="inputValue"
      class="region"
      :configs="{type:FORM_ITEM_TYPE.Region}"
    />
    <FormItem
      v-model="inputValue"
      class="region"
      :configs="{type:FORM_ITEM_TYPE.Cascader,bindId: 'areacode.china'}"
    />
    <h3 class="region">
      el-cascader
    </h3>
  </div> -->
</template>
<script>
import { getAreaCodeTree } from "@/utils/index.ts";
import { FORM_ITEM_TYPE } from "@/components/Types";

export default {
  name: "DemoIndex",
  data() {
    return {
      FORM_ITEM_TYPE,
      regionOptions: [],
      inputValue: "",
    };
  },
  async mounted() {
    this.regionOptions = await this.getBindIdIsAreaCode();
  },
  methods: {
    async getBindIdIsAreaCode() {
      try {
        let regionOptions = await getAreaCodeTree();
        return regionOptions[0]?.children;
      } catch (error) {
        return [];
      }
    },
    handleChange(val) {
      console.log(val);
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  width: 100%;
  height: 600px;
  .region {
    display: block;
    width: 500px;
    margin: 30px auto;
  }
}
</style>
<template>
  <div
    class="btn-group"
    v-if="configsListBtns.length > 2 && configs.btnType != 1"
  >
    <div
      :is="currentBtnType"
      :configs="configsListBtns[0]"
      v-bind="$attrs"
      :scope="scope"
    />
    <el-dropdown style="margin-left:5px">
      <span
        class="el-dropdown-link"
        style="font-size:12px;color:#006dd9;font-weight: 600;cursor: pointer;"
      >
        更多<i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item
          style="display: block;text-align:center"
          :is="currentBtnType"
          v-for="(config, index) in configsListBtns"
          v-if="index != 0"
          :key="index"
          :configs="config"
          v-bind="$attrs"
          :scope="scope"
        />
      </el-dropdown-menu>
    </el-dropdown>
  </div>
  <div class="btn-group" v-else>
    <div
      :is="currentBtnType"
      v-for="(config, index) in configsListBtns"
      :key="index"
      :configs="config"
      v-bind="$attrs"
      :scope="scope"
    />
  </div>
</template>
<script>
import { BTN_GROUP_TYPE } from "@/components/Types";

export default {
  name: "BtnGroup",
  props: {
    configs: {
      required: true,
      type: Object,
    },
    scope: {
      type: Object,
      default() {
        return {};
      },
    },
  },
  data() {
    return {};
  },
  computed: {
    configsListBtns() {
      let btnList = this?.configs?.btnList;
      if (!btnList) {
        console.error("未配置Button列表 按钮  [btnList]", this.configs);
        return "Btn";
      }
      return btnList;
    },
    currentBtnType() {
      const btnType = BTN_GROUP_TYPE[this?.configs?.btnType || 0];
      if (!btnType) {
        console.error("未配置Button列表类型 [btnType]", this.configs);
        return "Btn";
      }
      return btnType;
    },
  },
};
</script>
<style lang="scss"></style>

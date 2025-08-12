<template>
  <!-- 
 * @FileDescription: echarts 图表组件
 * @fileName: HomeEcharts.vue 
 * @Author: lazy_cat 
 * @LastEditTime: 2021-06-23 16:23:52
!-->
  <div style="width:100%;height:100%">
    <div v-show="isShowEacharts" id="echartsDome" />
    <!-- 无数据 -->
    <NoData
      v-show="!isShowEacharts"
      style="margin:50px auto;width: 100%; height: 64px"
    />
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  props: {
    echartsConfigs: Object,
    isShowEacharts: Boolean,
  },
  data() {
    return {
      // isshow: false,
    };
  },
  created() {},
  mounted() {
    this.initEcharts(this.echartsConfigs);
  },
  methods: {
    // 初始化
    initEcharts() {
      var chartDom = document.getElementById("echartsDome");
      var myChart = echarts.init(chartDom);
      this.echartsConfigs && myChart.setOption(this.echartsConfigs);
    },
    redraw() {
      var chartDom = document.getElementById("echartsDome");
      var myChart = echarts.init(chartDom);
      myChart.resize();
    },
  },
};
</script>

<style scoped lang="scss">
#echartsDome {
  height: 230px;
  width: 100%;
}
</style>

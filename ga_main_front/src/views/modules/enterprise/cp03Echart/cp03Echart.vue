<template>
    <card>
      <div id="chart_use" style="width: 90%; height: 400px;"></div>
      <div id="chart_lack" style="width: 90%; height: 400px;"></div>
    </card>
</template>

<script>
import * as echarts from 'echarts';

/*模块名*/
const moduleName = "enterprise-cp03Echart-cp03Echart";

export default {
  data() {
    var _this = this;
    return {
      moduleName,
    };
  },
  created () {
  
  },
  mounted(){
    this.initEchart()
  },
  methods: {
    async initEchart(){
        let { data: res } = await this.$http.get(`/enterprise/cp03echart/echart`);

        var domUse = document.getElementById('chart_use');
        var domLack = document.getElementById('chart_lack');

        var chartUse = echarts.init(domUse);
        var chartLack = echarts.init(domLack);

        const chartLegend =  {
          Controller: '管理人员',
          OrdWorker: '普通工人',
          SkillWorker: '技能工人',
          TechWorker: '技术工人',
        }

        const chartData = res.data;

        const optionUse = {
          title: {
            text: '企业用工数据分析'
          },
          tooltip:{
            trigger: 'axis'
          },
          legend: {
            data: Object.values(chartLegend)
          },
          
          xAxis: {
            type: 'category',
            data: chartData.yearMonth,
            splitLine:{
              show: true,
              lineStyle:{
                type: 'dashed'
              }
            }
          },
          yAxis: {
            type: 'value'
          },
          series: []
        };

      const optionLack = {
        title: {
            text: '企业缺工数据分析'
          },
          tooltip:{
            trigger: 'axis'
          },
          legend: {
            data: Object.values(chartLegend)
          },
          xAxis: {
            type: 'category',
            // boundaryGap: false,
            data: chartData.yearMonth,
            splitLine:{
              show: true,
              lineStyle:{
                type: 'dashed'
              }
            }
          },
          yAxis: {
            type: 'value'
          },
          series: []
      }

      
        for(let i in chartLegend){
            optionUse.series.push({
              name: chartLegend[i],
              type: 'bar',
              data: chartData['use'+i]
            })

            optionLack.series.push({
              name: chartLegend[i],
              type: 'bar',
              data: chartData['lack'+i]
            })
          }

          chartUse.setOption(optionUse);
          chartLack.setOption(optionLack);

    }
  }
};
</script>

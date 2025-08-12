<template>
  <!-- 
 * @FileDescription: 首页
 * @fileName: home.vue 
 * @Author: lazy_cat 
 * @LastEditTime: 2021-07-09 10:23:26
!-->
  <div>
    <MainPage>
      <Card :style="{ height: '100%',overflowY:'auto'}" :bodyStyle="{padding:'0px'}">
        <div v-if="isJg">
          <div class="title">广安市企业用工监测系统</div>
          <div class="chartDiv">
            <div id="chartOne"></div>
            <div id="chartTwo"></div>
          </div>
          <div id="chartThree"></div>
        </div>
  
        <PageLoading v-else :title="'欢迎使用'" :tipTitle="'广安市企业用工监测系统'" />
      </Card>
    </MainPage>
  </div>
</template>

<script>
import PageLoading from "@/components/common/PageLoading";
import * as echarts from 'echarts';

import findIndex from "lodash/findIndex";
import {LS_KEY } from "@/components/Types";

export default {
  components: {
    PageLoading,
  },

  data() {
    return {
      isLoading: true,
      isJg: this.$store.state.user.userType == '1',  
      // 导航类型
      navType: "rn",
      isPermissions: false,
      // 当前角色列表
      roleIdList: this.$store.state.user.roleIdList,

    };
  },
  created() {
   
  },
  mounted() {
    

  
    if(sessionStorage.getItem(LS_KEY.isNamed) == 'false'){
        // 判断是否实名
        this.$confirm('当前账号未完成实名认证，是否前往?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning',
              showClose: false,
              closeOnClickModal: false
            }).then(() => {
              this.$router.push({
                  name: "enterprise-cp01-cp01",
                  query: {
                    v: "update",
                    id: sessionStorage.getItem(LS_KEY.cp0101),
                    _t: Date.now(),
                    isJumpRoute: true,
                  },
              });

            }).catch(() => {
            
            });
    }

    this.initEchartOne()
    this.initEchartTwo()
    this.initEchartThree()

    
  },
  methods: {
   async initEchartOne(){
      var chartDom = document.getElementById('chartOne');
      var myChart = echarts.init(chartDom);
      var option;
      let { data: res } = await this.$http.get("/enterprise/cp03tj/homePageByRegion");
      option = {
        title: {
          text: '',
          subtext: '',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: "{b} : {c} ({d}%)" 
        },
        legend: {
          orient: 'horizontal',
          left: 'left',
          formatter:function (name){
                let data = option.series[0].data;
                let total =0;
                let faultVal;
                let p;
                for (let i = 0;i < data.length; i++){
                    total += Number(data[i].value);
                    if(data[i].name == name){
                        faultVal = data[i].value;
                    }
                }
                p = ((faultVal / total) * 100).toFixed(2);
                return `${name} ${faultVal}   ${p}%`
            }
        },
        series: [
          {
            name: '',
            type: 'pie',
            radius: '50%',
            label:{
              normal:{
                position:'outside',
                formatter:"{b}: {c}({d}%)"
              }
            },
            data: res.data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };

      option && myChart.setOption(option);
    },
   async initEchartTwo(){
      var chartDom = document.getElementById('chartTwo');
      var myChart = echarts.init(chartDom);
      var option;

      let { data: res } = await this.$http.get("/enterprise/cp03tj/homePageByEstate");
      option = {
        title: {
          text: '',
          subtext: '',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: "{b} : {c} ({d}%)" 
        },
        legend: {
          orient: 'horizontal',
          left: 'left',
          formatter:function (name){
                let data = option.series[0].data;
                let total =0;
                let faultVal;
                let p;
                for (let i = 0;i < data.length; i++){
                    total += Number(data[i].value);
                    if(data[i].name == name){
                        faultVal = data[i].value;
                    }
                }
                p = ((faultVal / total) * 100).toFixed(2);
                return `${name} ${faultVal}   ${p}%`
            }
        },
        series: [
          {
            name: '',
            type: 'pie',
            radius: '50%',
            label:{
              normal:{
                position:'outside',
                formatter:"{b}: {c}({d}%)"
              }
            },
            data: res.data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };

      option && myChart.setOption(option);
    },
    async initEchartThree(){
      var chartDom = document.getElementById('chartThree');
      var myChart = echarts.init(chartDom);
      var option;

      let { data: res } = await this.$http.get("/enterprise/cp03tj/homePageByYear");


      option = {
        title: {
          text: '',
          subtext: '',
          left: 'center'
        },
        grid:{
          left: '5%'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        xAxis: {
          type: 'category',
          data: res.data.xaxis
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: res.data.series,
            type: 'bar',
            barWidth: '40%',
            label:{
            
              normal: {
                show: true,
                textStyle: {
                    fontSize: 18 
                  }
            },
            },
          }
        ]
      };

      option && myChart.setOption(option);
    }
  
  },
};
</script>

<style scoped lang="scss">
.title{
  text-align: center;
  font-size: 36px;
  line-height: 80px;
  height: 80px;
}

.chartDiv{
  display: flex;
}

#chartOne{
  width: 50%;
  padding-left: 3%;
  height: 350px;
}

#chartTwo{
  width: 50%;
  padding-left: 3%;
  height: 350px;
}

#chartThree{
  width: 100%;
  height: 350px;
}

.get_through {
  width: 86px;
  height: 66px;
  text-align: center;
  line-height: 66px;
  font-size: 26px;
  color: #fff;
}
.topCard {
  p {
    font-weight: 600;
    margin: 9px 6px;
    color: #333;
    span {
      font-weight: 400;
      color: #666;
    }
  }
}
.textNoWrap {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.noSelect {
  -webkit-touch-callout: none;
  /* iOS Safari */
  -webkit-user-select: none;
  /* Chrome/Safari/Opera */
  -khtml-user-select: none;
  /* Konqueror */
  -moz-user-select: none;
  /* Firefox */
  -ms-user-select: none;
  /* Internet Explorer/Edge */
  user-select: none;
}
</style>
<style lang="scss">
//  箭头
.el-tooltip__popper[x-placement^="bottom"] .popper__arrow {
  border-bottom-color: white !important;
}
// 箭头
.el-tooltip__popper[x-placement^="bottom"] .popper__arrow:after {
  border-bottom-color: white !important;
}
// 内容
.testtooltip {
  background: white !important;
  box-shadow: 1px 1px 10px 3px #d3d3d6;
}
</style>

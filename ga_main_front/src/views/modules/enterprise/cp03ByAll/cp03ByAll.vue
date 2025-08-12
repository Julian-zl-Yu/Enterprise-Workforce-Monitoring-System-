<template>
  <MainPage :configs="configsMainPage">
    <CFormQuery v-model="formDataQuery" :configs="configsCFormQuery" @query="getTableList" />
    <Card style="height: 680px; overflow-y: scroll;"  >
      <div style="line-height: 2.5; padding: 12px; "  v-loading="dataListLoading">
        <h3>总体情况</h3>
        <p>{{currentYear}}年{{currentMonth}}月，
          广安市{{dataNum.allCpCountThisMonth_1}}户监测企业在岗职工总数为{{dataNum.allWorkerCountThisMonth_1}}人，比上月建档期岗位总数{{dataNum.difference_1}}人，{{dataNum.difference_1&&dataNum.difference_1.indexOf('增加') == -1 ? '减幅':'增幅'}}为{{dataNum.differenceRange_1}}。其中，增员企业{{dataNum.addWorkerCpCount_1}}家，占监测企业数的{{dataNum.addWorkerCpRange_1}}，增员{{dataNum.addWorkerCount_1}}人，增幅{{dataNum.addWorkerRange_1}}；减员企业{{dataNum.reduceWorkerCpCount_1}}家，占监测企业数的{{dataNum.reduceWorkerCpRange_1}}，减员{{dataNum.reduceWorkerCount_1}}人，减幅{{dataNum.reduceWorkerRange_1}}。
        </p>
        <h3>产业情况分析</h3>
        <p>
          从产业分类来看，第一产业用工{{dataNum.workerCount_2_1}}人，占用工总量{{dataNum.workerRange_2_1}}，较上期{{dataNum.workerDifference_2_1}}人，变化幅度{{dataNum.workerDifferenceRange_2_1}}，较年初{{dataNum.workerDifferenceBeginYear_2_1}}人，变化幅度{{dataNum.workerDifferenceRangeBeginYear_2_1}}；第二产业用工{{dataNum.workerCount_2_2}}人，占用工总量{{dataNum.workerRange_2_2}}，较上期{{dataNum.workerDifference_2_2}}人，变化幅度{{dataNum.workerDifferenceRange_2_2}}，较年初{{dataNum.workerDifferenceBeginYear_2_2}}人，变化幅度{{dataNum.workerDifferenceRangeBeginYear_2_2}}；第三产业用工{{dataNum.workerCount_2_3}}人，占用工总量{{dataNum.workerRange_2_3}}，较上期{{dataNum.workerDifference_2_3}}人，变化幅度{{dataNum.workerDifferenceRange_2_3}}，较年初{{dataNum.workerDifferenceBeginYear_2_3}}人，变化幅度{{dataNum.workerDifferenceRangeBeginYear_2_3}}
        </p>
        <h3>行业情况分析</h3>
        <p>
          从行业分类来看，增员人数最多的前5个行业分别是：{{dataNum.addWorkerCountTop_3}}；增员幅度最大的前5个行业分别是：{{dataNum.addWorkerRangeTop_3}}；减员人数最多的前5个行业分别是：{{dataNum.reduceWorkerCountTop_3}}；减员幅度最大的前5个行业分别是：{{dataNum.reduceWorkerRangeTop_3}}
        </p>
        <h3>地区用工分析</h3>
        <p>与上月相比，
          7个地区中，{{dataNum.noChangeCount_4}}个地区监测企业用工保持不变，{{dataNum.addChangeCount_4}}个地区监测企业用工增加，总体增幅为{{dataNum.addChangeRange_4}}；{{dataNum.reduceChangeCount_4}}个地区监测企业用工减少，总体减幅为{{dataNum.reduceChangeRange_4}}；
        </p>
        <p> 与年初相比，
          7个地区中，{{dataNum.noChangeCountBeginYear_4}}个地区监测企业用工保持不变，{{dataNum.addChangeCountBeginYear_4}}个地区监测企业用工增加，总体增幅为{{dataNum.addChangeRangeBeginYear_4}}；{{dataNum.reduceChangeCountBeginYear_4}}个地区监测企业用工减少，总体减幅为{{dataNum.reduceChangeRangeBeginYear_4}}
        </p>
        <h3>园区用工分析</h3>
        <p>
          与上月相比，9个园区中，{{dataNum.noChangeCount_5}}个园区监测企业用工保持不变，{{dataNum.addChangeCount_5}}个园区监测企业用工增加，总体增幅为{{dataNum.addChangeRange_5}}；{{dataNum.reduceChangeCount_5}}个园区监测企业用工减少，总体减幅为{{dataNum.reduceChangeRange_5}}；
        </p>
        <p>
          与年初相比， 9个园区中，{{dataNum.noChangeCountBeginYear_5}}个园区监测企业用工保持不变，{{dataNum.addChangeCountBeginYear_5}}个园区监测企业用工增加，总体增幅为{{dataNum.addChangeRangeBeginYear_5}}；{{dataNum.reduceChangeCountBeginYear_5}}个园区监测企业用工减少，总体减幅为{{dataNum.reduceChangeRangeBeginYear_5}}
        </p>
        <h3>企业规模分析</h3>
        <p>
          大型企业{{dataNum.workerCpCount6_1}}家，共{{dataNum.workerCount6_1}}名员工。与上月相比，{{getText(dataNum.changeWorkerCount6_1)}}名员工，变化幅度为{{dataNum.changeWorkerRange6_1}}。与年初相比，{{getText(dataNum.changeWorkerCountBeginYear6_1)}}名员工，变化幅度为{{dataNum.changeWorkerRangeBeginYear6_1}}。
        </p>
        <p>
          中型企业{{dataNum.workerCpCount6_2}}家，共{{dataNum.workerCount6_2}}名员工。与上月相比，{{getText(dataNum.changeWorkerCount6_2)}}名员工，变化幅度为{{dataNum.changeWorkerRange6_2}}。与年初相比，{{getText(dataNum.changeWorkerCountBeginYear6_2)}}名员工，变化幅度为{{dataNum.changeWorkerRangeBeginYear6_2}}。
        </p>
        <p>
          小型企业{{dataNum.workerCpCount6_3}}家，共{{dataNum.workerCount6_3}}名员工。与上月相比，{{getText(dataNum.changeWorkerCount6_3)}}名员工，变化幅度为{{dataNum.changeWorkerRange6_3}}。与年初相比，{{getText(dataNum.changeWorkerCountBeginYear6_3)}}名员工，变化幅度为{{dataNum.changeWorkerRangeBeginYear6_3}}。
        </p>
        <p>
          微型企业{{dataNum.workerCpCount6_4}}家，共{{dataNum.workerCount6_4}}名员工。与上月相比，{{getText(dataNum.changeWorkerCount6_4)}}名员工，变化幅度为{{dataNum.changeWorkerRange6_4}}。与年初相比，{{getText(dataNum.changeWorkerCountBeginYear6_4)}}名员工，变化幅度为{{dataNum.changeWorkerRangeBeginYear6_4}}。
        </p>
        <h3>缺工情况分析</h3>
        <p>
          监测企业中，缺工企业{{dataNum.nowLackCpCount_7}}家，缺工人数{{dataNum.nowLackWorkerCount_7}}人，比上期缺工企数{{getText(dataNum.differenceLackCpCount_7)}}家，比年初缺工企业{{getText(dataNum.differenceLackCpCountBeginYear_7)}}家。比上期缺工人数{{getText(dataNum.differenceLackWorkerCount_7)}}人，比年初缺工工人{{getText(dataNum.differenceLackWorkerCountBeginYear_7)}}人。从地区来看，有{{dataNum.lackByRegion_7}}个地区缺工。从行业来看有{{dataNum.lackByIndustry_7}}个行业缺工。
        </p>
      </div>
    </Card>
  </MainPage>
</template>

<script>
  /* 混入基类方法 */
  import mixinsBasePage from "@/mixins/basePage.ts";
  import mixinsMainPage from "@/mixins/mainPage.ts";
  /* 应用状态和页面描述信息 */
  import {
    state,
    defaultFormQueryData
  } from "./state";
  import {
    DSLFormItemsQuery,
    DSLTableHeader,
    MODULE_BASE_URL,
    MODULE_BASE_URL_EXPORT
  } from "./dsl";
  import {
    BTN_TYPE,
    BTN_GROUP_TYPE
  } from "@/components/Types";
  /* 组件 */

  import localStorageHelper from '@/utils/localStorageHelper';

  /*模块名*/
  const moduleName = "enterprise-cp03ByAll-cp03ByAll";
  /* 查询框store名 */
  const queryFormKeyName = "formQueryData";

  export default {
    mixins: [mixinsBasePage, mixinsMainPage],
    data() {
      var _this = this;
      return {
        moduleName,
        queryFormKeyName,
        formDataQuery: {
          ...defaultFormQueryData
        },

        configsMainPage: {
          moduleName,
          state
        },
        configsCFormQuery: {
          moduleName,
          keyForm: queryFormKeyName, //在state里面对应的表单索引字段
          DSLFormItems: DSLFormItemsQuery, //查询表单的查询项描述
        },
        currentYear: '',
        currentMonth: '',
        DSLTableHeader,
        moduleOptions: {
          getDataListURL: MODULE_BASE_URL,
          getDataListIsPage: false,
          exportURL: MODULE_BASE_URL_EXPORT,
          deleteURL: MODULE_BASE_URL,
          deleteIsBatch: true,
        },
        allCpCount: '',
        allWorkerCount: '',
        dataNum: {},
        configsBtns: {
          btnType: BTN_GROUP_TYPE.Btn,
          btnList: [{
              type: BTN_TYPE.export,
              click: () => _this.exportHandler(),
            },

          ],
        },
      };
    },
    mounted() {
      console.log(this.currentView);
    },
    methods: {
      getText: function(num){
        if(num < 0){
          return '减少' + Math.abs(num);
        } else if (num > 0){
          return '增加'+ num;
        } else {
          return '变化' + num;
        }

      },
      overrideFnSuccess: function (res) {
        this.currentYear =  this.formDataQuery.yearMonth.split('-')[0]
        this.currentMonth = this.formDataQuery.yearMonth.split('-')[1]
        if (res.code == 0) {
          this.dataNum = res.data;
        } else {
          return this.$message({
            message: res.msg,
            type: "error",
          });
        }
      }
    }
  };
</script>
<template>
  <MainPage :configs="configsMainPage" ref="mainPage">
    <CFormQuery
        v-model="formDataQuery"
        :configs="configsCFormQuery"
        @query="getTableList"
      />
      <Card  style="height: 100%;" ref="tableCard">
        <div class="option-btn-wrapper">
          <BtnGroup
            :configs="configsBtns"
          />
        </div>
        <el-table
          v-loading="dataListLoading"
          :data="dataList"
          border
          :max-height="tableHeight"
          style="width: 100%;"
          :span-method="arraySpanMethod"
          :header-cell-style="headerCellStyle"
          @selection-change="dataListSelectionChangeHandle"
        >
        
      
          <!-- 业务列 -->
          <template v-for="header in DSLTableHeader">
            <TableColumn
              :key="header.prop"
              :configs="header"
            />
          </template>
        </el-table>
      </Card>
  </MainPage>
</template>

<script>
/* 混入基类方法 */
import mixinsBasePage from "@/mixins/basePage.ts";
import mixinsMainPage from "@/mixins/mainPage.ts";
/* 应用状态和页面描述信息 */
import { state, defaultFormQueryData } from "./state";
import { DSLFormItemsQuery, DSLTableHeader, MODULE_BASE_URL,MODULE_BASE_URL_EXPORT } from "./dsl";
import { BTN_TYPE, BTN_GROUP_TYPE } from "@/components/Types";
/* 组件 */

import localStorageHelper from '@/utils/localStorageHelper';
import groupBy from "lodash/groupBy";


/*模块名*/
const moduleName = "enterprise-cp03ByLack-cp03ByLack";
/* 查询框store名 */
const queryFormKeyName = "formQueryData";

export default {
  mixins: [mixinsBasePage, mixinsMainPage],
  data() {
    var _this = this;
    return {
      moduleName,
      queryFormKeyName,
      formDataQuery: { ...defaultFormQueryData },
      
      configsMainPage: { moduleName, state },
      configsCFormQuery: {
        moduleName,
        keyForm: queryFormKeyName, //在state里面对应的表单索引字段
        DSLFormItems: DSLFormItemsQuery, //查询表单的查询项描述
      },
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
      tableHeight: 500,
      groupData: {},
      configsBtns: {
        btnType: BTN_GROUP_TYPE.Btn,
        btnList: [
          {
            type: BTN_TYPE.export,
            click: () => _this.exportHandler(),
          },
          
        ],
      },
    };
  },
  mounted(){
    console.log(this.currentView);
   
  },
  methods: {
    headerCellStyle:function({row, column, rowIndex, columnIndex}){
      if(columnIndex == 0 && rowIndex == 0){
        this.$nextTick(()=>{
          if(  document.getElementsByClassName(column.id).length != 0){
            document.getElementsByClassName(column.id)[0].setAttribute('colSpan', 2);
          }
        })
      }
    
      if(columnIndex == 1 && rowIndex == 0){
        return {
          display: 'none'
        }
      }
    },
    arraySpanMethod:function({ row, column, rowIndex, columnIndex }){
      if (columnIndex === 0) {
          if(rowIndex > 0 && row.category1 == this.dataList[rowIndex-1].category1){
            return {
              rowspan: 0,
              colspan: 0
            };
          } else {
            return {
              rowspan: this.groupData[row.category1].length,
              colspan: 1
            };
          }
        }
    },
    overrideFnSuccess: function(res){
      this.tableHeight = this.$refs['mainPage'].$el.clientHeight -38 - 54 - 50 - 24 - 36;
      if(res.code == 0){
        this.dataList = res.data.list;
        // 分组
        this.groupData = groupBy(this.dataList,'category1');
        this.allCpCount = res.data.allCpCount;
        this.allWorkerCount = res.data.allWorkerCount;
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

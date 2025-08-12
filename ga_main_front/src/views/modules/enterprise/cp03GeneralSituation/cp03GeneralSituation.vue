<template>
  <MainPage :configs="configsMainPage">
    <CFormQuery
        v-model="formDataQuery"
        :configs="configsCFormQuery"
        @query="getTableList"
      />
      <Card>
        <div class="option-btn-wrapper">
          <BtnGroup
            :configs="configsBtns"
          />
        </div>

        <el-table
          v-loading="dataListLoading"
          :data="dataList"
          border
          style="width: 100%"
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

/*模块名*/
const moduleName = "enterprise-cp03GeneralSituation-cp03GeneralSituation";
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
      configsBtns: {
        btnType: BTN_GROUP_TYPE.Btn,
        btnList: [
          {
            type: BTN_TYPE.export,
            click: () => _this.exportHandler(),
          },
          
        ],
      },
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
    };
  },
  mounted(){
    console.log(this.currentView);
  },
  methods: {
    overrideFnSuccess: function(res){
      if(res.code == 0){
        this.dataList = res.data.list;
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

<template>
  <MainPage :configs="configsMainPage">
    <ViewMain v-show="currentView == 'main'" />
    <!-- 弹窗, 新增 / 修改 -->
    <ViewSub
      v-if="currentView === 'detail'"
      ref="addOrUpdate"
      :current-view="currentView"
      @refreshDataList="getTableList"
    />
    <ViewSub
      v-if="currentView === 'update'"
      ref="addOrUpdate"
      :current-view="currentView"
      @refreshDataList="getTableList"
    />
    <el-dialog
      title="导入企业信息"
      :visible.sync="dialogFormVisible"
      v-loading="loading"
      element-loading-text="上传中"
    >
      <el-upload
        class="upload-demo"
        drag
        action="https://jsonplaceholder.typicode.com/posts/"
        :auto-upload="false"
        :limit="1"
        :on-change="handleChange1"
        :file-list="fileList"
        multiple
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsl、xls</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitUpload">确 定</el-button>
      </div>
    </el-dialog>
  </MainPage>
</template>

<script>
/* 混入基类方法 */
import mixinsBasePage from "@/mixins/basePage.ts";
import mixinsMainPage from "@/mixins/mainPage.ts";
import qs from "qs";
/* 应用状态和页面描述信息 */
import { state, defaultFormQueryData } from "./state";
import { DSLFormItemsQuery, DSLTableHeader, MODULE_BASE_URL } from "./dsl";
import { BTN_TYPE, BTN_GROUP_TYPE } from "@/components/Types";
/* 组件 */
import ViewMain from "./ViewMain.vue";
import ViewSub from "./ViewSub";
import { exportHandler } from "../../../../mixins/mainPage";
import { SuccessOrFailed, checkFormData, checkFile } from "@/utils/index.ts";
import { Loading } from 'element-ui';

/*模块名*/
const moduleName = "enterprise-cp11-cp11";
/* 查询框store名 */
const queryFormKeyName = "formQueryData";

export default {
  components: { ViewSub, ViewMain },
  mixins: [mixinsBasePage, mixinsMainPage],
  provide() {
    const ps11 = this;
    return {
      ps11,
    };
  },
  data() {
    var _this = this;
    return {
	  title:null,
      loding: false,
      dialogFormVisible: false,
      centerDialogVisible: false,
      loading: false,
      fileList: [],
      fileList2: [],
      moduleName,
      queryFormKeyName,
      formDataQuery: { ...defaultFormQueryData },
      /*列表外操作*/
      configsBtns: {
        btnType: BTN_GROUP_TYPE.Btn,
        btnList: [
          {
            text: "导入校验",
            click: () => _this.importExcel(),
          },
          {
            text: "保存数据",
            textLoading: "正在提交...",
            click: () => _this.saveDate(),
          },
        ],
      },
      configsBtns1: {
        
      },
	  configsBtns2: {
		    
	    },
      /*列表内操作按钮*/
      configsListBtns: {
       
      },
      configsMainPage: { moduleName, state },
      configsCFormQuery: {
        moduleName,
        keyForm: queryFormKeyName, //在state里面对应的表单索引字段
        DSLFormItems: DSLFormItemsQuery, //查询表单的查询项描述
      },
      DSLTableHeader,
      moduleOptions: {
        getDataListURL: MODULE_BASE_URL + "/page",
        getDataListIsPage: true,
        exportURL: MODULE_BASE_URL + "/export",
        deleteURL: MODULE_BASE_URL,
        deleteIsBatch: true,
        createdIsNeed: false
      },
	  //超过的大小图
	  biging:[],
	  isshow:false,
	  isbig:false,
	  isbow:false,
	  isfive:false
    };
  },
  methods: {

    handleChange1: function (file, fileList) {
      this.fileList = fileList;
    },
    submitUpload: function () {
  
      var _this = this;
      if (_this.fileList.length < 1) {
        return _this.$message.error("请选择需要上传的文件");
      }
     
      if (
        [
          "application/vnd.ms-excel",
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        ].indexOf(_this.fileList[0].raw.type) < 0
      ) {
        return _this.$message.error("只能上传xlsx");
      }
      var formData = new FormData();
      // formData.append("tm0101", _this.formDataQuery.tm0101);
      formData.append("file", _this.fileList[0].raw);
      _this.loading = true;

      this.$http({
        url: "/enterprise/cp11/importCp01",
        method: "post",
        data: formData,
      })
        .then(function (res) {
          _this.loading = false;
          _this.dialogFormVisible = false;
          _this.formDataQuery.cp1101 = res.data.data;
          if (res.data.data === "error") {
            alert("导入数据不合规");
          } else if (res.data.data === "notData") {
            _this.formDataQuery.cp1101 = "";
            alert("数据导入条数为0，请检查导入数据格式");
          } else {
            alert(res.data.msg);
          }
          _this.getTableList();
        })
        .catch(function (error) {
          _this.loading = false;
          _this.$message.error(error);
        });
    },
    downTemplate() {
      exportHandler(
        "/enterprise/ps11/export",
        window._.merge({ ...this.formDataQuery }),
        "批量导入企业信息"
      );
    },
    importExcel() {
      this.fileList = [];
      this.dialogFormVisible = true;
    },
   async saveDate() {
        if (
        this.formDataQuery.cp1101 == null ||
        this.formDataQuery.cp1101 == ""
      ) {
        return this.$message.error("请先导入检验");
      }  

      let loadingInstance1 = Loading.service({ fullscreen: true });
     let { data: res } = await this.$http.get(
        "/enterprise/cp11/saveDate/" + this.formDataQuery.cp1101
      ); 
    
      try {
            SuccessOrFailed(
              res,
              {
                /*成功 没有msg属性则不弹出提示信息*/
                fn: () => {
                  alert(res.msg)
                },
              },
              /*失败 msg必填*/
            {
                fn: ()=>{
                  alert(res.msg)
                }
             }
          )
      } catch (error) {
        
      } finally{
        this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
          loadingInstance1.close();
        });

        this.getTableList()
      }
    },
   // 选择文件时，往fileList里添加
    handleChange2(fileList) {
      this.fileList2.push(fileList);
	  this.fileList2.forEach((file) => {
	    let max_size = 2048
	    if(file.raw.size > max_size * 1024){
		    this.biging.push(file)
		    this.isshow = true
			this.isbow = true
	    }
	  });
	  this.fileList2.forEach((item,index)=>{
	    this.biging.forEach((item1,index1)=>{
		    if(item.uid == item1.uid){
			    this.fileList2.splice(index,this.biging.length)
		    }
	    })
	  })
	  if(this.fileList2.length > 50){
		  // this.fileList3 = this.fileList2.slice(0, 50)
		 this.isbow = false
		 this.isfive = true
		 this.centerDialogVisible = false
	  }
    },
    //上传提示框取消确定
	getupload(){
		this.isshow = false
		this.isfive = false
	},
	handleClose(){
		this.isshow = false
		this.isfive = false
	},
    // 移除文件
    removeFile(file) {
      // 移除文件时，要重新给fileList赋值
      let arr = [];
      for (let i = 0; i < this.fileList.length; i++) {
        if (this.fileList2[i].uid !== file.uid) {
          arr.push(this.fileList[i]);
        }
      }
      this.fileList = arr;
    }
   },
};
</script>

<style lang="less" scoped>
/deep/.el-dialog.is-fullscreen {
  width: 35%;
  margin-top: 0;
  margin-bottom: 0;
  height: 60%;
  overflow: auto;
}
</style>
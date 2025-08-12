<template>
  <PageLoading
    v-if="loading"
    id="root-element"
    :title="'loading'"
    key="loading"
  />
  <div
    key="main"
    v-else
    id="root-element"
    v-loading.fullscreen.lock="loading"
    :element-loading-text="$t('loading')"
    :class="['app-wrapper', { 'app-sidebar--fold': $store.state.sidebarFold }]"
  >
    <template v-if="!loading">
      <main-navbar />
      <main-sidebar />
      <div class="app-content__wrapper">
        <main-content v-if="!$store.state.contentIsNeedRefresh" />
      </div>
    </template>

    
<el-dialog
    title="提示"
    :visible.sync="dialogVisible"
    width="30%"
    :show-close="false"
    :close-on-press-escape="false"
    :close-on-click-modal="false"
    >
    <span>{{msg}}</span>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="gotoCp03">确 定</el-button>
    </span>
</el-dialog>
 
  </div>
</template>

<script lang="ts">
import MainNavbar from "./main-navbar.vue";
import MainSidebar from "./main-sidebar.vue";
import MainContent from "./main-content.vue";
import debounce from "lodash/debounce";
import {
  changeStyle,
  checkAreaCodeDB,
  getSysDictTypeAll,
} from "@/utils/index.ts";
import { SITE_CONFIG } from "@/main.config.ts";
import ls from "@/utils/localStorageHelper";
import { LS_KEY } from "@/components/Types";
import { API_URL } from "@/main.config.ts";
import Vue from "vue";
import { setKVCacheValue } from "@/utils/dataBaseTable/KeyValueCache";
import PageLoading from "@/components/common/PageLoading";
import checkContractorsInfo from "@/views/modules/enterprise/cp02/cp02.vue";

export default Vue.extend({
  components: {
    PageLoading,
    MainNavbar,
    MainSidebar,
    MainContent,

    checkContractorsInfo,

  },
  data() {
    return {
      loading: true,
      yjContent: "",
      yjDialogVisible: false,
      isMoveIn: 2,
      isShowJump: false,
      isShowCheck: false,
      dialogVisible: false,
      msg:'',
      firstStepIsWrong: false,
      companyKey: "0b7688287dacecbf2e09dfaf9f8b48a7",
      encrypted: "",
      dialogTitle: "完善信息",
      checkData: [
        {
          sort: "1",
          name: "isProjectInfoComplete",
          msg: "项目信息未完善，请完善项目信息",
          isFinish: true,
        },
        {
          sort: "2",
          name: "isCompanyInfoComplete",
          msg: "参建单位信息未完善，请完善参建单位信息",
          isFinish: true,
        },
        {
          sort: "3",
          name: "isPayrollAccount",
          msg: "工资专户信息未完善，请完善工资专户信息",
          isFinish: true,
        },
      ],
      isShowCheckBox: false,
      dialogWidth: "40%",
    };
  },
  watch: {
    $route: "routeHandler",
  },
  provide() {
    return {
      // 刷新
      refresh() {
        this.$store.state.contentIsNeedRefresh = true;
        this.$nextTick(() => {
          this.$store.state.contentIsNeedRefresh = false;
        });
      },
    };
  },
  async created() {
    try {
      window.Vuex.$on("resize", () => {
        changeStyle();
        this.handleWindowResize();
      });

      window.tabRemoveHandle = (tabName) => {
        if (tabName === "home") {
          return false;
        }
        this.$store.state.contentTabs = this.$store.state.contentTabs.filter(
          (item) => item.name !== tabName
        );
        if (this.$store.state.contentTabs.length <= 0) {
          this.$store.state.sidebarMenuActiveName =
            this.$store.state.contentTabsActiveName = "home";
          return false;
        }
        // 当前选中tab被删除
        if (tabName === this.$store.state.contentTabsActiveName) {
          let tab =
            this.$store.state.contentTabs[
              this.$store.state.contentTabs.length - 1
            ];
          this.$router.push({
            name: tab.name,
            params: { ...tab.params },
            query: { ...tab.query },
          });
        }
      };
      this.handleWindowResize();
      window.addEventListener(
        "resize",
        debounce(() => {
          window.Vuex.$emit("resize");
        }, 300)
      );
      this.routeHandler(this.$route, { created: true });
      /* setTimeout(() => {
        if (this.loading) {
          window.location.reload();
        }
      }, 1000 * 3); */
      // 获取字典列表, 添加并全局变量保存
      await Promise.all([
        this.getPermissions(),
        this.getUserInfo(),
        checkAreaCodeDB(),
      ]);
    } catch (error) {
      console.error(error);
    } finally {
      this.loading = false;
    }
  },
  mounted () {
    // 检查用工缺工是否填写
    this.checkMall();
  },
  methods: {
    gotoCp03(){
      this.dialogVisible = false
      this.$router.push({
        name: 'enterprise-cp03-cp03',
        query:{
          v:'add'
        }
      });
    },
    // 窗口改变大小
    handleWindowResize() {
      // if (this.$store.state.contentTabsActiveName==='home') {
      //    this.$store.state.sidebarFold =true
      // }else{
      this.$store.state.sidebarFold =
        document.documentElement["clientWidth"] <= 992 || false;
      // }
    },
    // 路由, 监听
    routeHandler(route, configs = {}) {
      // if (route.name==='home') {
      //   this.$store.state.sidebarFold = true
      // }else{
      this.$store.state.sidebarFold =
        document.documentElement["clientWidth"] <= 992 || false;
      // }
      if (!route.meta.isTab) {
        return false;
      }
      var tab = this.$store.state.contentTabs.filter(
        (item) => item.name === route.name
      )[0];

      if (!tab) {
        tab = {
          ...SITE_CONFIG["contentTabDefault"],
          ...route.meta,
          name: route.name,
          params: { ...route.params },
          query: { ...route.query },
        };
        /* 页面刷新，以示路径不同使subPage 根据id重新获取数据 */
        if (configs.created) {
          tab.query._t = Date.now();
        }
        this.$store.state.contentTabs =
          this.$store.state.contentTabs.concat(tab);
      }
      this.$store.state.sidebarMenuActiveName = tab.menuId;
      this.$store.state.contentTabsActiveName = tab.name;
    },
    // 获取当前管理员信息
    async getUserInfo() {
      try {
        let { data: res } = await this.$http.get(API_URL.userInfo);
        if (res.code !== 0) {
          this.$message.error(res.msg);
        } else {
          ls.set(LS_KEY.user, res.data);
          this.$store.state.user.id = res.data.id;
          this.$store.state.user.name = res.data.username;
          this.$store.state.user.realName = res.data.realName;
          this.$store.state.user.superAdmin = res.data.superAdmin;
          this.$store.state.user.userType = res.data.userType;
          this.$store.state.user.roleIdList = res.data.roleIdList;
          this.$store.state.dp_pj0101 = res.data.pj0101;
          this.encrypted = res.data.code;
          if (res.data.roleIdList.includes("1254300243697975297")) {
            this.isShowJump = true;
            if (!sessionStorage.getItem("isLookYj")) {
              this.refreshYj();
            }
            if (!this.$store.state.isAllFinish) {
              this.refreshCheck();
            }
          }
        }
      } catch (error) {
        console.error(error);
      }
    },
    /*获取权限*/
    async getPermissions() {
      try {
        let { data: res } = await this.$http.get(API_URL.permissions);
        if (res.code !== 0) {
          this.$message.error(res.msg);
        } else {
          SITE_CONFIG.permissions = res.data;
        }
      } catch (error) {
        console.error(error);
      }
    },
    mouseover() {
      this.isMoveIn = 0;
    },
    mouseout() {
      this.isMoveIn = 1;
    },
    goToExternalLinks() {
      window.open(
        `https://103.203.218.143:8001/statics/base/page.html?page=p.a.CNavQ&encryptedCode=${this.encrypted}&companyKey=${this.companyKey}`,
        // `http://192.168.0.108:8001/statics/base/page.html?page=p.a.CNavQ&encryptedCode=${this.encrypted}&companyKey=d267d066d9087c10a9a8b55a117d96a788`,
        // `http://scnmgappcs.xinyegk.com/statics/base/page.html?page=p.a.CNavQ&encryptedCode=${this.encrypted}&companyKey=${this.companyKey}`,
        "_blank"
      );
    },
    async checkProjectInfo() {
      let { data: res } = await this.$http.get("common/isProjectInfoComplete");
      if (!res) {
        this.checkData[0].isFinish = false;
        return;
      }
    },
    // async checkMall() {
    //   let { data: res } = await this.$http.get("/enterprise/cp03/mail");
    //   if (res.data == '1') {
    //     this.dialogVisible = true;
    //     this.msg = res.msg;
    //   }
    // },
    async checkContractorsInfo() {
      let { data: res } = await this.$http.get("common/isCompanyInfoComplete");
      if (!res) {
        this.checkData[1].isFinish = false;
        return;
      }
    },
    async checkSpecialAccountInfo() {
      let { data: res } = await this.$http.get("common/isPayrollAccount");
      if (!res) {
        this.checkData[2].isFinish = false;
        return;
      }
    },
    changeFinish(sort) {
      this.checkData = this.checkData.map((item) => {
        if (item.sort == sort) {
          item.isFinish = true;
        }
        return item;
      });
      this.dialogWidth = "40%";
      this.refreshCheck();
    },
    changeCheckBox(sort, fistIsFinish) {
      if (fistIsFinish === "finished") {
        this.firstStepIsWrong = false;
      }
      if (sort && sort != 1 && this.firstStepIsWrong) {
        this.$alert("请先完善项目信息，再进行后续的操作！", "提示", {
          confirmButtonText: "确定",
          center: true,
          type: "warning",
          callback: (action) => {
            return false;
          },
          showClose: false,
        });
        this.dialogWidth = "40%";
        return false;
      } else {
        this.isShowCheckBox = sort;
      }
      if (sort) {
        this.dialogWidth = "80%";
      } else {
        this.dialogWidth = "40%";
      }
    },
    async refreshCheck() {
      try {
        // 校验项目信息，参建单位，专户信息
        await Promise.all([
          this.checkProjectInfo(),
          this.checkContractorsInfo(),
          this.checkSpecialAccountInfo(),
        ]);
        if (this.checkData.length > 0) {
          this.checkData.filter((item) => {
            if (item.name == "isProjectInfoComplete" && !item.isFinish) {
              this.firstStepIsWrong = true;
            }
          });
          let isShowCheck = false;
          this.checkData.some((item) => {
            if (item.isFinish == false) {
              isShowCheck = true;
            }
          });
          this.isShowCheck = isShowCheck;
          this.$store.state.isAllFinish = !isShowCheck;
        } else {
          this.isShowCheck = false;
        }
      } catch (error) {
        console.error(error);
      } finally {
      }
    },
    async refreshYj() {
      try {
        // 校验项目信息，参建单位，专户信息
        let { data: res } = await this.$http.get(
          "/enterprise/yj01/getInfoContent"
        );
        if (res.data) {
          this.yjContent = res.data;
          this.yjDialogVisible = true;
        }
      } catch (error) {
        console.error(error);
      } finally {
      }
    },
    yjBack() {
      this.yjDialogVisible = false;
      sessionStorage.setItem("isLookYj", "true");
    },
  },
});
</script>
<style lang="scss" scoped>
::v-deep .yj_dialog{
  .el-dialog__title{
    font-weight: 600;
  }
   .el-dialog__body{
     padding-top: 0;
  }
}
@-webkit-keyframes moveIn {
  0% {
    width: 0;
    opacity: 1;
  }
  100% {
    width: 460px;
    opacity: 1;
  }
}
@keyframes moveIn {
  0% {
    width: 0;
    opacity: 1;
  }
  100% {
    width: 460px;
    opacity: 1;
  }
}
@-webkit-keyframes moveOut {
  0% {
    width: 460px;
    opacity: 1;
  }
  100% {
    width: 0;
    opacity: 1;
  }
}
@keyframes moveOut {
  0% {
    width: 460px;
    opacity: 1;
  }
  100% {
    width: 0;
    opacity: 1;
  }
}
.jumpBox {
  display: flex;
  flex-flow: row;
  align-items: center;
  width: auto;
  position: absolute;
  right: -40px;
  bottom: 40vh;
  cursor: pointer;
  z-index: 200;
}
.jumpText {
  width: 0;
  overflow: hidden;
  height: 60px;
  background: #fff;
  font-size: 28px;
  line-height: 60px;
  padding: 0 20px;
  border-radius: 10px 0 0 10px;
}
.moveIn {
  -webkit-animation: moveIn 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  animation: moveIn 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}
.moveOut {
  -webkit-animation: moveOut 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  animation: moveOut 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}
::v-deep .el-dialog__body {
  max-height: 68vh;
  overflow: auto;
}
.main-page,
.sub-page {
  height: 100% !important;
}

.result {
  display: flex;
  flex-flow: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  .result-icon {
    font-size: 50px;
  }
  .result-text {
    height: 10vh;
    padding: 10px;
  }
}
</style>

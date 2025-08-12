<template>
  <MainPage
    :configs="configsMainPage"
    class="dev-demo"
  >
    <div class="nav-bar">
      <div>
        <router-link to="/dev/doc">
          <el-button
            type="primary"
            class="btn-switch"
          >
            文档
          </el-button>
        </router-link>
        <router-link :to="currentView==='sub'?'/dev/demo':'/dev/demo?v=sub'">
          <el-button
            type="primary"
            class="btn-switch"
          >
            {{ currentView==='sub'?'列表页':'详情页——可编辑' }}
          </el-button>
        </router-link>
        <router-link :to="'/dev/demo?v=detail'">
          <el-button
            type="primary"
            class="btn-switch"
          >
            只查看，无编辑
          </el-button>
        </router-link>
        <router-link :to="'/dev/demo?v=TestScrollPage'">
          <el-button
            type="primary"
            class="btn-switch"
          >
            TestScrollPage
          </el-button>
        </router-link>
      </div>
    </div>
    <div
      v-show="currentView==='main'"
      style=" flex: 1; height: 1px; overflow: auto; "
    >
      <div>
        tipsTime {{ tipsTime }}
        <el-button @click="logs=[]">
          clear logs
        </el-button>
        <input v-model="tipsMsg">
        <ul class="log-wrapper">
          <li
            v-for="(log, index) in logs"
            :key="index"
          >
            {{ log }}
          </li>
        </ul>
      </div>
      <div style="overflow:auto">
        <el-divider>展示项</el-divider>
        <el-switch
          v-model="isShowTipsDemo"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-text="show"
          inactive-text="hidden"
        />
        <div class="tips-show">
          <pre
            v-if="isShowTipsDemo"
            ref="tips"
            v-tips
            :data-time="tipsTime"
            :data-tips="tipsMsg"
          >
          v-tips
          </pre>
        </div>
        <div class="demo-wrapper">
          <Btnmini
            v-for="(config,index) in configsBtnmini"
            :key="index"
            :configs="config"
          />
        </div>
        <Card header="图片预览">
          <el-button @click="showImg">
            showImg
          </el-button>
        </Card>
        <Card header="Viewer">
          <!--文件预览-->
          {{ filesArray }}
          <Viewer
            v-model="filesArray"
            :configs="configsViewer"
          />
        </Card>
        <Card>
          <!-- {{ dataDynamicList }} -->
          <DynamicRowList
            v-model="dataDynamicList"
            :options="configsDynamicRowList"
          />
        </Card>
      </div>
    </div>
    <!-- MainView End -->
    <DevDemoSub
      v-if="currentView==='sub'"
      ref="addOrUpdate"
      @refreshDataList="getTableList"
    />
    <DevDemoSub
      v-if="currentView==='detail'"
      ref="addOrUpdate"
      @refreshDataList="getTableList"
    />
    <TestScrollPage
      v-if="currentView==='TestScrollPage'"
      ref="addOrUpdate"
      @refreshDataList="getTableList"
    />
  </MainPage>
</template>
<script>
import { state, defaultFormQueryData } from "./state";
import { DSLTableHeader } from "./dsl";

import { BTN_TYPE, STATUS_TYPE } from "@/components/Types";
import mixinsBasePage from "@/mixins/basePage.ts";
import mixinsMainPage from "@/mixins/mainPage.ts";
import DevDemoSub from "./DevDemoSub";
import { required } from "@/utils/formRule";
import { makeViewerItem } from "@/components/Form/Viewer";
import { readCard } from "@/utils/idCardReader";
import { msgChannel } from "@/utils/msgChannel";

const moduleName = "DevDemo";

import TestScrollPage from "./TestScrollPage";

export default {
  name: moduleName,
  components: { DevDemoSub, TestScrollPage },
  mixins: [mixinsBasePage, mixinsMainPage],
  data() {
    const deleteHandle = () => {
      this.$notify({
        title: "警告",
        message: "这是一条警告的提示消息",
        type: STATUS_TYPE.warning,
      });
    };
    return {
      msg: "",
      input2: "",
      filesArray: [],
      moduleOptions: {
        /* 船检师不调用query方法 */
        createdIsNeed: false,
      },
      moduleName,
      dataDynamicList: (() => {
        return [...new Array(3)].map((_, i) => ({
          name: "name__" + i,
          industry: "industry__" + i,
          areacode: "areacode__",
          linkman: "linkman__",
          linkphone: "linkphone__",
          status: Number(i) % 2,
        }));
      })(),
      positionPickerValue: "",
      logs: [],
      isShowTipsDemo: true,
      vTipsInfo: {},
      tipsTime: Date.now(),
      tipsMsg: "需要的提示信息",
      configsDynamicRowList: {
        DSLTableHeader: [...DSLTableHeader],
        /* 必填，否则不能再增加新的行 */
        rules: {
          name: [required],
        },
      },
      configsBtnmini: [
        // {
        //   type: BTN_TYPE.delete,
        //   shiro: "enterprise:pj06:delete",
        //   text: "删除",
        //   click: deleteHandle,
        // },
        {
          type: BTN_TYPE.add,
          shiro: "enterprise:pj06:delete",
          text: "添加",
          click: deleteHandle,
        },
      ],
      /* MainPage state 注册模块状态 */
      configsMainPage: {
        moduleName,
        state,
      },
      configsViewer: {
        fileType: "001",
        shiro: "enterprise:pj06:delete",
      },
    };
  },
  computed: {
    ...mapState({
      formData: (state) => state.DevDemo.formData,
    }),
  },
  watch: {
    input2(val) {
      this.ports.forEach((port) => port.postMessage(val));
    },

    ["$store.state.DevDemo.formData.memo"](memo) {
      this.logs.unshift("memo" + memo);
    },
  },
  mounted() {
    /* 
    const porta1 = msgChannel(
      "iframe",
      {
        src: "http://localhost:5000/mcc",
        style: `
              position: fixed; 
              top: 100px; 
              background: white; 
              right: 0; 
              height: 300px; 
              width: 50%; 
              z-index: 23; 
        `,
      },
      {
        onmessage: (e) => {
          this.msg = `iframe: ${Date.now()}`;
        },
      }
    );

    const porta2 = msgChannel(
      "iframe2",
      {
        src: "http://localhost:5000/mcc",
        style: `
              position: fixed; 
              top: 300px; 
              background: grey; 
              right: 0; 
              height: 300px; 
              width: 50%; 
              z-index: 23; 
        `,
      },
      {
        onmessage: (e) => {
          this.msg = `2: ${JSON.stringify(e.data)}`;
        },
      }
    );
    this.ports = [porta1, porta2];

    setInterval(() => {
      this.tipsTime = Date.now();
    }, 5000);
   */
  },
  methods: {
    async getIDCardInfo() {
      const res = await readCard();
      console.log("getIDCardInfo -> res", res);
    },
    showImg() {
      this.$viewer(
        makeViewerItem(
          [
            "https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg",
            "https://fuss10.elemecdn.com/0/6f/e35ff375812e6b0020b6b4e8f9583jpeg.jpeg",
            "https://www.baidu.com",
          ].map((src, index) => {
            return {
              viewType: index !== 2 ? "img" : "pdf",
              name: "test",
              src,
              id: `id_${index}`,
            };
          })
        ),
        1
      );
    },
    handleDynamic(handleDynamic) {
      console.log(
        "handleDynamic -> handleDynamic",
        JSON.stringify(handleDynamic, null, 2)
      );
    },
    async handleFormSubmit() {
      return new Promise((resolve) => setTimeout(resolve, 1000 * 2));
    },
  },
};
</script>
<style lang="scss" scoped>
.dev-demo {
  height: 100vh;
  overflow: hidden;

  .nav-bar {
    width: 100%;
    height: 54px;
    display: flex;
    flex-flow: row nowrap;
    align-items: center;
    @include elevation4;

    .btn-switch {
      margin-left: 12px;
    }
  }
}

.demo-wrapper {
  margin: 12px;
}

.log-wrapper {
  max-height: 600px;
  overflow: auto;
}
</style>
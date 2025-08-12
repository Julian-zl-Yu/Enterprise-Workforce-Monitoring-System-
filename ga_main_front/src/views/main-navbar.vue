<template>
  <nav
    :class="`app-navbar--${$store.state.navbarLayoutType} elevation1`"
    class="app-navbar"
  >
    <div class="app-navbar__body line">
      <h1 class="app-navbar__brand">
        <a class="app-navbar__brand-lg">{{ $t("brand.lg") }}</a>
      </h1>
      <div style="flex: 1" />
      <div class="app-navbar__wrapper">
        <!--  <div class="app-navbar__menu">
          <div class="organization">
            单位名称
          </div>
        </div> -->
        <div class="app-navbar__menu options">
          <a class="app-navbar__item">
            <el-badge v-model="unReadCount">
              <a style="color:#fff;margin-right:10px" @click="myNoticeRouter"
                >消息</a
              >
            </el-badge>
          </a>
          <a
            class="app-navbar__item"
            target="_blank"
            @click="JumpFAQ"
            rel="noopener noreferrer"
            style="display: none;"
          >
            <i class="el-icon-question" />常见问题
          </a>
          <a class="app-navbar__item">
            <svg class="icon-svg app-navbar__icon-menu" aria-hidden="true">
              <use xlink:href="#icon-idcard" />
            </svg>
            {{ $store.state.user.realName }}
          </a>
          <a class="app-navbar__item" @click="updatePasswordHandle">
            <svg class="icon-svg app-navbar__icon-menu" aria-hidden="true">
              <use xlink:href="#icon-key" />
            </svg>
            {{ $t("updatePassword.title") }}
          </a>
          <a class="app-navbar__item" @click="logoutHandler">
            <svg
              class="icon-svg app-navbar__icon-menu"
              style="
                vertical-align: middle;
                fill: currentColor;
                overflow: hidden;
              "
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="1593"
            >
              <path
                d="M599.36 643.36V786.4c0 17.792-14.304 32.224-31.936 32.224H114.88c-17.632 0-31.936-14.432-31.936-32.224V237.6c0-17.792 14.304-32.224 31.936-32.224h452.544c17.632 0 31.936 14.432 31.936 32.224v143.04h-64.544v-78.048c0-13.568-10.72-24.608-23.936-24.608H171.488c-13.248 0-23.968 11.008-23.968 24.608v418.816c0 13.568 10.72 24.576 23.968 24.576H510.88c13.216 0 23.936-11.008 23.936-24.576V643.36h64.544z m295.392-163.936H322.464v72.384h572.288v-72.352z m46.336 28.992L752 319.296l-55.52 55.52L885.6 563.904l55.488-55.488z m-55.52-52.448L692.352 649.184l55.52 55.52 193.184-193.216-55.488-55.52z"
                p-id="1594"
              />
            </svg>
            {{ $t("logout") }}
          </a>
        </div>
      </div>
    </div>
    <!-- 弹窗, 修改密码 -->
    <update-password v-if="updatePasswordVisible" ref="updatePassword" />
  </nav>
</template>
<script>
import screenfull from "screenfull";
import UpdatePassword from "./main-navbar-update-password";
import { clearLoginInfo } from "@/utils/index.ts";
import { SITE_CONFIG } from "@/main.config";

export default {
  inject: ["refresh"],
  components: {
    UpdatePassword,
  },
  data() {
    return {
      updatePasswordVisible: false,
      messageTip: false,
      ws: null,
      unReadCount: 0,
    };
  },
  watch: {
    "$store.state.isNoticeRefresh": {
      handler: function(val, oldVal) {
        this.getUnReadCount();
      },
      // 深度观察监听
      deep: true,
    },
  },
  created() {
    // let socket = new WebSocket(`${SITE_CONFIG["socketURL"]}`);
    // socket.onopen = function() {};
    // socket.onerror = () => {
    //   this.$notify.error({
    //     title: this.$t("notice.disconnect"),
    //     message: this.$t("notice.disconnectMessage"),
    //   });
    // };
    // socket.onmessage = (event) => {
    //   const result = JSON.parse(event.data);
    //   // 如果是有新文本通知，则提示有新通知
    //   if (result.type === 0) {
    //     this.messageTip = true;
    //     this.$notify({
    //       title: this.$t("notice.new"),
    //       message: result.msg,
    //       type: "info",
    //       duration: 5000,
    //     });
    //   }
    // };
    this.websocket();
    // 未读通知数
    this.getUnReadCount();
  },
  methods: {
    websocket() {
      //建立socket通道
      //process.env.VUE_APP_URL为服务端地址
      //code为自定义的参数，主要是用于服务端标识当前会话是哪个用户
      this.ws = new WebSocket(`${SITE_CONFIG["socketURL"]}`);
      //socket连接成功后的回调函数
      this.ws.onopen = () => {
        console.log("websocket连接成功！");
        //若项目中没有使用nginx转发请求则忽略这步
        //设置定时器，每过55秒传一次数据
        //以防长时间不通信被nginx自动关闭会话
        //也可以通过修改nginx配置文件延长关闭时间
        setInterval(() => {
          this.keepAlive(this.ws);
        }, 6000 * 100);
      };
      //接收来自服务端消息的回调函数
      this.ws.onmessage = (evt) => {
        var notices = JSON.parse(evt.data);
        this.getUnReadCount();
      };
      //关闭socket的回调函数
      this.ws.onclose = () => {
        // 关闭 websocket
        console.log("连接已关闭...");
      };
    },
    //持续向后台发送消息，用于维护socket通道不被nginx关闭
    keepAlive(webSocket) {
      if (webSocket) {
        if (webSocket.readyState == webSocket.OPEN) {
          webSocket.send("");
        }
      }
    },
    myNoticeRouter() {
      this.$router.replace("notice-notice-user");
    },
    // 跳转常见问题
    JumpFAQ() {
      const { href } = this.$router.resolve({
        name: "FAQ",
      });
      window.open(href, "_blank");
    },
    getUnReadCount() {
      this.$http
        .get("/sys/notice/mynotice/unread")
        .then(({ data: res }) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg);
          }else{
            this.unReadCount = res.data;
          }
        })
        .catch((error) => console.error(error));
    },
    // 全屏
    fullscreenHandle() {
      if (!screenfull.enabled) {
        return this.$message({
          message: this.$t("fullscreen.prompt"),
          type: "warning",
          duration: 500,
        });
      }
      screenfull.toggle();
    },
    // 修改密码
    updatePasswordHandle() {
      this.updatePasswordVisible = true;
      this.$nextTick(() => {
        this.$refs.updatePassword.init();
      });
    },
    // 退出
    async logoutHandler() {
      try {
        await this.$confirm(
          this.$t("prompt.info", { handle: this.$t("logout") }),
          this.$t("prompt.title"),
          {
            confirmButtonText: this.$t("confirm"),
            cancelButtonText: this.$t("cancel"),
            type: "warning",
          }
        );
        let { data: res } = await this.$http.post("/logout");
        if (res.code !== 0) {
          return this.$message.error(res.msg);
        }
        clearLoginInfo();
        this.$router.push({ name: "login" });
      } catch (error) {
        console.error(error);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.app-navbar__body {
  &.line {
    padding: 0 12px;
    background: linear-gradient(90deg, #ffffff, #4378ad);
  }
  .app-navbar__wrapper {
    .app-navbar__menu {
      a.app-navbar__item {
        color: white;
        &:hover {
          transform: scale(1.2);
          cursor: pointer;
        }
      }
    }
  }
}

.app-navbar__brand {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 5px;
  margin: 0;
  height: 100%;
  font-size: 20px;
  text-transform: uppercase;
  white-space: nowrap;
  color: $--color-primary;
  overflow: hidden;
  transition: width 0.3s;
}
.app-navbar__wrapper {
  display: flex;
  flex-flow: column nowrap;
  justify-content: space-around;
  align-items: flex-end;
  .app-navbar__menu {
    display: flex;
    flex-flow: row nowrap;
    justify-content: space-around;
    align-items: center;
    &.options {
    }
    .organization {
      margin: 0 12px;
      font-size: 18px;
    }
    .app-navbar__item {
      margin: 0 12px;
      &:hover {
        cursor: pointer;
      }
    }
  }
}
</style>

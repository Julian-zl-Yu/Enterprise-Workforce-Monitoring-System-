<template>
  <div class="login-wrapper">
    <section class="header elevation1">
      <header>
        <span class="icon" />
        <span class="title">{{ $t("brand.lg") }}</span>
      </header>
    </section>
    <section class="main">
      <keep-alive>
        <router-view />
      </keep-alive>
    </section>
    <footer>
      <div style="margin:0 auto;display: flex;
        flex-flow: row; display: none;">
        <div>
          <p>
            Copyright xinyegk.com
            <a style="color:#fff" href="http://xinyegk.com/"
              target="_blank">四川信业金键信息技术有限责任公司</a
            >
            版权所有 All Rights Reserved
          </p>
          <p>公司地址：成都市天府大道南段846号天府创新中心202-204 <a style="color:#fff" target="_blank" href="https://beian.miit.gov.cn/">蜀ICP备19004125号</a></p>
        </div>
        <div style="display: flex;margin-left:20px">
          <p>联系我们：</p>
          <p>028-63828995<br /><br />400-668-0289</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import { state } from "./Register/state";
import { state as state1 } from "./ResetPassword/state";

export default {
  name: "Login",
  data() {
    return {
      state,
      state1,
      isShow: false,
    };
  },
  computed: {},
  destroyed() {
    if (this.$store.hasModule("register")) {
      try {
        this.$store.unregisterModule("register");
      } catch (error) {
        console.error(error);
      }
    }

    if (this.$store.hasModule("resetPassword")) {
      try {
        this.$store.unregisterModule("resetPassword");
      } catch (error) {
        console.error(error);
      }
    }
  },
  created() {
    if (!this.$store.hasModule("register")) {
      try {
        this.$store.registerModule("register", state);
      } catch (error) {
        console.error(error);
      }
    }
    if (!this.$store.hasModule("resetPassword")) {
      try {
        this.$store.registerModule("resetPassword", state1);
      } catch (error) {
        console.error(error);
      }
    }
  },
  methods: {
   
  },
};
</script>

<style lang="scss" src="./Login.scss"></style>

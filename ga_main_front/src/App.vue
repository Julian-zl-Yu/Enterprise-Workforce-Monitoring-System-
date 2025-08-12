<template>
  <transition name="el-fade-in-linear">
    <router-view :class="'route-name-' + $route.name" />
  </transition>
</template>

<script lang="ts">
import Cookies from "js-cookie";
import { messages } from "@/i18n";
import Vue from "vue";

export default Vue.extend({
  watch: {
    "$i18n.locale": "i18nHandle",
  },
  created() {
    this.i18nHandle(this.$i18n.locale);
  },
  methods: {
    i18nHandle(val, oldVal) {
      Cookies.set("language", val);
      document.querySelector("html").setAttribute("lang", val);
      document.title = messages[val].brand.lg;
      // 非登录页面，切换语言刷新页面
      if (this.$route.name !== "login" && oldVal) {
        window.location.reload();
      }
    },
  },
});
</script>

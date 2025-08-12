<template>
  <main :class="['app-content', { 'app-content--tabs': $route.meta.isTab }]">
    <!-- tab展示内容 -->
    <template v-if="$route.meta.isTab">
      <el-dropdown class="app-content--tabs-tools">
        <i class="el-icon-arrow-down" />
        <el-dropdown-menu
          slot="dropdown"
          :show-timeout="0"
        >
          <el-dropdown-item
            @click.native="tabRemoveHandle($store.state.contentTabsActiveName)"
          >
            {{ $t("contentTabs.closeCurrent") }}
          </el-dropdown-item>
          <el-dropdown-item @click.native="tabsCloseOtherHandle()">
            {{ $t("contentTabs.closeOther") }}
          </el-dropdown-item>
          <el-dropdown-item @click.native="tabsCloseAllHandle()">
            {{ $t("contentTabs.closeAll") }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <el-tabs
        v-model="$store.state.contentTabsActiveName"
        @tab-click="tabSelectedHandle"
        @tab-remove="tabRemoveHandle"
        @click.native.right="showTabMenue"
      >
        <el-tab-pane
          v-for="item in $store.state.contentTabs"
          :key="item.name"
          :name="item.name"
          :label="item.title"
          :closable="item.name !== 'home'"
          :class="{ 'is-iframe': tabIsIframe(item.iframeURL) }"
        >
          <template v-if="item.name === 'home'">
            <svg
              slot="label"
              class="icon-svg app-content--tabs-icon-nav"
              aria-hidden="true"
            >
              <use xlink:href="#icon-home" />
            </svg>
          </template>
          <iframe
            v-if="tabIsIframe(item.iframeURL)"
            :src="item.iframeURL"
            width="100%"
            height="100%"
            :title="item.name"
          />
          <DynamicView
            v-else
            :name="item.name"
          />
        </el-tab-pane>
      </el-tabs>
    </template>
    <!-- 其他方式, 展示内容 -->
    <template v-else>
      <DynamicView :name="'home'" />
    </template>
  </main>
</template>

<script lang="ts">
import { isURL } from "@/utils/validate.ts";
import DynamicView from "@/router/DynamicView.vue";
import Vue from "vue";
export default Vue.extend({
  components: { DynamicView },
  data() {
    return {};
  },
  mounted() {
    setTimeout(() => {
      window.Vuex.$emit("resize");
    }, 1000);
  },
  methods: {
    showTabMenue(e) {
      e.preventDefault();
      e.stopPropagation();
      console.log(e);
    },
    // tabs, 是否通过iframe展示
    tabIsIframe(url) {
      return isURL(url);
    },
    // tabs, 选中tab
    tabSelectedHandle(tab) {
      tab = this.$store.state.contentTabs.filter(
        (item) => item.name === tab.name
      )[0];
      if (tab) {
        if (tab.name === this.$route.name) return;
        this.$router.push({
          name: tab.name,
          params: { ...tab.params },
          query: { ...tab.query },
        });
      }
    },
    // tabs, 删除tab
    tabRemoveHandle(tabName) {
      if (tabName === "home") {
        return false;
      }
      this.$store.state.contentTabs = this.$store.state.contentTabs.filter(
        (item) => item.name !== tabName
      );
      if (this.$store.state.contentTabs.length <= 0) {
        this.$store.state.sidebarMenuActiveName = this.$store.state.contentTabsActiveName =
          "home";
        return false;
      }
      // 当前选中tab被删除
      if (tabName === this.$store.state.contentTabsActiveName) {
        let tab = this.$store.state.contentTabs[
          this.$store.state.contentTabs.length - 1
        ];
        this.$router.push({
          name: tab.name,
          params: { ...tab.params },
          query: { ...tab.query },
        });
      }
    },
    // tabs, 关闭其它
    tabsCloseOtherHandle() {
      this.$store.state.contentTabs = this.$store.state.contentTabs.filter(
        (item) => {
          return (
            item.name === "home" ||
            item.name === this.$store.state.contentTabsActiveName
          );
        }
      );
    },
    // tabs, 关闭全部
    tabsCloseAllHandle() {
      this.$store.state.contentTabs = this.$store.state.contentTabs.filter(
        (item) => item.name === "home"
      );
      this.$router.push({ name: "home" });
    },
  },
});
</script>

<style lang="scss">
</style>



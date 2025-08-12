<template>
  <div>
    <ViewDev />
  </div>
</template>

<script>
import debounce from "lodash/debounce";
import { changeStyle } from "@/utils/index.ts";
import { SITE_CONFIG } from "@/main.config";
import ViewDev from "./devView/ViewDev.vue";

export default {
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
  components: {
    ViewDev,
  },
  data() {
    return {
      loading: true,
    };
  },
  watch: {
    $route: "routeHandler",
  },
  async created() {
    try {
      window.Vuex.$on("resize", () => {
        changeStyle();
        this.handleWindowResize();
      });
      this.handleWindowResize();
      window.addEventListener(
        "resize",
        debounce(() => {
          window.Vuex.$emit("resize");
        }, 300)
      );
      this.routeHandler(this.$route, { created: true });
      await Promise.all([this.getPermissions(), this.getUserInfo()]);
    } catch (error) {
      console.error(error);
    } finally {
      this.loading = false;
    }
  },
  methods: {
    // 窗口改变大小
    handleWindowResize() {
      this.$store.state.sidebarFold =
        document.documentElement["clientWidth"] <= 992 || false;
    },
    // 路由, 监听
    routeHandler(route, configs = {}) {
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
        this.$store.state.contentTabs = this.$store.state.contentTabs.concat(
          tab
        );
      }
      this.$store.state.sidebarMenuActiveName = tab.menuId;
      this.$store.state.contentTabsActiveName = tab.name;
    },
    // 获取当前管理员信息
    async getUserInfo() {
      let { data: res } = await this.$http.get("/sys/user/info");
      if (res.code !== 0) {
        return this.$message.error(res.msg);
      }
      this.$store.state.user.id = res.data.id;
      this.$store.state.user.name = res.data.username;
      this.$store.state.user.superAdmin = res.data.superAdmin;
      this.$store.state.user.userType = res.data.userType;
      this.$store.state.user.roleIdList = res.data.roleIdList;
    }, // 获取权限
    async getPermissions() {
      let { data: res } = await this.$http.get("/sys/menu/permissions");
      if (res.code !== 0) {
        return this.$message.error(res.msg);
      }
      SITE_CONFIG.permissions = res.data;
      // console.log("SITE_CONFIG", JSON.stringify(SITE_CONFIG.permissions,null,2));
    },
  },
};
</script>

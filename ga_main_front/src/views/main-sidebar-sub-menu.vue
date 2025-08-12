<template>
  <el-submenu
    v-if="
      menu.children &&
        menu.children.length >= 1 &&
        menu.whether != '0' &&
        showWhether
    "
    :index="menu.id"
    :popper-append-to-body="false"
  >
    <template slot="title">
      <svg class="icon-svg app-sidebar__menu-icon" aria-hidden="true">
        <use :xlink:href="`#${menu.icon}`" />
      </svg>
      <span>{{ menu.name }}</span>
    </template>
    <sub-menu
      v-for="item in menu.children"
      :key="item.id"
      v-if="item.whether != '0'"
      :menu="item"
    />
  </el-submenu>
  <el-menu-item
    v-else-if="menu.whether != '0' && showWhether"
    :index="menu.id"
    @click="gotorouteHandler(menu.id)"
  >
    <svg class="icon-svg app-sidebar__menu-icon" aria-hidden="true">
      <use :xlink:href="`#${menu.icon}`" />
    </svg>
    <span>{{ menu.name }}</span>
  </el-menu-item>
</template>

<script>
import SubMenu from "./main-sidebar-sub-menu";
import { SITE_CONFIG } from "@/main.config";

export default {
  name: "SubMenu",
  components: {
    SubMenu,
  },
  props: {
    menu: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      arrNode: [],
    };
  },
  computed: {
    showWhether() {
      let falg = false;
      if (this.$store.state.user.superAdmin == 1) {
        if (
          this.menu.children &&
          this.menu.children.length == 0 &&
          this.menu.whether == "0"
        )
          return (this.menu.whether = true);
        let children = this.menu.children; // 子数据
        let translator = (children) => {
          children.forEach((item) => {
            if (item.whether == "0") {
              item.whether = true;
            }
          });
          if (children.children && children.children.length > 0) {
            translator(children, children.children);
          }
        };
        translator(children);
        return true;
      } else {
        this.arrNode.forEach((item) => {
          if (item.whether == 1) {
            falg = true;
          }
        });
        return falg;
      }
    },
  },
  created() {
    this.translateMenuTree(this.menu);
  },
  methods: {
    // 通过menuId与动态(菜单)路由进行匹配跳转至指定路由
    gotorouteHandler(menuId) {
      var route = SITE_CONFIG.dynamicMenuRoutes.filter(
        (item) => item.meta.menuId === menuId
      )[0];
      if (route.meta.isNewWindow) {
        window.open(`./#/${route.path}`);
        return;
      }
      var reg = /^([hH][tT]{2}[pP]:\/\/|[hH][tT]{2}[pP][sS]:\/\/)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\/])+$/;
      if (reg.test(route.meta.iframeURL)) {
        window.open(
          `${route.meta.iframeURL}/?id=${this.$store.state.dp_pj0101}`
        );
        return;
      }

      var tab = this.$store.state.contentTabs.filter(
        (item) => item.name === route.name
      )[0];
      if (tab) {
        if (tab.name === this.$route.name) return;
        return this.$router.push({
          name: tab.name,
          params: { ...tab.params },
          query: { ...tab.query },
        });
      }

      if (route) {
        return this.$router.push({ name: route.name });
      }
    },
    translateMenuTree(menu) {
      if (menu.children.length == 0) {
        this.arrNode.push(menu);
      } else {
        menu.children.forEach((item) => {
          this.translateMenuTree(item);
        });
      }
    }
  },
};
</script>

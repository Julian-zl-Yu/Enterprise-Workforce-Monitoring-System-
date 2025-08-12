<template>
  <aside
    :class="[
      'app-sidebar elevation1',
      `app-sidebar--${$store.state.sidebarLayoutSkin}`,
    ]"
  >
    <div class="search-key-words">
      <el-input
        v-show="!$store.state.sidebarFold"
        v-model="menuSearchKeywords"
        clearable
        size="mini"
        placeholder="输入模块名"
        prefix-icon="el-icon-search"
      />
    </div>
    <el-scrollbar style="flex: 1" v-loading="navIsLoading" v-if="isRouterAlive">
      <!-- all menu -->
      <div v-show="!menuSearchKeywords" class="app-sidebar__inner">
        <el-menu
          :default-active="$store.state.sidebarMenuActiveName"
          :collapse="$store.state.sidebarFold"
          :unique-opened="true"
          :collapse-transition="false"
          class="app-sidebar__menu"
        >
          <sub-menu
            v-for="menu in $store.state.sidebarMenuList"
            :key="menu.id"
            :menu="menu"
          />
        </el-menu>
      </div>
      <!-- 根据关键字显示 -->
      <div v-if="menuSearchKeywords" class="app-sidebar__inner">
        <el-menu
          :default-active="$store.state.sidebarMenuActiveName"
          :collapse="$store.state.sidebarFold"
          :unique-opened="true"
          :collapse-transition="false"
          class="app-sidebar__menu"
        >
          <sub-menu
            v-for="menu in sidebarMenuListByKeyWords"
            :key="menu.id"
            :menu="menu"
          />
        </el-menu>
      </div>
    </el-scrollbar>
    <div
      :class="['toggle', { flod: $store.state.sidebarFold }]"
      @click="$store.state.sidebarFold = !$store.state.sidebarFold"
    >
      <i class="el-icon-caret-left" />
    </div>
  </aside>
</template>

<script lang="ts">
import SubMenu from "./main-sidebar-sub-menu";
import { SITE_CONFIG } from "@/main.config";
import debounce from "lodash/debounce";
import { set } from "lodash";
import { getSysMenuNav } from "@/router/index";

function treeFilter(array, selectedKey) {
  let targetArray = [];
  for (let index = 0; index < array.length; index++) {
    const item = array[index];
    if (~item.name.indexOf(selectedKey)) {
      targetArray.push(item);
      continue;
    } else {
      let resArray = [];
      if (item.children) {
        resArray = treeFilter(item.children, selectedKey);
        if (resArray.length > 0) {
          item.children = resArray;
          targetArray.push(item);
        }
      }
    }
  }
  return targetArray;
}

const debounceTreeFilter = debounce((arr, key, vm) => {
  let _arr = JSON.parse(JSON.stringify(arr));
  vm.sidebarMenuListByKeyWords = treeFilter(_arr, key);
}, 600);

export default {
  components: {
    SubMenu,
  },
  computed: {
    isRouterAliveFn() {
      return this.$store.state.nav.navType;
    },
    navIsLoading() {
      return this.$store.state.nav.navIsLoading;
    },
  },
  data() {
    return {
      isRouterAlive: true,
      menuSearchKeywords: "",
      sidebarMenuListByKeyWords: [],
    };
  },
  created() {
    var _this = this;
    // _this.$store.state.sidebarMenuList = SITE_CONFIG["menuList"];
    _this.$watch("menuSearchKeywords", (value) => {
      if (!value) return [];
      // return debounceTreeFilter(SITE_CONFIG["menuList"], value, _this);
      return debounceTreeFilter(
        _this.$store.state.sidebarMenuList,
        value,
        _this
      );
    });
  },
  watch: {
    isRouterAliveFn(curVal, oldVal) {
      
      var _this = this;
      getSysMenuNav();
      // _this.$store.state.sidebarMenuList = SITE_CONFIG["menuList"];
      _this.$watch("menuSearchKeywords", (value) => {
        if (!value) return [];
        // return debounceTreeFilter(SITE_CONFIG["menuList"], value, _this);
        return debounceTreeFilter(
          _this.$store.state.sidebarMenuList,
          value,
          _this
        );
      });
      this.isRouterAlive = false;
      this.$nextTick(function() {
        this.isRouterAlive = true;
      });
    },
  },
};
</script>
<style lang="scss" scoped>
aside.app-sidebar {
  overflow: unset;
  &::after {
    content: " ";
  }
}
.search-key-words {
  padding: 4px;
}
.toggle {
  transition: opacity left width 0.3s ease-in-out;
  position: absolute;
  background-color: #fff;
  width: 20px;
  height: 40px;
  right: -10px;
  top: 45%;
  color: #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.12);
  z-index: 1;
  &.flod {
    .el-icon-caret-left {
      transform: rotate(180deg);
    }
  }
  &:hover {
    cursor: pointer;
  }
}
</style>

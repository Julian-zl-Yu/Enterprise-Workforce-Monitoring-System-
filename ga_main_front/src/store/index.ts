// @ts-nocheck
import Vue from "vue";
import Vuex from "vuex";
import cloneDeep from "lodash/cloneDeep";
import user from "./modules/user";
import nav from "./modules/nav";
import { SITE_CONFIG } from "@/main.config";
Vue.use(Vuex);

export default new Vuex.Store({
    namespaced: true,
    state: {
        // 导航条, 布局风格, default(白色) / colorful(鲜艳)
        navbarLayoutType: "default",
        // 侧边栏, 布局皮肤, default(白色) / dark(黑色)
        sidebarLayoutSkin: "default",
        // 侧边栏, 折叠状态
        sidebarFold: false,
        // 侧边栏, 菜单
        sidebarMenuList: [],
        sidebarMenuActiveName: "",
        isNoticeRefresh: false,
        // 内容, 是否需要刷新
        contentIsNeedRefresh: false,
        // 内容, 标签页(默认添加首页)
        contentTabs: [{
            ...SITE_CONFIG["contentTabDefault"],
            "name": "home",
            "title": "home"
        }],
        contentTabsActiveName: "home",
        dp_pj0101: "",
        // 缓存数据
        selectDataKey: {},
        isJumpRoute: false,
        isAllFinish:false
    },
    modules: {
        user,
        nav
    },
    mutations: {
        // 重置vuex本地储存状态
        resetStore(state) {
            if (state?.user?.id) {
                Object.keys(state).forEach((key) => {
                    state[key] = cloneDeep(SITE_CONFIG["storeState"][key]);
                });
            }
        }
    }
});
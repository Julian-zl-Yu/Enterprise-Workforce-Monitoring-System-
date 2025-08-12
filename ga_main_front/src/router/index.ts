// @ts-nocheck
import Vue from "vue";
import {
    getPathMap,
    Router
} from "./vue-router";
import http from "@/utils/http.ts";
import store from "@/store";
import {
    isURL
} from "@/utils/validate.ts";
import {
    SITE_CONFIG
} from "@/main.config";
import startCase from "lodash/startCase";
import camelCase from "lodash/camelCase";
import findIndex from "lodash/findIndex";
import merge from "lodash/merge";
import {
    LS_KEY,
    STATUS_TYPE
} from "@/components/Types";
import ls from "@/utils/localStorageHelper";

Vue.use(Router);
// 页面路由(独立页面)
export const pageRoutes = [{
    path: "/401",
    component: () => import("@/views/pages/401"),
    name: "401",
    meta: {
        title: "401无权访问"
    }
},
{
    path: "/404",
    component: () => import("@/views/pages/404"),
    name: "404",
    meta: {
        title: "404未找到"
    },
    beforeEnter(to, from, next) {
        // 拦截处理特殊业务场景
        // 如果, 重定向路由包含__双下划线, 为临时添加路由
        if (/__.*/.test(to.redirectedFrom)) {
            return next(to.redirectedFrom.replace(/__.*/, ""));
        }
        next();
    }
},
{
    name: "login",
    path: "/login",
    redirect: "/login/form",
    meta: {
        title: "登录"
    },
    component: () => import("@/views/pages/Login/Login"),
    children: [{
        path: "form",
        name: "loginForm",
        component: () => import( /* webpackChunkName: "LoginForm" */ "@/views/pages/Login/LoginForm")
    },
    {
        path: "register",
        name: "register",
        component: () => import( /* webpackChunkName: "LoginRegister" */ "@/views/pages/Login/Register/Register")
    },
    {
        path: "resetPassword",
        name: "resetPassword",
        component: () => import( /* webpackChunkName: "LoginRegister" */ "@/views/pages/Login/ResetPassword/ResetPassword")
    }
    ]

    },
    // 常见问题
    {
        path: "/FAQ",
        component: () => import("@/views/pages/FAQ"),
        name: "FAQ",
        meta: {
            title: "常见问题"
        }
    },
];

/* 只在开发模式下有用 */
let mainComponent = () => import("@/views/main");

if (process.env.NODE_ENV === "development") {
    mainComponent = window.location.pathname === "/dev" ? () => import("@/dev/devMain.vue") : () => import("@/views/main");
    pageRoutes.push({
        name: "Dev",
        path: "/dev",
        meta: {
            title: "测试开发"
        },
        redirect: {
            name: "devdoc"
        },
        component: () => import("@/views/devDoc/Index"),
        children: [{
            path: "doc",
            name: "devdoc",
            component: () => import( /* webpackChunkName: "LoginForm" */ "@/views/devDoc/DevDoc")
        }, {
            path: "demo",
            name: "devdemo",
            component: () => import( /* webpackChunkName: "LoginForm" */ "@/views/devDoc/DevDemo")
        },
        {
            path: "layout",
            name: "devLayout",
            component: () => import( /* webpackChunkName: "LoginForm" */ "@/views/devDoc/Layouter/Layouter")
        }
        ]

    });
}

export async function getSysMenuNav() {
    var roleId = store.state.user.roleId
    var navArr = []
    // 存储路由菜单数据 避免多次发送请求
    if (store.state.nav.allNavArr.length == 0) {
        // 判断是否通过roleId获取路由
        if (roleId) {
            let {
                data: res
            } = await http.get("/sys/menu/roleNav/" + roleId);

            if (res.code !== 0) {
                Vue.prototype.$message.error(res.msg);
                return false;
            }
            navArr = res.data
            if (navArr.length && navArr.length > 0) {
                store.state.nav.navIsLoading = false
            }
            store.state.nav.allNavArr.push({
                navArr: navArr,
                navType: store.state.nav.navType
            })
        } else {
            let {
                data: res
            } = await http.get("/sys/menu/nav");

            if (res.code !== 0) {
                Vue.prototype.$message.error(res.msg);
                return false;
            }
            navArr = res.data
            if (navArr.length && navArr.length > 0) {
                store.state.nav.navIsLoading = false
            }
        }

    }
    if (store.state.nav.allNavArr.length > 0) {
        if (findIndex(store.state.nav.allNavArr, function (o) { return o.navType == store.state.nav.navType; }) == -1) {
            // 通过roleId获取路由
            let {
                data: res
            } = await http.get("/sys/menu/roleNav/" + roleId);

            if (res.code !== 0) {
                Vue.prototype.$message.error(res.msg);
                return false;
            }
            navArr = res.data
            if (navArr.length && navArr.length > 0) {
                store.state.nav.navIsLoading = false
            }
            store.state.nav.allNavArr.push({
                navArr: navArr,
                navType: store.state.nav.navType
            })
        }
    }
    var newNavArr = []
    if (store.state.nav.allNavArr.length > 0) {
        store.state.nav.allNavArr.filter(item => {
            if (item.navType == store.state.nav.navType) {
                newNavArr = item.navArr
            }
        })
    } else {
        newNavArr = navArr
    }

    // SITE_CONFIG["menuList"] = navArr;
    // store.state.sidebarMenuList = navArr
    store.state.sidebarMenuList = newNavArr
    // fnAddDynamicMenuRoutes(SITE_CONFIG["menuList"]);
    // fnAddDynamicMenuRoutes(navArr);
    fnAddDynamicMenuRoutes(newNavArr);
    /*    
       try { // 获取菜单列表, 添加并全局变量保存
          
       } catch (error) {
           console.error(error);
           Vue.prototype.$message.error("网络故障，请稍后刷新页面！");
       } */
}

// 模块路由(基于主入口布局页面)
export const moduleRoutes = {
    path: "/",
    component: mainComponent,
    name: "main",
    redirect: {
        name: "home"
    },
    meta: {
        title: "主入口布局"
    },
    children: [{
        path: "/home",
        component: () => import("@/views/modules/home.vue"),
        name: "home",
        meta: {
            title: "首页",
            isTab: true
        }
    }]
};

export function addDynamicRoute(routeParams, router) {
    // 组装路由名称, 并判断是否已添加, 如是: 则直接跳转
    var routeName = routeParams.routeName;
    var dynamicRoute = SITE_CONFIG["dynamicRoutes"].filter(item => item.name === routeName)[0];
    if (dynamicRoute) {
        return router.push({
            name: routeName,
            params: routeParams.params
        });
    }
    // 否则: 添加并全局变量保存, 再跳转
    dynamicRoute = {
        path: routeName,
        component: () => import(`@/views/modules/${routeParams.path}`),
        name: routeName,
        meta: {
            ...SITE_CONFIG["contentTabDefault"],
            menuId: routeParams.menuId,
            title: `${routeParams.title}`
        }
    };
    router.addRoutes([{
        ...moduleRoutes,
        name: `main-dynamic__${dynamicRoute.name}`,
        children: [dynamicRoute]
    }]);
    SITE_CONFIG["dynamicRoutes"].push(dynamicRoute);
    router.push({
        name: dynamicRoute.name,
        params: routeParams.params
    });
}

const router = new Router({
    mode: "hash",
    scrollBehavior: () => ({
        y: 0
    }),
    routes: pageRoutes.concat(moduleRoutes)
});


router.afterEach(async (to, from) => {
    const fromName = from?.name;
    if (fromName) {
        const tab = store.state.contentTabs.filter(item => item.name === fromName)[0];
        if (tab) {
            tab.query = {
                ...from?.query
            };
        }
    }
});

router.beforeEach(async (to, from, next) => {
    /*开发模式下才有dev路径下的模块*/
    if (/^dev/g.test(to.name)) {
        return next();
    }
    const token = sessionStorage.getItem("token");
    /*没有token就是没有登录 TODO：过期检测 没有登录也就不需要加载动态菜单*/
    if (!token) {
        /*没有登录的情况下只允许登录注册*/
        const PageWhiteList = ["loginForm", "register",'resetPassword'];
        if (PageWhiteList.indexOf(to.name) < 0) {
            return next("/login");
        }
        return next();
    }
    /* 有token的情况 */
    /* 没有加载菜单就先加载菜单  */
    if (!SITE_CONFIG.dynamicMenuRoutesHasAdded) {
        /* 后于VueRouter根据location，需要再触发 */
        await getSysMenuNav();
    }

    if (to.name === "loginForm") {
        return next("/home");
    } else if (isPageRoute(to)) {
        return next();
    } else {
        return next(false);
    }
});

/**
 * 判断当前路由是否为页面路由
 * @param {*} route 当前路由
 */
function isPageRoute(route) {
    const pathMap = getPathMap();
    return Boolean(pathMap[route.path]);
}

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function fnAddDynamicMenuRoutes(menuList = [], routes = []) {
    var temp = [];
    for (var i = 0; i < menuList.length; i++) {
        if (menuList[i].children && menuList[i].children.length >= 1) {
            temp = temp.concat(menuList[i].children);
            continue;
        }
        // 组装路由
        var route = {
            path: "",
            component: null,
            name: "",
            meta: {
                ...SITE_CONFIG["contentTabDefault"],
                menuId: menuList[i].id,
                title: menuList[i].name
            }
        };
        // eslint-disable-next-line
        /*URL支持{{ SITE_CONFIG["xxxx"] }}占位符变量比如:apiURL
        I* 参见 main.config.js SITE_CONFIG*/
        let menuURL = (menuList[i].url || "").replace(/{{([^}}]+)?}}/g, (s1, s2) => {
            const prop = s2.match(/SITE_CONFIG\["(.*)"\]/)[1];
            return SITE_CONFIG[prop];
        });

        if (isURL(menuURL)) {
            route["path"] = route["name"] = `i-${menuList[i].id}`;
            route["meta"]["iframeURL"] = menuURL;
        } else {
            /* @newtab => target="_blank" 新开一个页面*/
            const reg = /@newtab/;
            if (reg.test(menuURL)) {
                menuURL = menuURL.replace(reg, "");
                route["meta"]["isNewWindow"] = menuURL;
            }
            menuURL = menuURL.replace(/^\//, "").replace(/_/g, "-");
            route["path"] = route["name"] = menuURL.replace(/\//g, "-");

            route["component"] = () => import(`@/views/modules/${menuURL}`);

            /*  if (process.env.View === "CZTL") {
                 route["component"] = () => import(`@/viewsCZTL/modules/${menuURL}`);
             } else {
             } */
        }
        routes.push(route);
    }
    if (temp.length >= 1) {
        return fnAddDynamicMenuRoutes(temp, routes);
    } else {

        // 添加路由
        const dynamicRoute = merge({}, moduleRoutes, {
            children: routes
        });
        router.addRoutes([dynamicRoute, {
            path: "*",
            redirect: {
                name: "404"
            }
        }]);
        SITE_CONFIG.dynamicMenuRoutes = routes;
        SITE_CONFIG.dynamicMenuRoutesHasAdded = true;
    }
}

export default router;

/*获取当前路由名*/
/* moduleName 就是 routeName */
export function getModuleNameFromRouteName(routeName) {
    routeName = String(routeName);
    const nameArray = routeName.split("-");
    if (nameArray.length === 3) {
        routeName = `${nameArray[0]}_${nameArray[1]}`;
    }
    return startCase(camelCase(routeName)).replace(/ /g, "");
}
// @ts-nocheck

import { update } from "lodash";
import Vue from "vue";

/* 生产环境下不打印log信息 */
if (process.env.NODE_ENV === "production") {
  window.console.oldLog = window.console.log;
  window.console.log = () => { };
}
export const API_URL = {
  /* 权限 */
  permissions: "/sys/menu/permissions",
  userInfo: "/sys/user/info",
  /*文件上传*/
  upload: "/enterprise/ot01/upload",
  /*删除*/
  delete: "/enterprise/ot01",
  /*行政区划 版本*/
  getAreaCodeVersion: "/sys/region/regionVersion",
  /*行政区划 原始数据*/
  getAreaCodeOrigin: "/sys/region/regionUpdate",
  /* 身份证阅读器 */
};

let REMOTE_HOST_MAP = {
  /* 开发环境 */
  dev: {
    api: "http://localhost:9002",
    socket: "wss://jzgsmzpc.xygkcloud.com/websocket",
  },
  /*  集成测试环境 */
  "prod:sit": {
    api: 'http://192.168.0.46:9192',//杨通伟  
    socket: "ws://182.150.57.171:19018/websocket",
  },
  /*  验收测试环境 */
  "prod:uat": {
    api: "http://localhost:9002",
    socket: "ws://localhost:9000/websocket",
  },
  /*  生产环境 */
  prod: {
    api: "http://gawork.xinyegk.com:19192",//测试3.2
    socket: "wss://jzgsmzpc.xygkcloud.com/websocket",
  },
};
// 创建一个类型
type typeNodeEnv = keyof typeof REMOTE_HOST_MAP;

const nodeEnv: typeNodeEnv = process.env.VUE_APP_NODE_ENV;
const BASE_URL = process.env.BASE_URL;
const remoteHostInfo = REMOTE_HOST_MAP[nodeEnv];
export const SITE_CONFIG = {
  /* 服务端字典对应的标签名、值名 */
  DICT_LABEL_VALUE_NAME: ["dictLabel", "dictValue"],
  /*前端静态资源的访问地址*/
  BASE_URL,
  /*环境模式*/
  nodeEnv,
  version: Vue.version,
  apiURL: remoteHostInfo.api, // api请求地址
  socketURL: remoteHostInfo.socket,
  storeState: {}, // vuex本地储存初始化状态（用于不刷新页面的情况下，也能重置初始化项目中所有状态）
  contentTabDefault: {
    // 内容标签页默认属性对象
    name: "", // 名称, 由 this.$route.name 自动赋值（默认，名称 === 路由名称 === 路由路径）
    params: {}, // 参数, 由 this.$route.params 自动赋值
    query: {}, // 查询参数, 由 this.$route.query 自动赋值
    menuId: "", // 菜单id（用于选中侧边栏菜单，与this.$store.state.sidebarMenuActiveName进行匹配）
    title: "", // 标题
    isTab: true, // 是否通过tab展示内容?
    iframeURL: "", // 是否通过iframe嵌套展示内容? (以http[s]://开头, 自动匹配)
  }, // 内容标签页默认属性对象
  menuList: [], // 左侧菜单列表（后台返回，未做处理）
  permissions: [], // 页面按钮操作权限（后台返回，未做处理）
  dynamicRoutes: [], // 动态路由列表
  dynamicMenuRoutes: [], // 动态(菜单)路由列表
  dynamicMenuRoutesHasAdded: false, // 动态(菜单)路由是否已经添加的状态标示（用于判断是否需要重新拉取数据并进行动态添加操作）
};
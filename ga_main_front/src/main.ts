//@ts-nocheck
import {
  SITE_CONFIG
} from "@/main.config";
import Vue from "vue";
import Element from "element-ui";
import "animate.css";
// import "animate.css/source/attention_seekers/shakeX.css";
import App from "@/App.vue";
import i18n from "@/i18n";
import router from "@/router/index.ts";
import store from "@/store";
import {
  GetLib
} from "@/utils/libScript";
import {
  GLOBAL_LIB,
  STATUS_TYPE
} from "@/components/Types";
import "@/icons";
// import "@/element-ui/theme/index.css";
import "../public/element-theme/default/index.css";
import http from "@/utils/http";
import {
  ImportAllCustomizeComponent
} from "@/components";

import {
  ImportAllCustomizeDirective
} from "@/directives";
import {
  hasPermission,
  getDictLabel,
  getAreaCodeLabel
} from "@/utils/index.ts";
import cloneDeep from "lodash/cloneDeep";
import {
  BTN_TYPE
} from "@/components/Types";
import {
  Plugin
} from "vue-fragment";
import { FORM_ITEM_TYPE } from "@/components/Types";
import VueDragDrop from "vue-drag-drop";
import "@/assets/scss/ui.scss";
import BaiduMap from 'vue-baidu-map';
// 滚动组件
import vueSeamlessScroll from 'vue-seamless-scroll'
window.alert.success = message => {
  Element.Notification({
    message,
    type: STATUS_TYPE.success,
    duration: 2000,
  });
};

window.alert.error = message => {
  Element.Notification({
    message,
    type: STATUS_TYPE.error,
    duration: 2000,
  });
};
/* 全应用通信BUS */
window.Vuex = new Vue();


Vue.config.productionTip = false;
Vue.use(vueSeamlessScroll)
Vue.use(Plugin);
Vue.use(VueDragDrop);
Vue.use(Element, {
  size: "medium",
  i18n: (key: string, value: string) => i18n.t(key, value)
});
Vue.use(BaiduMap, {
  ak: 'arobHI7OA0T2buqmy5iWLndFYq2Ejp2B'
});

ImportAllCustomizeComponent(Vue);
ImportAllCustomizeDirective(Vue);
// 挂载全局
Vue.prototype.BTN_TYPE = BTN_TYPE;
Vue.prototype.$http = http;
Vue.prototype.$hasPermission = hasPermission;
Vue.prototype.$getDictLabel = getDictLabel;
Vue.prototype.$getAreaCodeLabel = getAreaCodeLabel;
Vue.prototype.FORM_ITEM_TYPE = FORM_ITEM_TYPE;
Vue.prototype.$log = (...args) => {
  console.log.apply(console.log, args);
};

Array.prototype.removeByValue = function (val) {
  for (var i = 0; i < this.length; i++) {
    if (this[i] == val) {
      this.splice(i, 1);
      break;
    }
  }
};
// 保存整站vuex本地储存初始状态
SITE_CONFIG["storeState"] = cloneDeep(store.state);

window.app = new Vue({
  i18n,
  router,
  store,
  render: h => h(App)
}).$mount("#app");

Object.defineProperty(window.app, "state", {
  get() {
    return window.app?.$store?.state || "not found";
  }
});

// 日期格式化
Date.prototype.format = function(fmt) { 
  var o = { 
     "M+" : this.getMonth()+1,                 //月份 
     "d+" : this.getDate(),                    //日 
     "h+" : this.getHours(),                   //小时 
     "m+" : this.getMinutes(),                 //分 
     "s+" : this.getSeconds(),                 //秒 
     "q+" : Math.floor((this.getMonth()+3)/3), //季度 
     "S"  : this.getMilliseconds()             //毫秒 
 }; 
 if(/(y+)/.test(fmt)) {
         fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
 }
  for(var k in o) {
     if(new RegExp("("+ k +")").test(fmt)){
          fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
      }
  }
 return fmt; 
}      

/*解决 PositionPick 第一次加载地图白屏*/
setTimeout(async () => await GetLib(GLOBAL_LIB.AMap), 1);

window.onerror = function (message, source, lineno, colno, error) {
  console.info("window.onerror -> message, source, lineno, colno, error", message, source, lineno, colno, error);
};
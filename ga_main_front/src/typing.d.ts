import VueRouter from "vue-router";
import { Route } from "vue-router";
import http from "@/utils/http";
import Vue, { VNode } from "vue";


declare module "*.vue" {
  export default Vue;
}

declare module '*.md' {
  const content: string;
  export default content;
}

declare module "vue/types/vue" {
  interface Vue {
    $router: VueRouter,
    $route: Route,
    $http: http
  }
}

declare var window: Window & typeof globalThis & {
  SITE_CONFIG: object,
};

declare global {
  namespace JSX {
    // tslint:disable no-empty-interface
    interface Element extends VNode { }
    // tslint:disable no-empty-interface
    interface ElementClass extends Vue { }
    interface IntrinsicElements {
      [elem: string]: any
    }
  }
}


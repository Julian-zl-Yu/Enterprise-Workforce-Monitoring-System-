import axios from "axios";
import Cookies from "js-cookie";
import router from "@/router/index.ts";
import qs from "qs";
import $ from "jquery";
import {
    clearLoginInfo
} from "@/utils/index.ts";
import isPlainObject from "lodash/isPlainObject";
import {
    SITE_CONFIG
} from "@/main.config";
import ls from "@/utils/localStorageHelper";
import {
    LS_KEY
} from "@/components/Types";

const http = axios.create({
    baseURL: SITE_CONFIG["apiURL"],
    timeout: 1000 * 180,
    withCredentials: true
});

/**
 * 请求拦截
 */

http.interceptors.request.use(config => {
    config.headers["Accept-Language"] = Cookies.get("language") || "zh-CN";
    config.headers["token"] = sessionStorage.getItem(LS_KEY.token) || "";
    // 默认参数
    var defaults = {};
    // 防止缓存，GET请求默认带_t参数
    if (config.method === "get") {
        config.params = {
            ...config.params,
            ...{
                "_t": new Date().getTime()
            }
        };
    }
    if (isPlainObject(config.params)) {
        config.params = {
            ...defaults,
            ...config.params
        };
    }
    if (isPlainObject(config.data)) {
        config.data = {
            ...defaults,
            ...config.data
        };
        if (/^application\/x-www-form-urlencoded/.test(config.headers["content-type"])) {
            config.data = qs.stringify(config.data);
        }
    }
    return config;
}, error => {
    return Promise.reject(error);
});

/**
 * 响应拦截
 */

http.interceptors.response.use(response => {
    if (response.data.code === 401 || response.data.code === 10001) {
        clearLoginInfo();
        router.replace({
            name: "login"
        });
        return Promise.reject(response.data.msg);
    }
    return response;
}, error => {
    console.error(error);
    return Promise.reject(error);
});

export default http;

export function ajax(options) {
    const {
        url,
        method,
        params
    } = options;

}

export function getJSON(path, params = {}) {
    return new Promise((resolve, reject) => {
        $.getJSON(path, params)
            .done(resolve)
            .fail(reject);
    });
}
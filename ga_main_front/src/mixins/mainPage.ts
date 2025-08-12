import qs from "qs";
import Cookies from "js-cookie";
import merge from "lodash/merge";
import map from "lodash/map";
import isFunction from "lodash/isFunction";
import isArray from "lodash/isArray";
import {
    SITE_CONFIG
} from "@/main.config";
import {
    SuccessOrFailed,
    addToken
} from "@/utils/index.ts";
import {
    getModuleNameFromRouteName
} from "@/router";
import $ from "jquery";
import Vue from "vue";
export function getPageInfo(page = 1, limit = 10, total = 0) {
    return {
        page,
        limit,
        total,
    };
}

export default Vue.extend({
    data() {
        /* eslint-disable */
        return {
            privateCurrentView: "main",
            /* 2020年5月19日13:01:47 */
            pageInfo: getPageInfo(),
            /* 2020年5月19日13:01:47 */
            // 设置属性
            moduleOptions: {
                createdIsNeed: true, // 此页面是否在创建时，调用查询数据列表接口？
                activatedIsNeed: false, // 此页面是否在激活（进入）时，调用查询数据列表接口？
                getDataListURL: '', // 数据列表接口，API地址
                getDataListIsPage: false, // 数据列表接口，是否需要分页？
                deleteURL: '', // 删除接口，API地址
                deleteIsBatch: false, // 删除接口，是否需要批量？
                deleteIsBatchKey: 'id', // 删除接口，批量状态下由那个key进行标记操作？比如：pid，uid...
                exportURL: '' // 导出接口，API地址
            },
            // 默认属性
            dataForm: {}, // 查询条件
            // formDataQuery:{},//默认的查询条件
            dataList: [], // 数据列表
            order: '', // 排序，asc／desc
            orderField: '', // 排序，字段
            /*新增pageInfo 配合 */
            page: 1, // 当前页码
            limit: 10, // 每页数
            total: 0, // 总条数
            dataListLoading: false, // 数据列表，loading状态
            dataListSelections: [], // 数据列表，多选项
            addOrUpdateVisible: false // 新增／更新，弹窗visible状态
        }
        /* eslint-enable */
    },
    watch: {
        dataList: {
            handler: function (val, oldVal) {
                this.$nextTick(() => {
                    this.$refs?.elTable?.doLayout();
                });
            },
            // 深度观察监听
            deep: true
        },
        $route: {
            handler: function (val, oldVal) {
                this.$nextTick(() => {
                    this.$refs?.elTable?.doLayout();
                });
            },
            // 深度观察监听
            deep: true
        }
    },
    computed: {
        currentView(): string {
            if (this.$route.name === this.moduleName) {
                this.privateCurrentView = this.$route?.query?.v || "main";
            }
            return this.privateCurrentView;
        },
    },
    created() {
        this.checkModuleName();
    },
    mounted() {
        if (this.moduleOptions.createdIsNeed) {
            this.query();
        }
    },
    activated() {
        if ((this as any).moduleOptions.activatedIsNeed) {
            (this as any).query();
        }
    },
    methods: {
        checkModuleName() {
            if (!this.moduleName) {
                window.alert("no moduleName");
            }
        },
        isShowView(viewName) {
            this.ShowViewRecord = this.ShowViewRecord || {};
            /* 只在路由是当前模块是，对路由改变做出响应 */
            /* 当前路由所示模块 */
            let tabName = getModuleNameFromRouteName(this.$route.name);
            if (tabName === this.moduleName /* 当前模块 */) {
                /* 相同才判断，否则保持原来的 */
                this.ShowViewRecord = {
                    [this.currentView]: true
                };
            }
            return this.ShowViewRecord[viewName] || false;
        },

        async query(...args) {
            /* 可以自己override query => overrideQuery */
            if (isFunction(this.overrideQuery)) return this.overrideQuery.apply(this, args);
            this.dataListLoading = true;
            const url = this.moduleOptions.getDataListURL;

            Object.keys(this.formDataQuery).forEach(key => {
                if (isArray(this.formDataQuery[key])) {
                    this.formDataQuery.startTime = this.formDataQuery[key][0]
                    this.formDataQuery.endTime = this.formDataQuery[key][1]
                }
            })

            let params = merge(
                this.moduleOptions.getDataListIsPage ? {
                    ...this.pageInfo
                } : {}, {
                order: this.order,
                orderField: this.orderField,
                ...this.formDataQuery
            });

            if (isFunction(this.queryHookBeforeSend)) {
                params = this.queryHookBeforeSend(params);
            }
            let {
                data: res
            } = await this.$http.get(url, {
                params
            });
            // this.dataListLoading = false;

            setTimeout(() => {
                this.dataListLoading = false;
            }, 800);

            let fnSuccess = isFunction(this.overrideFnSuccess) ? this.overrideFnSuccess.bind(this, res) : () => {
                this.dataList = this.moduleOptions.getDataListIsPage ? res.data.list : res.data;
                this.pageInfo.total = this.moduleOptions.getDataListIsPage ? res.data.total : 0;
            };
            let fnFailed = isFunction(this.overrideFnFailed) ? this.overrideFnFailed.bind(this) : () => {
                this.dataList = [];
                this.pageInfo.total = 0;
            };
            return SuccessOrFailed(res, {
                fn: fnSuccess
            }, {
                msg: res.msg,
                fn: fnFailed
            });
        }, // 获取数据列表
        dataListSelectionChangeHandle(val) {
            this.dataListSelections = val;
        },
        // 排序
        dataListSortChangeHandle(data) {
            if (!data.order || !data.prop) {
                this.order = "";
                this.orderField = "";
                return false;
            }
            this.order = data.order.replace(/ending$/, "");
            this.orderField = data.prop.replace(/([A-Z])/g, "_$1").toLowerCase();
            this.query();
        },
        getTableList() {
            this.pageInfo.page = 1;
            this.query();
        },
        // 删除
        async deleteBy(id) {
            const _this = this;
            if (_this.moduleOptions.deleteIsBatch && !id && _this.dataListSelections.length <= 0) {
                return _this.$message({
                    message: _this.$t("prompt.deleteBatch"),
                    type: "warning",
                });
            }
            let {
                data: res
            } = await _this.$http.delete(
                `${_this.moduleOptions.deleteURL}${_this.moduleOptions.deleteIsBatch ? "" : "/" + id}`,
                _this.moduleOptions.deleteIsBatch ? {
                    "data": id ? [id] : _this.dataListSelections.map(item => item[_this.moduleOptions.deleteIsBatchKey])
                } : {}
            );
            return SuccessOrFailed(res, {
                msg: "删除成功",
                fn: () => _this.getTableList()
            }, {
                msg: res.msg
            });
        },
        // 导出
        exportHandler() {
            let element = new Vue();
            // element.$loading();
            
            var params;
            params = qs.stringify({
                "token": Cookies.get("token"),
                ...this.formDataQuery
            });
            let exportUrl= `${SITE_CONFIG["apiURL"]}${this.moduleOptions.exportURL}?${params}`;
            // window.location.href = `${SITE_CONFIG["apiURL"]}${this.moduleOptions.exportURL}?${params}`;
            window.open(exportUrl)
            // element.$loading().close();
        },
        // 导出合同
        exportContract(ids) {
            console.log(ids)
            var params;
            params = qs.stringify({
                "token": Cookies.get("token"),
                "ps0201s": ids.join(",")
            });
            console.log(params)
            window.location.href = `${SITE_CONFIG["apiURL"]}${this.moduleOptions.exportURL}?${params}`;
        }
    }
});

export function exportHandler(exportURL: string, dataForm: object, title = "导出文件", method = "get") {
    const action = `${SITE_CONFIG["apiURL"]}${exportURL}`;
    /*TODO:post请求未验证*/
    if ("post" === method) {
        const inputElements = map(dataForm, (value, name) => `<input name="${name}" type="hidden" value="${value}" />`).join("\n");
        const $form = $(
            `<form method="post" enctyped="multipart/form-data" action="${action}">
                ${inputElements}
            </form> `);
        $form.appendTo("body").submit().remove();
        return;
    }
    const params = `${addToken("")}&${qs.stringify(dataForm)}`;
    const linkHref = `${action}${params}`;
    const a = document.createElement("a");
    a.href = linkHref;
    a.download = title;
    a.target = "_blank";
    a.click();
}

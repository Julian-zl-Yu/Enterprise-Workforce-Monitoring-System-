import {
    BTN_TYPE
} from "@/components/Types";
import t from "@/i18n/zh-CN";
import {
    animateCSS
} from "@/utils/index.ts";
import isFunction from "lodash/isFunction";


export let BtnMixins = {
    props: {
        configs: {
            required: true,
            type: Object
        },
        canDelete: [Array],
        scope: {
            type: Object,
            default: () => ({}),
        }
    },

    data() {
        return {
            isLoading: false
        };
    },
    methods: {
        checkShowByScope(isAllow) {
            return this.configs.isShowByScope ? this.configs.isShowByScope(this.scope) : isAllow;
        },
        async debounceClick() {
            if (this.isLoading) return;
            /*loading */
            this.isLoading = true;
            try {
                await this.configs?.debounceClick();
            } catch (error) {
                setTimeout(
                    async () => await animateCSS(".el-form-item__error", "shakeX"),
                        300
                );
                console.error(error);
            } finally {
                /*hide loading icon*/
                this.isLoading = false;
            }
        }
    },
    computed: {
        attrIsDisabled() {
            if (isFunction(this?.configs.isDisabled)) {
                return this.configs.isDisabled(this.scope);
            }
            return false;
        },
        /* debounceBtn */
        loadingText() {
            return this.configs.textLoading || "等待...";
        },
        /* debounceBtn */
        shiro() {
            const _shiro = (this?.configs?.shiro);
            const typeOfShiro = typeof (_shiro);
            /*如果设置了shiro，以shiro权限为准，否则直接显示*/
            return ("string" === typeOfShiro) ? this.$hasPermission(_shiro) : true;
        },
        text() {
            return (this?.configs?.text) || t.btnType[BTN_TYPE[this.btnType]] || "按钮";
        },
        /*TODO:按钮颜色可以根据类型，可以配置一个映射表*/
        typeClass() {
            const dangerArray = [
                BTN_TYPE.delete
            ];

            if (this?.configs?.type === BTN_TYPE.save) {
                return "primary";
            }

            if (~dangerArray.indexOf(this?.configs?.type)) {
                return "danger";
            }

            if (this?.configs?.type === BTN_TYPE.login) {
                return "btn-type-login";
            }
            return "default";
        },
        btnType() {
            return this?.configs?.type;
        },
    }
};
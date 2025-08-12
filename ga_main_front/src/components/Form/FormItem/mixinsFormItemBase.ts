/*
import mixinSelect from "./Select";
import mixinCascader from "./Cascader";
import mixinRadio from "./Radio";
import mixinInput from "./Input";
import mixinDict from "./Dict";
import mixinInputNumber from "./InputNumber";
import mixinPassOrNot from "./PassOrNot";
import mixinMoney from "./Money";
import mixinPositionPicker from "./PositionPicker";
import mixinDatePicker from "./DatePicker";
 */
import reduce from "lodash/reduce";
import { FORM_ITEM_TYPE, STATUS_TYPE } from "@/components/Types";
import FormItemReadonly from "./implementFormItem/FormitemReadonly";

export const model = {
    model: {
        prop: "value",
        event: "change"
    }
};

export const subData = {
    data() {
        return {
            textWhenReadOnly: ""
        };
    }
};

export const subCreated = {
    /* mixin 需要init+componentType为方法名的初始化方法 */
    /*switch (this?.configs?.type) {*/
    /*TODO: dict 实际上是select（不一定）后期根据类型不同可能有不同的表现方式，比如是、否 通过 不通过 radio checkbox都是可选的*/
    /*    case FORM_ITEM_TYPE.Dict: {
            this.initDict();
            break;
        }
        default: {
            const initFunctionName = `init${FORM_ITEM_TYPE[this.componentType]}`;
            const fn = this[initFunctionName];
            if (fn) {
                fn(this?.configs || {});
            } else {
                console.error(`缺少 ${initFunctionName}`);
            }
            break;
        }
    }*/
    created() {
        /*check 必要的方法 比如 init*/
        if (!this.init) {
            this.$notify({
                title: "警告",
                message: `${FORM_ITEM_TYPE[this.componentType]}缺少必要方法`,
                type: STATUS_TYPE.warning
            });
        } else {
            this.init();
        }
    }
};

export const subComputed = {
    computed: {
        /*根据是否表单 是否列表 是否可编辑 是否disabled 是否readonly*/
        canEdit() {
            /*列表*/
            if (this.$parent.isTable) {
                /*不可编辑*/
                if (!this.isEdit) {
                    return false;
                }
            } else {
                /*表单 但是 只读*/
                if (this.attrFormReadOnly) {
                    return false;
                }
                /* 表单item只读 */
                if (this.configs?.readOnly) {
                    return false;
                }
            }
            return true;
        },
    }
};

export function attrMultiple() {
    return this?.configs?.multiple || false;
}

export function bindId() {
    return this.configs?.bindId;
}

export function clearable() {
    return this.configs?.clearable == false ? false : true;
}

export function userCustom() {
    return this.configs?.userCustom || false ;
}

export function maxValue() {
    return this.configs?.maxValue || '' ;  
}

export function minValue() {
    return this.configs?.minValue || 0 ;  
}
export function precision(){
    return this.configs?.precision || 0
}

export function isCache(){
    return this.configs?.isCache == false ? false : true;
}

export const props = {
    props: {
        value: {
            required: false,
            validator: () => {
                /* if (!(value || isNumberWhenInputing(value) || isString(value))) {
                    console.log("mixinsFormItem value: ",value);
                } */
                return true;
            },
            default() {
                return "";
            }
        },
        /*表单数据，可能根据传入的参数获取表单其他兄弟数据（可能不是同一个表单）*/
        formData: {
            required: false,
            type: Object,
            default() {
                return {};
            }
        },
        /* TableColumn option */
        rowIndex: {
            required: false,
            type: Number,
            default() {
                return 0;
            }
        },
        /*是否是列表模式，与isEdit配合使用*/
        isTable: {
            required: false,
            type: Boolean,
            default() {
                return false;
            }
        },
        isEdit: {
            required: false,
            type: Boolean,
            default() {
                /*与TableColumn相反，默认是需要编辑*/
                return true;
            }
        },
        scope: {
            required: false,
            type: Object,
            default() {
                return {};
            }
        },
        /* TableColumn option */
        configs: {
            required: true,
            type: Object
        }
    }
};

export const subProps = {
    props: {
        ...reduce(props.props, (parentProps, config, key) => {
            if ("value" === key) {
                parentProps.value = { required: true, validator: config.validator };
            } else {
                parentProps[key] = { required: true, type: config.type };
            }
            return parentProps;
        }, {}),
        /*表单是否编辑，保持控件对应UI，与readonly不一样*/
        componentType: {
            required: true,
            type: Number,
        },
        attrPlaceholder: {
            required: true,
            type: String,
        },
        attrDisabled: {
            required: true,
            type: Boolean,
        },
        attrFormReadOnly: {
            required: true,
            type: Boolean,
        }
    }
};

export const subComponents = {
    components: {
        FormItemReadonly
    }
};

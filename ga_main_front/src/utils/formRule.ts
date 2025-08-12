import i18n from "@/i18n";
import _isArray from "lodash/isArray";
import isPlainObject from "lodash/isPlainObject";
import isFinite from "lodash/isFinite";
import _debounce from "lodash/debounce";
import _some from "lodash/some";
import {
    isIDCard,
    isCorpcode,
    isEmail,
    isInput
} from "@/utils/validate.ts";
/* 必填 */
export const required = {
    required: true,
    validator: (rule, value, callback) => {
       
        let message = i18n.t("validate.required");
        if (rule.field == "addressObj") {
            if (value.address=='') {
                return callback(message);
            }
        }
        /* 未输入则判为空  */
        if (!isInput(value)) {
            return callback(message);
        }
        /* 数组要求至少选择一个 */
        if (_isArray(value) && value.length < 1) {
            return callback((message));
        }
        /* 对象属性值，但凡有一个是有用的，就让你过 */
        if (isPlainObject(value) && !_some(value, v => v || isFinite(v))) {
            return callback((message));
        }
        /* 如果是地址选择器带经纬度，必须三个属性都全 */
        /* if(_isObject(value)&&) */
        callback();
    },
    trigger: ["blur", "change"]
};

/* 排除特殊值: 不能为value数组包含的值*/
export const fnExclude = (configs = {
    value: [],
    msg: "信息不符合要求"
}) => {
    return {
        validator: (rule, value, callback) => {
            if (!value) {
                const msg = configs.msg || i18n.t("validate.required");
                callback(msg);
            } else if (~configs.value.indexOf(value)) {
                callback(configs.msg);
            } else {
                callback();
            }
        },
        trigger: ["blur", "change"]
    };
};

/**
 * @Description CForm 自有属性itemRefs用于收集formitem实例
 * @date 2020-08-24
 * @param {any} _this
 * @param {any} keyForm
 * @param {any} keyProp
 * @returns {any}
 */
function getAsyncValidatorTargetItem(_this, keyForm, keyProp) {
    let targetItem = _this.$refs[keyForm].itemRefs[keyProp];
    return _isArray(targetItem) ? targetItem[0] : targetItem;
}

export const fnAsyncValidator = (configs = {
    validateRequest: () => Promise.reject,
    vm: {},
    /*当前vue实例*/
    form: {},
    /*CForm 的ref name */
    prop: "" /*需要校验的字段prop name*/
}) => {
    const _this = configs.vm;
    const keyForm = configs.form;
    const keyProp = "item_" + configs.prop;

    const fnDebounceValidateRequest = _debounce(value => {
        let targetItem = getAsyncValidatorTargetItem(_this, keyForm, keyProp);
        configs
            .validateRequest(value)
            /*清空方法*/
            .then(() => targetItem.clearValidate())
            .catch(error => {
                //显示错误提示
                targetItem.validateMessage = error;
                targetItem.validateState = "error";
            });
    }, 1000);

    return {
        validator: (rule, value, callback) => {
            if (!value) {
                callback();
            }
            let targetItem = getAsyncValidatorTargetItem(_this, keyForm, keyProp);
            fnDebounceValidateRequest(value);
            if (targetItem.validateMessage) {
                callback((targetItem.validateMessage));
            } else {
                callback();
            }
        },
        trigger: ["blur", "change"]
    };
};

/* @测试 */
export const test = {
    validator: (rule, value, callback) => {
        console.log("rule.field", rule.field);
        if (!value) {
            callback((i18n.t("validate.required")));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};


//长度验证（数据）
export function length(min, max) {
    return {
        validator: function (rule, value, callback) {
            if (value) {
                value = String(value);
                if (value.length >= min && value.length <= max) {
                    callback();
                } else {
                    callback(("长度应在" + min + " " + max + "之间"));
                }
            }
            callback();
        }
    };
}

//手机号验证
export const phone = {
    /**
     * @Description 
     * @date 2020-07-30
     * @param {any} rule
     * @param {any} value
     * @param {any} callback
     * @returns {any}
     */
    validator: function (rule, value, callback) {
        var exp = /^1[3456789]\d{9}$/;
        if (!value) {
            callback();
        } else if (!exp.test(value)) {
            callback(("手机号码格式不正确"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};
//身份证号验证
export const idCardNumber = {
    validator: function (rule, value, callback) {
        /* JavaScript 中 number 0 string '' null undefined 都是假值 */
        /* 保持结构的扁平 */
        if (!value) {
            callback();
        } else if (!isIDCard(value)) {
            callback(("请输入正确的身份证号码"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};

/* 社会信用代码 */
export const corpcode = {
    validator: function (rule, value, callback) {
        /* JavaScript 中 number 0 string '' null undefined 都是假值 */
        /* 保持结构的扁平 */
        if (!value) {
            callback();
        } else if (!isCorpcode(value)) {
            callback(("请输入正确的社会统一信用代码"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};

/* 电子邮箱 */
export const email = {
    validator: function (rule, value, callback) {
        /* JavaScript 中 number 0 string '' null undefined 都是假值 */
        /* 保持结构的扁平 */
        if (!value) {
            callback();
        } else if (!isEmail(value)) {
            callback(("请输入正确的电子邮件地址"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};
//银行卡号验证
export const bankCardNumber = {
    validator: function (rule, value, callback) {
        var exp = /^([1-9]{1})(\d{11}|\d{12}|\d{13}|\d{14}|\d{15}|\d{16}|\d{17}|\d{18}|\d{19}|\d{20})$/;
        if (!value) {
            callback();
        } else if (!exp.test(value)) {
            callback(("请输入12至21位银行卡号"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};
//数字验证
export const number = {
    validator: function (rule, value, callback) {
        var exp = /^(0|[1-9]\d*)$/;
        if (!value) {
            callback();
        } else if (!exp.test(value)) {
            callback(("请输入正确的数字"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};


export const positiveNumber = {
    validator: function (rule, value, callback) {
        var exp = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/;
        if (!value) {
            callback();
        } else if (!exp.test(value)) {
            callback(("请输入正确的数字"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};

//金额验证(小数点后六位)
export const money = {
    validator: function (rule, value, callback) {
        var exp = /^(([1-9]\d*)|\d)(\.\d{1,4})?$/;
        if (!value) {
            callback();
        } else if (!exp.test(value)) {
            callback(("金额格式不对"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};
//地址区县市验证
export const address = {
    validator: function (rule, value, callback) {
        var exp = /[区|县|市|级]$/;
        if (!value) {
            callback();
        } else if (!exp.test(value)) {
            callback(("请填入详细地址信息(xxx市xxx区xxx街道)"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};


/* 设备序列号校验 */
export const deviceserialno = {
    validator: function (rule, value, callback) {
        var exp = /^[A-Za-z0-9]+$/;
        if (!value) {
            callback();
        } else if (!exp.test(value)) {
            callback(("参数不正确,只能输入最大50位的字母或数字"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};

/* 经度校验 */
export const Longitude = {
    validator: function (rule, value, callback) {
        var exp = /^(\-|\+)?(((\d|[1-9]\d|1[0-7]\d|0{1,3})\.\d{0,15})|(\d|[1-9]\d|1[0-7]\d|0{1,3})|180\.0{0,15}|180)$/;
        if (!value) {
            callback();
        } else if (!exp.test(value)) {
            callback(("经度整数部分为0-180,小数部分为0到15位!"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};

/* 纬度校验 */
export const Latitude = {
    validator: function (rule, value, callback) {
        var exp = /^(\-|\+)?([0-8]?\d{1}\.\d{0,15}|90\.0{0,15}|[0-8]?\d{1}|90)$/;
        if (!value) {
            callback();
        } else if (!exp.test(value)) {
            callback(("纬度整数部分为0-90,小数部分为0到15位!"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};

/* 中文 */
export const isChinese = {
    validator: function (rule, value, callback) {
        var exp = /^[a-zA-Z\u4e00-\u9fa5]+$/;
        if (!value) {
            callback();
        } else if (!exp.test(value)) {
            callback(("只能输入中文或者英文!"));
        } else {
            callback();
        }
    },
    trigger: ["blur", "change"]
};
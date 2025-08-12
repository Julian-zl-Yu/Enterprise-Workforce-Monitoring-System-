import {
    isNumberWhenInputing
} from "@/utils/validate.ts";
import {
    debounceEnsureDot,
    debounceCheckNumberByFormatter
} from "./checkNumber";

export function handleNumberChange(val) {
    /* 有可能是xxx.这种【.】结尾的字符串  允许在输入过程中存在*/
    if (isNumberWhenInputing(val)) {
        /* item自己有校验调用自己的formatter设置val */
        if (this.formatter) {
            debounceCheckNumberByFormatter(val, this);
        } else {
            debounceEnsureDot(val, this);
        }
        this.$emit("change", val);
    }
}

export function handleMoneyChange(val) {
    if (isNumberWhenInputing(val)) {
        /* 出现 1. 点后面没有数字的情况：1000*/
        debounceEnsureDot(val, this);
        this.$emit("change", val);
    }
}

export function handleValueChange(val) {
    this.$emit("change", val);
}
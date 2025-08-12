import _debounce from "lodash/debounce";

/* 确保是数字 12341234.12341243  非 12341234.、 00009012341234. 、12341234.34340000*/
function checkNumberEnsureDot(numberValue, vm) {
    const sArray = String(numberValue).split(".");
    /*整数部分不能以0开头；如果有小数点，后面的数字不能为空 最后一位不能是0*/
    let isValidNumber = false;
    if (sArray.length === 1) {
        isValidNumber = (/^0$|^[1-9]\d*$/g.test(sArray[0]));
    } else if (sArray.length === 2) {
        isValidNumber = /^0$|^[1-9]\d*$/g.test(sArray[0]) && /^\d*[1-9]$/g.test(sArray[1]);
    }

    if (!isValidNumber) {
        vm && vm.$emit("change", "");
    }
    return isValidNumber;
}

function emitChange(val) {
    this.$emit("change", val);
}


function checkNumberByFormatter(valueNumber, vm) {
    /* debounce 方式,如果是数字再转换 */
    if (checkNumberEnsureDot(valueNumber, vm)) {
        valueNumber = Number(valueNumber);
        vm.formatter(valueNumber, emitChange.bind(vm));
    }
}

export const debounceEnsureDot = _debounce(checkNumberEnsureDot, 1000);
export const debounceCheckNumberByFormatter = _debounce(checkNumberByFormatter, 1000);
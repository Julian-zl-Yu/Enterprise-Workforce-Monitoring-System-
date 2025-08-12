
export function isInput(value: any): boolean {
    /* 为0，亦为已输入 */
    if (0 === value) {
        return true;
    } else if (!value) {
        /* 除0以外为假值则为未输入*/
        return false;
    } else {
        return true;
    }
}
/**
 * 邮箱
 * @param {*} s
 */
export function isEmail(s: string) {
    return /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(s);
}

/*在输入的时候保证xxxxx.xxxxx x是数字即可*/
export function isNumberWhenInputing(s: string) {
    /* TODO: 数字0 */
    return /^\d*(\.\d*)?$/g.test(s);
}

/**
 * 手机号码
 * @param {*} s
 */
export function isMobile(s: string) {
    return /^1[0-9]{10}$/.test(s);
}

/**
 * 电话号码
 * @param {*} s
 */
export function isPhone(s: string) {
    return /^([0-9]{3,4}-)?[0-9]{7,8}$/.test(s);
}

/**
 * URL地址
 * @param {*} s
 */
export function isURL(s: string) {
    return /^http[s]?:\/\/.*/.test(s);
}

export function isBlobURL(s: string) {
    return /^blob:http:/.test(s);
}

export function isRelativeURL(s: string) {
    return /^\.\//.test(s);
}

/* 身份证 */
export function isIDCard(s: string) {
    return /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(s);
}

/* 社会统一信用代码 */
export function isCorpcode(s: string) {
    return /[^_IOZSVa-z\W]{2}\d{6}[^_IOZSVa-z\W]{10}/g.test(s);
}
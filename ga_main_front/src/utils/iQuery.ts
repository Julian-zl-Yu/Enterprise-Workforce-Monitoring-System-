export function on(el, eventName, fn) {
    el.addEventListener(eventName, fn);
}

export function off(el, eventName, fn) {
    el.removeEventListener(eventName, fn);
}

export function isObjectEqual(a, b) {
    if (a === void 0) a = {};
    if (b === void 0) b = {};

    // handle null value #1566
    if (!a || !b) {
        return a === b;
    }
    var aKeys = Object.keys(a);
    var bKeys = Object.keys(b);
    if (aKeys.length !== bKeys.length) {
        return false;
    }
    return aKeys.every(function (key) {
        var aVal = a[key];
        var bVal = b[key];
        // check nested equality
        if (typeof aVal === "object" && typeof bVal === "object") {
            return isObjectEqual(aVal, bVal);
        }
        return String(aVal) === String(bVal);
    });
}

/**/
export const $id = document.getElementById.bind(document);
export const $$ = document.querySelector.bind(document);
export const $c = document.createElement.bind(document);
export const $append = document.body.append.bind(document.body);
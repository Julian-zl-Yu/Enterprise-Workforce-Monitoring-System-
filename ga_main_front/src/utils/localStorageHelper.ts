// @ts-nocheck
import qs from "qs";
import forEach from "lodash/forEach";

const prefix = "gkcloudls_";

type typeProperty = "token" | "user";

export default {
    get(property: typeProperty) {
        return qs.parse(localStorage[`${prefix}${property}`]).value;
    },
    set(property: typeProperty, value) {
        localStorage[`${prefix}${property}`] = qs.stringify({
            value
        });
        return true;
    },
    clear() {
        let reg = new RegExp(`^${prefix}`, "g");
        forEach(localStorage, (value, keyIndex) => {
            const key = localStorage.key(keyIndex);
            if (reg.test(key)) {
                delete localStorage[key];
            }
        });
    }
};
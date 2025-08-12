import isFunction from "lodash/isFunction";

export default class Resolver {
    constructor() {
        this.deep = [];
    }

    push(resolve) {
        this.deep.push(resolve);
    }

    trigger(data, callback) {
        this.deep.forEach(resolve => resolve(data));
        isFunction(callback) && callback();
    }
}
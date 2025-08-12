import $ from "jquery";
import isString from "lodash/isString";
export function makeEleRectFull(idSelectorOrEle) {
    try {
        var ele = isString(idSelectorOrEle) ? document.getElementById(idSelectorOrEle) : idSelectorOrEle;
        var $parent = $(ele).parent();
        var parentDom = $parent[0];
        var parentRect = parentDom.getBoundingClientRect();
        ele.style.width = parentRect.width + "px";
        ele.style.height = parentRect.height + "px";
        return ele;
    } catch (error) {
        console.log(error);
    }
}
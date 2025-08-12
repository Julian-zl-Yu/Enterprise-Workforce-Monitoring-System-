import {
    getUUID
} from "@/utils/index.ts";
import {
    on,
    off
} from "@/utils/iQuery";
import $ from "jquery";

let isInTipsAreaAreaArea = false;
let currentTipsIndex = 0;

$("body")
    /* 短时间内鼠标移动到tips内部 */
    .delegate(".vl-notify.vl-notify-tips.is-direction", "mouseenter", function () {
        isInTipsAreaAreaArea = true;
    })
    .delegate(".vl-notify.vl-notify-tips.is-direction", "mouseleave", function () {
        isInTipsAreaAreaArea = false;
        window.utils.layer.close(currentTipsIndex);
    });

/* showOverflowTooltip */
function handleMouseleave(event) {
    isInTipsAreaAreaArea = false;
    const el = event.target;
    if (!el.tipsId) return;
    currentTipsIndex = el.tipsId;
    setTimeout(() => {
        if (isInTipsAreaAreaArea) return;
        window.utils.layer.close(currentTipsIndex);
    }, 300);
}

/* showOverflowTooltip */
function handleTipsMouseEnter(event) {
    /* fixed:垂直方向 不触发关闭tips */
    window.utils.layer.closeAll();
    isInTipsAreaAreaArea = true;
    const el = event.target;
    if (!el.dataset.show) return;
    if (!el.dataset.tips) return;
    const elP = event.target.parentElement;
    if (elP.className === "form-item all-item wrapper") {
        /* 在form-item wrap之内，只有超过本身长度才提示 */
        const elNextElementSibling = el.nextElementSibling;
        /* 16是一个经验值，保证内容出现省略号时能实现tips展示 */
        if (elNextElementSibling.offsetWidth <= (el.offsetWidth - 16)) return;
    }
    el.tipsId = window.utils.layer.tips(el.dataset.tips, `#${el.id}`, {
        tips: 0,
        time: 1000,
        noClass: true,
        isDirection: "is-direction",
        triangle: "top"
    });
}

export default {
    name: "tips",
    body: {
        bind(el) {
            el.id = `tips${getUUID()}`;
            el.__handleTipsMouseEnter = handleTipsMouseEnter;
            el.__handleMouseleave = handleMouseleave;
            on(el, "mouseenter", handleTipsMouseEnter);
            on(el, "mouseleave", handleMouseleave);
        },
        unbind(el) {
            off(el, "mouseenter", el.__handleTipsMouseEnter);
            off(el, "mouseleave", el.__handleMouseleave);
            delete el.__handleTipsMouseEnter;
            delete el.__handleMouseleave;
        },
    }
};
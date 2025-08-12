// @ts-nocheck
import {
    $$,
    $c,
    $append
} from "@/utils/iQuery";

let styleDG = false;
let currentHeight = 0;

export default function () {
    if (!styleDG) {
        styleDG = $$("#style-dynamic-group");
    }
    if (!styleDG) {
        styleDG = $c("style");
        styleDG.id = "style-dynamic-group";
        $append(styleDG);
    }
    let targetEle = $$(".app-content.app-content--tabs>div.el-tabs.el-tabs--top>div.el-tabs__content");
    // const tWidth = targetEle["clientWidth"];
    if (!targetEle) return;
    const tHeight = targetEle.clientHeight;
    if (currentHeight === tHeight) return;
    currentHeight = tHeight;
    styleDG.innerHTML = `
        .main-page, .sub-page{
            height:${currentHeight}px;
        }
    `;
}
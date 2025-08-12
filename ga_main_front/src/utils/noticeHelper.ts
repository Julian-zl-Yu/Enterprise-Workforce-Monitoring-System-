import $ from "jquery";

export const addNotice = (selector = "#test", fn) => {
    let $ele = $(selector);
    if (!$ele[0]) return;
    if (fn) {
        fn();
    } else {
        $ele.addClass("error-notice");
    }
};
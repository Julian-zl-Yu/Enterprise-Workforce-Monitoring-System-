import $ from "jquery";
export const addAnimate = (selector = "#test", animation = "shakeX", prefix = "animate__") => {
    return new Promise((resolve) => {
        const element = $(selector);
        if (!element[0]) return;
        const animationName = `${prefix}${animation}`;
        const elementClass = `${prefix}animated ${animationName}`;

        function handleAnimationEnd() {
            element.removeClass(elementClass);
            element.off("animationend", handleAnimationEnd);
            resolve("Animation ended");
        }

        element.on("animationend", handleAnimationEnd);
        element.addClass(elementClass);
    });
};
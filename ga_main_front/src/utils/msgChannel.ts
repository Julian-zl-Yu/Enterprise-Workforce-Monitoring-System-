import $ from "jquery";

export function msgChannel(id, options, {
    onmessage,
    onmessageerror
}) {
    const {
        MessageChannel
    } = window;
    options.id = `message-channel-${id}`;
    const privateIframe = $("<iframe/>", options);
    const {
        port1,
        port2
    } = new MessageChannel();
    port1.onmessage = onmessage || console.log;
    port1.onmessageerror = onmessageerror || console.error;
    privateIframe.on("load", () => {
        privateIframe[0].contentWindow.postMessage(
            `${options.id}`,
            "*",
            [port2]
        );
    });
    privateIframe.appendTo($("body"));
    return port1;
}
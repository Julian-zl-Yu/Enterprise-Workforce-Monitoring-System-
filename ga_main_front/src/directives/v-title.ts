export default {
    name: "title",
    body: {
        inserted(el) {
            document.title = el.dataset.title;
        }
    }
};
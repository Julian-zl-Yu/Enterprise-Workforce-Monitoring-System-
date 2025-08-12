export const defaultFormQueryData = {
    title: ""
};
export const defaultFormData = {
    title: "",
    sort: "",
    pubDate: "",
    createDate: ""
};

export let state = {
    state: {
        formQueryData: {
            ...defaultFormQueryData
        },
        formData: {
            ...defaultFormData
        }
    },
    getters: {},
    mutations: {},
    actions: {}
};

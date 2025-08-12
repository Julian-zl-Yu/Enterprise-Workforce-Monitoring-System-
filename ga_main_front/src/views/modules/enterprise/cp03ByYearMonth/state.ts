import dayjs from "dayjs";

export const defaultFormQueryData = {
    year: dayjs().format("YYYY")
};

export const defaultFormData = {
    
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
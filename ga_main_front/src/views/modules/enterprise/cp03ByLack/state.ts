import dayjs from "dayjs";

export const defaultFormQueryData = {
    yearMonth: dayjs().add(-1, 'month').format("YYYY-MM")
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
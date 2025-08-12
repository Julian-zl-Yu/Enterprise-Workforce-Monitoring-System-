import {toDate} from "../../../../utils/transformData";

export const defaultFormQueryData = {
	// years: toDate(Date.now(), "YYYYMM"),
	result: "",
	cp1101: ""
};

export let state = {
	state: {
		formQueryData: {
			...defaultFormQueryData
		}
	},
	getters: {},
	mutations: {},
	actions: {}
};
export const defaultFormQueryData = {
	corpname: "",
	corptype: ""
};


export const defaultFormData = {
	"corptype": "",
	pmName: '',
	pmIdCardType: '1',
	pmIdCardNumber: '',
	"cp0101": "",
	"cp01DTO": {
		"address": "",
		"areacode": "",
		"businessstatus": "",
		"capitalcurrencytype": "CNY",
		"corpcode": "",
		"corpname": "",
		"corptype": "",
		"cp0101": "",
		"email": "",
		"establishdate": "",
		"faxnumber": "",
		"idcardtype": "1",
		"legalman": "",
		"legalmanidcardnumber": "",
		"linkcellphone": "",
		"linkman": "",
		"linkphone": "",
		"memo": "",
		"officephone": "",
		"regcapital": "",
		"website": "",
		"zipcode": "",
		"entscope": "",
		"regdept": "",
		"organizationtype": ""
	},
	"cp0201": "",
	"pj0101": ""
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

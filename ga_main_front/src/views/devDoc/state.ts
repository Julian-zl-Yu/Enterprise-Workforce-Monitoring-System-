import http from "@/utils/http";
import {
    Position
} from "@/components/Form/FormItem/implementFormItem/PositionPicker/Position";

import dayjs from "dayjs";

export const defaultFormData = {
    "tagno": "130203",
    "status": "statu阿什顿发",
    "pj0101": "3",
    "model": "5",
    "devicename": "devicename",
    "trademark": "trademark",
    "devicetype": {
        "longitude": 104.072601,
        "latitude": 30.663375,
        "address": "四川省成都市青羊区太升路街道安逸158酒店(顺城店)鼓楼南街174号院",
        "label": ""
    },
    "dischargestage": "dischargestage",
    "manufacturer": "231234123441234.34",
    "enginetype": "enginetype",
    "factorymonthly": dayjs().format("YYYY-MM-DD"),
    "propertyunit": dayjs().add(1, "day").format("YYYY-MM-DD"),
"creditcode"
:
"creditcode",
    "memo"
:
"memoasd",
    "creator"
:
"creator",
    "createDate"
:
"createDate",
    "updater"
:
"updater",
    "updateDate"
:
"updateDate"
}
;

export let state = {
    state: {
        formQueryData: {
            /* 项目名称 */
            name: ""
        },
        formData: {
            ...defaultFormData
        }
    },
    getters: {},
    mutations: {},
    actions: {}
};
// @ts-nocheck
import { API_URL } from "@/main.config";
/*
* 不使用诸如0 假值
* */
export enum BTN_TYPE {
    /*保存*/
    "save" = 1,
    "print",
    "print_preview",
    "upload",
    "import",
    "download",
    "export",
    "preview",
    "decrease",
    "increase",
    "detail",
    "delete",
    "add",
    "create",
    "edit",
    "view",
    "update",
    "query",
    "clear",
    "lastStep",
    "close",
    "cancel",
    "back",
    "login",
    "generate",
    "restart",
    "snap",
    "viewImg",
    "disBind",
    "refresh",
    'batchOut',
    'issue',
    'videoUrl',
    'viewyj',
    "enter",
    "contractExport",
    "kqDetail"
}

/*按钮组*/
export enum BTN_GROUP_TYPE {
    /*列表外的按钮*/
    "Btn" = 1,
    /*列表内的按钮*/
    "Btnmini"
}

/*提示信息*/
export const enum STATUS_TYPE {
    /*请求发送中*/
    pendding = "pendding",
    /*发送完成*/
    done = "done",
    /*成功*/
    success = "success",
    /*警告*/
    warning = "warning",
    /*提示信息*/
    info = "info",
    /*错误*/
    error = "error",
}

export const enum FORM_ITEM_TYPE {
    /*Input 0 是假值，是默认类型,新添加的类型必须在Input之后*/
    "Input",
    "InputFocus",
    "Textarea",
    "Cascader",
    "Select",
    "Dict",
    "Dept",
    "Radio",
    "PassOrNot",
    "RYGcode",
    "Status",
    "Areacode",
    "Region",
    "DatePicker",
    "DateRange",
    "InputNumber",
    "InputCardReader",
    /*地理位置选择器 */
    "PositionPicker",
    "Money",
    "Avatar",
    "SMScode",
    "InputBtn"
}

/* 币种 */
export const enum CURRENCY_TYPE {
    "RMB" = 1,
    "USD" = 2,
    "JPY" = 3,
    "EUR" = 4,
    "HKD" = 5
}


export const enum FORM_ITEM_CONFIGS_TYPE {
    /*列表过长 tips 弹出框*/
    "showOverflowTooltip" = 1
}

/*全局变量——第三方库 全局引用*/
export const enum GLOBAL_LIB {
    /* 高德地图 */
    "AMap" = "AMap",
    "AMapUI" = "AMapUI",
}

/*行政区划类型*/
export const enum REGION_TYPE {
    /* 全国 */
    "all" = 1
}

/*文件类型*/
export const enum FILE_TYPE {
    /* png image */
    "pdf",
    "img",
}

export const DISCARD_DB_NAME_ARRAY = ["mydb", "mydb1"];
/*indexedDB 数据库*/
export const enum DB_NAME {
    "mydb" = "cloud"
}

export const enum DB_TB_NAME {
    "areacode" = "areacode",
    /*key-value 键值对*/
    "kv" = "kv",
    /*用作缓存的table 每次进入系统清除*/
    "kvCache" = "kvCache",
    "formDataCache" = "formDataCache"
}

export const enum DB_TB_kv_key {
    "AREACODE_VERSION" = "AREACODE_VERSION",
    "AREA_CODE_TREE" = "AREA_CODE_TREE",
    "AREA_CODE_ORIGIN" = "AREA_CODE_ORIGIN"
}


/*localStorage key*/
export const enum LS_KEY {
    /* png image */
    "token" = "token",
    "user" = "user",
    "isNamed" = 'isNamed',
    "cp0101" = 'cp0101',
}

/* 常用的formitem */
export let pj0101 = {
    label: "项目名称",
    prop: "pj0101",
    type: FORM_ITEM_TYPE.Select,
    lazyLoad: true,
    bindUrl: API_URL.getProjectDict
};

export let yearsYYYYMM = {
    prop: "years",
    label: "年月",
    type: FORM_ITEM_TYPE.DatePicker,
    valueFormat: "yyyyMM",
    dateType: "month",
    format: "yyyy-MM"
};

/* 上传允许的类型 */
export const enum FILE_TYPES {
    png = "image/png",
    jpg = "image/jpeg",
    pdf = "application/pdf",
}

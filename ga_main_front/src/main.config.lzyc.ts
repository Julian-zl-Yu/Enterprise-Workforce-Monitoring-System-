export const API_URL = {
	/* 权限 */
	permissions: "/sys/menu/permissions",
	userInfo: "/sys/user/info",
	/*文件上传*/
	upload: "/enterprise/ot01/upload",
	/*删除*/
	delete: "/enterprise/ot01",
	/*行政区划 版本*/
	getAreaCodeVersion: "/sys/region/regionVersion",
	/*行政区划 原始数据*/
	getAreaCodeOrigin: "/sys/region/regionUpdate",
	/* 身份证阅读器 */
};

export let REMOTE_HOST_MAP = {
	/* 开发环境 */
	"dev": {
		api: "http://localhost:9000",
		// api: "http://localhost:9000",
		socket: "ws://localhost:9000/websocket"
	},
	/*  集成测试环境 */
	"prod:sit": {
		api: "http://localhost:9000",
		socket: "ws://localhost:9000/websocket"
	},
	/*  验收测试环境 */
	"prod:uat": {
		api: "http://localhost:9000",
		socket: "ws://localhost:9000/websocket"
	},
	/*  生产环境 */
	"prod": {
		api: "https://116.63.146.210:9000",
		socket: "ws://116.63.146.210:9000/websocket"
	},
};

export const SITE_CONFIG_PRIVATE = {
	/* AreaCode 默认数据 */
	AREA_CODE_DEFAULT: "510500",
};
import {
	phone,
	required,
    number,
    length
} from "../../../../utils/formRule";
import {FORM_ITEM_TYPE, yearsYYYYMM} from "../../../../components/Types";

export const MODULE_BASE_URL = "/enterprise/cp11";

/* 列表 表头 */
export let DSLTableHeader = [
    {prop: "corpcode", label: "社会统一信用代码"}
    , {prop: "corpname", label: "企业名称"}
    , {prop: "areacode", label: "注册地区", type: FORM_ITEM_TYPE.Cascader,bindId: "areacode.china"}
    , {prop: "industry", label: "行业",type: FORM_ITEM_TYPE.Dict, bindId: "INDUSTRY"}
    , {prop: "estate", label: "产业",type: FORM_ITEM_TYPE.Dict, bindId: "ESTATE"}
    , {prop: "scale", label: "企业规模",type: FORM_ITEM_TYPE.Dict, bindId: "SCALE"}
    , {prop: "linkcellphone", label: "联系人手机号码"}
    , {prop: "msg", label: "导入结果"}

];

/* 查询框 */
export let DSLFormItemsQuery = {
  
};

/* 详情表单 */
export let DSLFormItems = {

};


/* 详情表单 验证规则 */

export let DSLFormItemRule = {

};
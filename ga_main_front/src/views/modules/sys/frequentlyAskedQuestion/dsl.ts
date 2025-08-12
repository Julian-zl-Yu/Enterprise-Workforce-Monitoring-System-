import {
    required
} from "@/utils/formRule";
import { FORM_ITEM_TYPE } from "@/components/Types";

export const MODULE_BASE_URL = "/demo/news";
export let DSLCopyFormItemsQuery = []

/* 列表 表头 */
export let DSLTableHeader = [
    {
        prop: "title",
        label: "标题",
    },
    {
        prop: "sort",
        label: "排序"
    },
    {
        prop: "pubDate",
        label: "发布时间"
    },
    {
        prop: "createDate",
        label: "创建时间"
    },
];

/* 查询框 */
export let DSLFormItemsQuery = {
    title: {
        span: 8,
        prop: "title",
        label: "标题"
    }
};

/* 详情表单 */
export let DSLFormItems = {
    title: {
        label: "标题",
        prop: "title"

    },
    content: {
        label: "内容",
        prop: "content"
    },
    sort: {
        label: "排序",
        prop: "sort"
    },
    pubDate: {
        label: "发布时间",
        prop: "pubDate"
    },
};


/* 详情表单 验证规则 */

export let DSLFormItemRule = {
    title: [required],
    content: [required],
    sort: [required],
    pubDate: [required]
};

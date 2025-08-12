<template>
    <SubPage>
        <Card v-show="$route.query.s==='list'">
            <div class="option-btn-wrapper">
                <BtnGroup :configs="configsBtns"/>
            </div>
            <el-table v-loading="dataListLoading" :data="dataList" border style="width: 100%">
                <template v-for="header in DSLTableHeader">
                    <TableColumn :key="header.prop" :configs="header"/>
                </template>
                <el-table-column
                        :label="$t('handle')"
                        fixed="right"
                        header-align="center"
                        align="center"
                        width="150"
                >
                    <template slot-scope="scope">
                        <BtnGroup
                                :configs="configsListBtns"
                                :scope="scope"
                        />
                    </template>
                </el-table-column>
            </el-table>
            <Paginationbar v-model="pageInfo" @query="query"/>
        </Card>

        <template slot="footer">
            <Btn :configs="{ type: BTN_TYPE.back, click: handleClickBack }"/>
            <Btn v-if="$route.query.s==='listadd'" ref="submitBtn" :configs="configsBtnDebounce"/>
            <Btn v-if="$route.query.s==='listupdate'" ref="submitBtn" :configs="configsBtnDebounce"/>
        </template>
        <el-dialog title="导入实发" :visible.sync="dialogFormVisible" v-loading="loading" element-loading-text="上传中">
            <el-upload
                    class="upload-demo"
                    drag
                    action="https://jsonplaceholder.typicode.com/posts/"
                    :auto-upload="false"
                    :limit="1"
                    :on-change="handleChange"
                    :file-list="fileList"
                    multiple>
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">只能上传xlsl、xls</div>
            </el-upload>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitUpload">确 定</el-button>
            </div>
        </el-dialog>
    </SubPage>
</template>

<script>
    import basePage from "@/mixins/basePage.ts";
    import {mixinsSubPage, subPropCurrentView} from "@/mixins/subPage.ts";
    import {SuccessOrFailed, checkFormData} from "@/utils/index.ts";
    import {defaultFormData} from "./state";
    import {BTN_TYPE, BTN_GROUP_TYPE} from "@/components/Types";
    import {exportHandler, getPageInfo} from "../../../../mixins/mainPage";
    import {FORM_ITEM_TYPE} from "../../../../components/Types";

    /*模块名*/
    const moduleName = "Enterpriseps11";

    export default {
        name: "Enterpriseps11Sub",
        mixins: [basePage, mixinsSubPage, subPropCurrentView],
        data() {
            var _this = this;
            const handleFormSubmit = this.handleFormSubmit.bind(this);
            return {
                dialogFormVisible: false,
                dataListLoading: false,
                dataList: [],
                DSLTableHeader: [
                    {prop: "name", label: "工人名字"}
                    , {prop: "personType", label: "人员类型", type: FORM_ITEM_TYPE.Dict, bindId: "PERSONTYPE"}
                    , {
                        prop: "contractperiodtype",
                        label: "支付类型",
                        type: FORM_ITEM_TYPE.Dict,
                        bindId: "CONTRACTPERIODTYPE"
                    }
                    , {prop: "shouldMoney", label: "应发数(元)"}
                    , {prop: "shouldTime", label: "应发时间"}
                    , {prop: "banknumber", label: "银行卡号"}
                    , {prop: "bankcode", label: "银行名称", type: FORM_ITEM_TYPE.Dict, bindId: "PAY_BANK_CODE"}
                    , {prop: "remark", label: "备注"}
                ],
                configsBtns: {
                    btnType: BTN_GROUP_TYPE.Btn,
                    btnList: [
                        {
                            type: BTN_TYPE.add,
                            // shiro: "enterprise:tm01:save",
                            click: () => _this.addMethods(),
                        }
                    ],
                },
                configsSubPage: {moduleName},
                /*提交按钮*/
                configsBtnDebounce: {
                    type: BTN_TYPE.save,
                    textLoading: "正在提交...",
                    debounceClick: handleFormSubmit,
                },
                formData: window._.cloneDeep(defaultFormData),
                pageInfo: {
                    page: 1,
                    limit: 10,
                    total: 0,
                    cp1101: "",
                },
                /*列表内操作按钮*/
                configsListBtns: {
                    btnType: BTN_GROUP_TYPE.Btnmini,
                    btnList: [
                        {
                            type: BTN_TYPE.update,
                            // shiro: "enterprise:bbkq08:info",
                            click: (scope) => {
                                _this.goSub({v: "detail", s: "listupdate", id: scope.row.pa0301})
                            },
                        },
                    ],
                },
            };
        },

        created() {
            this.init();
        },
        methods: {
            submitUpload() {
              alert(1);
            },
            downTemplate() {
                exportHandler(
                    '/enterprise/ps11/export',
                    window._.merge({ ...this.formDataQuery }),
                    '企业应发工资'
                );
            },
            addMethods() {
                // this.goSub({v: "detail", s: "listadd"});
                this.dialogFormVisible = true;
            },
            handleClickBack() {
                if (this.$route.query.s === "listadd") {
                    this.goSub({v: 'detail', s: 'list'});
                    return;
                }
                if (this.$route.query.s === "listupdate") {
                    this.goSub({v: 'detail', s: 'list', id: this.$route.query.id});
                    return;
                }


                if (this.$route.query.s === "list") {
                    this.goMain();
                    return;
                }
            },
            async query() {
                let {data: res} = await this.$http.get("enterprise/bpa03/page", {
                    params: this.pageInfo,
                });
                SuccessOrFailed(
                    res,
                    {
                        fn: () => {
                            this.dataList = res.data.list;
                            this.pageInfo.total = res.data.total;
                        },
                    },
                    {msg: res.msg}
                );
            },
            init() {
                if (["update", "detail"].includes(this.currentView)) {
                    this.pageInfo.cp1101 = this.$route.query.id;
                    this.query();
                    // if ("detail" === this.currentView) {
                    //     this.configsCForm.readOnly = true;
                    // }
                    return;
                }
            },
        },
    };
</script>

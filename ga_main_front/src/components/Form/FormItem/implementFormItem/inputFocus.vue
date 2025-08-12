<template>
  <fragment>
    <!--isEdit {{ isEdit }} -->
    <!--canEdit {{canEdit}}-->
    <template v-if="canEdit">
      <el-input
        ref="formItem"
        clearable
        v-bind="$attrs"
        v-model="value1"
        :placeholder="attrPlaceholder"
        @focus="handelFocus"
        :disabled="attrDisabled"
        @input="handleValueChange"
      />
      <div v-show="showDialog">
        <el-dialog
          title="工人"
          :visible.sync="dialogTableVisible"
          append-to-body
          highlight-current-row
        >
          <CFormQuery
            class="card"
            v-model="formDataQuery"
            :configs="configsCFormQuery"
            @query="getTableData(1)"
          />
          <el-table
            v-loading="isLoading"
            :data="tableData"
            border
            @row-click="rowClick"
            style="width: 100%"
            height="500"
          >
            <!-- 选择列 -->
            <template v-for="header in DSLTableHeader">
              <TableColumn :key="header.prop" :configs="header" />
            </template>
          </el-table>
          <Paginationbar
            v-model="pageInfo"
            :page-sizes="[10, 100, 1000]"
            @query="getTableData"
          />
        </el-dialog>
      </div>
    </template>
    <FormItemReadonly
      v-else
      :prop-class="'text-readonly'"
      :is-table="isTable"
      :value="value"
    />
  </fragment>
</template>

<script>
import { FORM_ITEM_TYPE } from "@/components/Types";
import { handleValueChange } from "@/components/Form/FormItem/commonFormItem";
import { SuccessOrFailed } from "@/utils/index.ts";
import { getPageInfo } from "@/mixins/mainPage.ts";
import {
  model as baseModel,
  subCreated,
  subComputed,
  subProps,
  subComponents,
} from "@/components/Form/FormItem/mixinsFormItemBase";

/*disable是否禁用*/
export default {
  name: "ImplInputFocus",
  mixins: [subCreated, baseModel, subProps, subComputed, subComponents],
  computed: {
    inputType() {
      if (this.componentType === FORM_ITEM_TYPE.Textarea) {
        return "textarea";
      } else {
        return "text";
      }
    },
    attrShowPassword() {
      return this?.configs?.showPassword || false;
    },
  },
  watch: {
    value: {
      handler: function(val, oldVal) {
        this.value1 = this.value;
      },
    },
    deep: true,
  },
  data() {
    return {
      tableData: [],
      value1: "",
      showDialog: false,
      dialogTableVisible: false,
      isLoading: false,
      DSLTableHeader: [
        {
          prop: "tm0101",
          label: "所属班组",
          type: FORM_ITEM_TYPE.Select,
          bindUrl: "/common/getTeamInfo",
        },
        {
          prop: "personName",
          label: "姓名",
        },
        {
          prop: "idCardNumber",
          label: "身份证号码",
        },
      ],
      isTableColumnCheckboxDisabled: false,
      listSelectedRowIndex: [],
      pageInfo: getPageInfo(),
      formDataQuery: {
        personName: "",
        tm0101: "",
      },
      configsCFormQuery: {
        btnspan: 24,
        btnAlign: "right",
        DSLFormItems: {
          personName: {
            span: 8,
            prop: "personName",
            label: "姓名",
          },
          tm0101: {
            span: 8,
            prop: "tm0101",
            label: "班组",
            type: FORM_ITEM_TYPE.Select,
            bindUrl: "/common/getTeamInfo",
          },
        }, //查询表单的查询项描述
      },
    };
  },
  mounted() {
    this.getTableData();
  },
  updated() {},
  methods: {
    init() {},
    handleValueChange,
    async getTableData() {
      try {
        this.isLoading = true;
        let { data: res } = await this.$http.get(this.configs?.url, {
          params: { ...this.pageInfo, ...this.formDataQuery },
        });
        return SuccessOrFailed(
          res,
          {
            fn: () => {
              this.tableData = res.data.list;
              this.pageInfo.total = res.data.total;
            },
          },
          {
            msg: res.msg,
            fn: () => {
              this.tableData = [];
              this.pageInfo.total = 0;
            },
          }
        );
      } catch (e) {
        console.error(e);
      } finally {
        this.isLoading = false;
      }
    },
    handelFocus() {
      this.showDialog = true;
      this.dialogTableVisible = true;
    },
    rowClick(val) {
      this.showDialog = false;
      this.dialogTableVisible = false;
      this.handleValueChange(val.personName);
      this.value1 = val.personName;
      this.$store.state.ps0201 = val.ps0201;
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .card {
  margin: 24px 0 !important;
}
::v-deep .el-dialog {
  min-width: 820px;
}
::v-deep .has-gutter th {
  color: #303133 !important;
  background-color: #d6d7d8 !important;
}
::v-deep .el-pagination {
  text-align: center;
  margin-top: 20px;
}
::v-deep .el-pagination .el-icon-circle-close {
  display: none !important;
}
::v-deep .el-pagination .el-icon-circle-check {
  display: none !important;
}
</style>

<template>
  <div class="dynamic-row-list">
    <!-- {{ currentEditRowIndex }}-currentEditRowIndexCache-{{ currentEditRowIndexCache }} -->
    <!-- <pre> {{ JSON.stringify(options,null,2) }} </pre> -->
    <!-- selectedIndexArray-{{ dataListSelections }} -->
    <div class="option-btn-wrapper">
      <BtnGroup
        :configs="configsBtns"
        :can-delete="dataListSelections"
      />
    </div>
    <el-table
      :data="dataTableList"
      border
      style="width: 100%;"
    >
      <!-- 选择列 -->
      <TableColumnCheckbox
        ref="TableColumnCheckbox"
        v-model="dataListSelections"
        :disabled="tableColumnCheckboxDisabled"
      />
      <!-- 业务列 -->
      <TableColumn
        v-for="(header) in DSLTableHeader"
        :key="header.prop"
        :current-edit-row-index="currentEditRowIndex"
        :configs="header"
      />
      <!-- 固定列 -->
      <el-table-column
        :label="$t('handle')"
        fixed="right"
        header-align="center"
        align="center"
        width="150"
      >
        <template slot-scope="scope">
          <BtnGroup
            :configs="currentEditRowIndex===scope.$index?configsListBtnsEdit:configsListBtnsView"
            :scope="scope"
          />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import { BTN_GROUP_TYPE, BTN_TYPE, STATUS_TYPE } from "@/components/Types";
import merge from "lodash/merge";
import reduce from "lodash/reduce";
import last from "lodash/last";
import AsyncValidator from "async-validator";
import { animateCSS } from "@/utils/index.ts";
import { $$ } from "@/utils/iQuery";

const DEFAULT_CURRENT_EDIT_ROW_INDEX = -1;

export default {
  name: "DynamicRowList",
  model: {
    prop: "value",
    event: "change"
  },
  props: {
    value: {
      type: Array,
      default() {
        return [];
      }
    },
    options: {
      type: Object,
      required: true
    }
  },
  data() {
    const _this = this;
    return {
      tableColumnCheckboxDisabled: false,
      checkedRowArray: [],
      defaultRowData: {},
      currentEditRowIndexCache: {},
      currentEditRowIndex: DEFAULT_CURRENT_EDIT_ROW_INDEX,
      dataListSelections: [],
      configsListBtnsView: {
        btnType: BTN_GROUP_TYPE.Btnmini,
        btnList: [
          {
            type: BTN_TYPE.edit,
            click: scope => _this.handleClickEditRow(scope)
          },
          {
            type: BTN_TYPE.delete,
            click: scope => {
              _this.$refs.TableColumnCheckbox.handleChangeCheckAll(false);
              _this.dataTableList.splice(scope.$index, 1);
            }
          }
        ]
      },
      configsListBtnsEdit: {
        btnType: BTN_GROUP_TYPE.Btnmini,
        btnList: [
          {
            type: BTN_TYPE.cancel,
            click: scope => _this.handleCancelRowData(scope)
          },
          {
            type: BTN_TYPE.save,
            text: "确认",
            click: scope => _this.handleSaveRowData(scope.row, scope.$index)
          }
        ]
      },
      configsBtns: {
        btnType: BTN_GROUP_TYPE.Btn,
        btnList: [
          {
            type: BTN_TYPE.increase,
            disabled: false,
            text: "添加一行记录",
            click: () => _this.handleIncreaseRowData()
          },
          {
            type: BTN_TYPE.delete,
            click: () => _this.handleDeleteRowMultiple()
          }
        ]
      },
      dataList: []
    };
  },
  computed: {
    rules() {
      return this.options.rules || [];
    },
    dataTableList: {
      get() {
        return this.value;
      },
      set(val) {
        this.$emit("change", val);
      }
    },
    DSLTableHeader() {
      return this.options.DSLTableHeader || [];
    },
    DSLTableHeaderMap() {
      return this.DSLTableHeader.reduce((target, i) => {
        target[i.prop] = i;
        return target;
      }, {});
    }
  },
  created() {
    this.setDefaultRowData();
    this.$watch(
      "currentEditRowIndex",
      currentEditRowIndex => {
        const disabled = currentEditRowIndex !== DEFAULT_CURRENT_EDIT_ROW_INDEX;
        this.configsBtns.btnList[0].disabled = disabled;
        this.tableColumnCheckboxDisabled = disabled;
      },
      {
        immediate: true
      }
    );
  },
  methods: {
    handleClickEditRow(scope) {
      if (this.currentEditRowIndex !== DEFAULT_CURRENT_EDIT_ROW_INDEX) {
        return;
      }
      this.currentEditRowIndex = scope.$index;
      this.currentEditRowIndexCache = { ...scope.row };
    },
    handleDeleteRowMultiple() {
      this.dataTableList = this.dataTableList.filter((row, index) => {
        if (!~this.dataListSelections.indexOf(index)) {
          return row;
        }
      });
      this.$refs.TableColumnCheckbox.handleChangeCheckAll(false);
    },
    async handleSaveRowData(currentRowData, rowIndex) {
      try {
        await this.checkRowData(currentRowData);
        this.resetEditState();
      } catch (error) {
        this.handleError(error, rowIndex);
      }
    },
    async handleCancelRowData(scope) {
      /*还原之前的数据*/
      scope.row = merge(scope.row, this.currentEditRowIndexCache);
      /*检测行数据，如果不满足删除这一行*/
      try {
        await this.checkRowData(scope.row);
      } catch (error) {
        this.$refs.TableColumnCheckbox.handleChangeCheckAll(false);
        this.dataTableList.splice(scope.$index, 1);
      } finally {
        this.resetEditState();
      }
    },
    async handleIncreaseRowData() {
      try {
        /* 最后一行的数据必须满足required条件 */
        if (this.dataTableList.length > 0) {
          await this.checkRowData(last(this.dataTableList));
        }
        const dataTableList = [
          ...this.dataTableList,
          { ...this.defaultRowData }
        ];
        this.dataTableList = dataTableList;
        /* DONE: focus */
        const scope = {
          $index: dataTableList.length - 1,
          row: last(dataTableList)
        };
        this.handleClickEditRow(scope);
      } catch (error) {
        this.handleError(error, this.dataTableList.length - 1);
      }
    },
    async checkRowData(rowData) {
      if (!rowData) {
        return Promise.reject("未找到数据对象");
      } else {
        return await new Promise((resolve, reject) => {
          const descriptor = this.rules;
          new AsyncValidator(descriptor).validate(rowData, (errors, fields) =>
            errors ? reject(errors, fields) : resolve()
          );
        });
      }
    },
    handleError(tipsInfo, rowIndex) {
      tipsInfo.forEach(tip => {
        if (!tip.stack) {
          setTimeout(async () => {
            try {
              const classNameSelector = `[data-identify=dynamic-column-item-${tip.field}-${rowIndex}]`;
              const element = $$(classNameSelector);
              const i = this.DSLTableHeaderMap[tip.field];
              const message = `${i.label}: ${tip.message}`;
              this.$notify({
                title: "错误提示",
                message,
                type: STATUS_TYPE.error
              });
              await animateCSS(element, "shakeX");
            } catch (error) {
              console.log("error", error);
            }
          }, 300);
        } else {
          console.error(tip);
        }
      });
    },
    setDefaultRowData() {
      this.defaultRowData =
        this.options.defaultRowData ||
        reduce(
          this.DSLTableHeader,
          (target, item) => {
            target[item.prop] = "";
            return target;
          },
          {}
        );
    },
    resetEditState() {
      this.currentEditRowIndex = DEFAULT_CURRENT_EDIT_ROW_INDEX;
      this.currentEditRowIndexCache = {};
    },
    handleDataListSelectionChange(dataListSelections) {
      this.dataListSelections = dataListSelections;
    }
  }
};
</script>
<style lang="scss">
</style>
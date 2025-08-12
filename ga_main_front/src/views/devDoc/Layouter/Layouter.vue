<template>
  <div class="layouter">
    <!--  -->
    <ul class="item-list panel">
      <drag
        v-for="(item,key) in itemInfo"
        :key="key"
        :class="['item-wrapper',{'notice-unuse':getUsedTimes(item)<1}]"
        tag="li"
        :transfer-data="item"
      >
        {{ item.label }}
        <span
          v-show="getUsedTimes(item)>1"
          class="use-many-times"
        >use {{ getUsedTimes(item) }} times</span>
      </drag>
    </ul>
    <!-- 【】 -->
    <div class="table">
      <div class="panel table-options">
        <div
          v-for="(val,prop) in tableConfigs"
          :key="prop"
          class="config"
        >
          <span>{{ prop }}:</span>
          <el-input v-model="tableConfigs[prop]" />
        </div>
      </div>

      <table class="table-stage panel">
        <tr
          v-for="(rowItem, rowIndex) in dsl"
          :key="rowIndex"
        >
          <td class="remove-row">
            <el-button @click="removeRow(rowIndex)">
              -Row
            </el-button>
          </td>
          <drop
            v-for="(colItem,colIndex) in rowItem"
            :key="colItem.p||'empty'+colIndex"
            tag="td"
            :colspan="colItem.c||1"
            :rowspan="colItem.r||1"
            class="td-item"
            @drop="handleDrop({ rowIndex,colIndex},$event)"
          >
            <drag
              class="item-wrapper"
              tag="div"
              :transfer-data="{ rowIndex,colIndex}"
            >
              <span class="label">{{ itemInfo[colItem.p]&&itemInfo[colItem.p].label }}</span>
              <i
                class="el-icon-edit icon"
                @click="handleClickItem(colItem,{r:rowIndex,c:colIndex})"
              />
            </drag>
          </drop>
          <td class="add-col">
            <el-button @click="addCol(rowItem)">
              +Col
            </el-button>
            <el-button @click="removeCol(rowItem)">
              -Col
            </el-button>
          </td>
        </tr>
        <tr>
          <td class="add-row">
            <el-button @click="addRow">
              +Row
            </el-button>
          </td>
        </tr>
      </table>
    </div>
    <div class="json-panel">
      <div class="json-panel-item panel layout">
        <h6>布局</h6>
        <monaco v-model="dsl" />
      </div>
      <!-- item的信息，比如label -->
      <div class="json-panel-item panel item-info">
        <h6>所有item信息</h6>
        <monaco
          v-model="itemInfo"
          is-format="true"
          @change="handleItemInfoChange"
        />
      </div>
      <div class="json-panel-item panel col-item">
        <h6>单个item布局信息修改</h6>
        <monaco
          :value="itemDsl"
          is-format="true"
          @change="handleItemChange"
        />
      </div>
    </div>
  </div>
</template>
<script>
import Monaco from "./Monaco";
import _ from "lodash";
const cloneDeep = _.cloneDeep;

export default {
  name: "Layouter",
  components: { Monaco },
  data() {
    return {
      tableConfigs: {
        col: 3,
        row: 1,
      },
      itemInfo: {},
      currentrc: "",
      dsl: [[{ p: "" }, { p: "" }, { p: "" }]],
      itemDsl: {},
      targetText: "target source",
    };
  },
  computed: {
    flattendsl() {
      return _.flattenDeep(this.dsl).map((i) => i.p);
    },
  },
  methods: {
    getUsedTimes(item) {
      let count = 0;
      _.each(this.flattendsl, (prop) => {
        if (item.prop === prop) count++;
      });
      return count;
    },
    addCol(row) {
      row.push({ p: "" });
      const _list = cloneDeep(this.dsl);
      this.dsl = _list;
    },
    removeCol(row) {
      row.splice(row.length - 1);
      const _list = cloneDeep(this.dsl);
      this.dsl = _list;
    },
    addRow() {
      this.dsl.push(this.dsl[0]?.map(() => ({ p: "" })) || [{ p: "" }]);
    },
    removeRow(rowIndex) {
      this.dsl.splice(rowIndex, 1);
    },
    handleDrop(toItem, fromItem /* transfer-data */) {
      const _list = cloneDeep(this.dsl);
      let fromValue, toValue;
      if (_.has(fromItem, "label")) {
        /* 从列表选取，不交换 */
        fromValue = {
          p: fromItem.prop,
        };
      } else {
        /* 交换 */
        toValue = this.dsl[toItem.rowIndex][toItem.colIndex];
        fromValue = this.dsl[fromItem.rowIndex][fromItem.colIndex];
        _list[fromItem.rowIndex][fromItem.colIndex] = toValue;
      }
      _list[toItem.rowIndex][toItem.colIndex] = fromValue;
      this.dsl = _list;
    },
    handleItemInfoChange(itemInfo) {
      this.itemInfo = itemInfo;
      const dslArray = _.map(itemInfo, (item) => item.prop);
      const dsl = [];
      let row = [];
      let colCount = 0;

      while (dslArray.length > 0) {
        let itemProp = dslArray.pop();
        row.push({ p: itemProp });
        /* 该换行了 */
        if (++colCount >= this.tableConfigs.col) {
          dsl.push(row);
          row = [];
          colCount = 0;
        }
      }
      /* 最后一次没有在循环体中完成的push */
      if (colCount < this.tableConfigs.col) {
        dsl.push(row);
      }
      this.dsl = dsl;
    },
    handleItemChange(item) {
      this.itemDsl = item;
      const { r, c } = this.currentEditItemInfo;
      this.dsl[r][c] = item;
      const _list = cloneDeep(this.dsl);
      this.dsl = _list;
    },
    handleClickItem(item, info) {
      this.itemDsl = item;
      this.currentrc = `r${info.rowIndex}c${info.colIndex}`;
      this.currentEditItemInfo = info;
    },
  },
};
</script>
<style lang="scss" scoped>
.layouter {
  height: 100vh;
  width: 100%;
  overflow: hidden;
  display: flex;
  flex-flow: row nowrap;
  justify-content: center;
  align-items: center;

  .panel {
    border: 2px solid #303030;
    border-radius: 8px;
    margin: 10px;
  }
  .item-list {
    width: 10%;
    height: 100%;
    overflow: auto;
    margin: 10px;
    padding: 0;
    > li {
      &.notice-unuse {
        border-radius: 4px;
        border: 1px solid red;
        color: red;
      }
      span.use-many-times{
        color:orange;
        border-radius: 4px;
        border: 1px solid red;
        padding:2px;
        background: black;
      }
      margin: 10px;
      list-style: none;
      padding: 4px;
    }
  }
  .table {
    width: 55%;
    height: 100vh;
    float: left;
    display: flex;
    flex-flow: column nowrap;
    justify-content: flex-start;
    overflow: auto;
    .table-options {
      .config {
        display: flex;
        flex-flow: row nowrap;
        justify-content: center;
        align-items: center;
        padding: 10px;
        span {
          margin: 0 1rem;
        }
      }
    }
    .table-stage {
      flex: 1;
      tr {
        &:hover {
          background: #f0f9eb;
        }
      }
    }
    .td-item {
      width: 160px;
      font-size: 1rem;
      text-align: center;
      border: 1px solid #303030;

      .item-wrapper {
        display: flex;
        flex-flow: row nowrap;
        justify-content: space-between;
        align-items: center;
      }
    }
  }
  .json-panel {
    width: 35%;
    height: 100%;
    float: right;
    display: flex;
    flex-flow: column nowrap;
    justify-content: space-between;
    h6 {
      padding: 0;
      margin: 10px;
    }
    .json-panel-item {
      height: 50%;
      margin: 10px;
      padding: 10px;
      display: flex;
      flex-flow: column nowrap;
      &.layout {
        height: 20%;
      }
      &.item-info {
        height: 30%;
      }

      overflow: auto;
    }
    .textarea-panel {
      height: 100%;
      width: 100%;
    }
  }
}
</style>
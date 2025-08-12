<template>
  <MainPage id="id-dept-page" :configs="configsMainPage">
    <el-breadcrumb
      separator-class="el-icon-arrow-right"
      class="aui-card"
      style="margin-bottom:0;"
    >
      <el-breadcrumb-item
        v-for="(breadcrumb, index) in breadcrumbStack"
        :key="breadcrumb.name"
        :class="[
          breadcrumbStack.length - 1 === index
            ? 'current-breadcrumb'
            : 'data-breadcrumb',
        ]"
        @click.native="showData(breadcrumb, index)"
      >
        {{ breadcrumb.name }}
      </el-breadcrumb-item>
    </el-breadcrumb>
    <el-card
      v-show="currentView === 'main'"
      shadow="never"
      class="aui-card--fill"
    >
      <div class="mod-enterprise__ps02">
        <div class="option-btn-wrapper">
          <BtnGroup :configs="configsBtns" :can-delete="dataListSelections" />
        </div>
        <!-- <div class="table-wrapper"> -->
        <el-table
          v-if="isShowTable"
          v-loading="dataListLoading"
          :data="dataList"
          border
          ref="lazyTable"
          style="width: 100%;"
          height="500px"
        >
          <!-- 选择列 -->
          <el-table-column
            :label="$t('dept.name')"
            header-align="center"
            align="center"
          >
            <template slot-scope="scope">
              <div
                v-if="scope.row.subData"
                class="sub-title-wrapper link"
                @click="showSubData(scope.row)"
              >
                <span>></span>
                <span>{{ scope.row.name }}</span>
              </div>
              <div v-else class="sub-title-wrapper">
                <span>{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          <!-- 业务列 -->
          <template v-for="header in DSLTableHeader">
            <TableColumn :key="header.prop" :configs="header" />
          </template>
          <!-- 固定列 -->
          <el-table-column
            :label="$t('handle')"
            fixed="right"
            header-align="center"
            align="center"
            width="150"
          >
            <template slot-scope="scope">
              <BtnGroup :configs="configsListBtns" :scope="scope" />
            </template>
          </el-table-column>
        </el-table>
        <!-- </div> -->
      </div>
    </el-card>
    <AddOrUpdate
      v-if="isShowDetail"
      :is-show="isShowDetail"
      :row="rowInfo"
      @change="isShowDetail = $event"
    />
  </MainPage>
</template>

<script>
/* 混入基类方法 */
import mixinsBasePage from "@/mixins/basePage.ts";
import mixinsMainPage from "@/mixins/mainPage.ts";
/* 应用状态和页面描述信息 */
import { state } from "./dept/state";
import { DSLFormItemsQuery, DSLTableHeader, MODULE_BASE_URL } from "./dept/dsl";
import { BTN_TYPE, BTN_GROUP_TYPE } from "@/components/Types";
import map from "lodash/map";
import pick from "lodash/pick";
import { SuccessOrFailed } from "@/utils/index.ts";
import AddOrUpdate from "./dept/AddOrUpdate";

/* 组件 */
// import Ps02Sub from "./Ps02Sub";

/*模块名*/
const moduleName = "sys-dept";
/* 查询框store名 */
const queryFormKeyName = "formQueryData";

export default {
  components: {
    AddOrUpdate,
  },
  mixins: [mixinsBasePage, mixinsMainPage],
  data() {
    var _this = this;
    return {
      dom: "",
      max: 20,
      scrollData: [],
      isShowTable: true,
      isShowDetail: false,
      rowInfo: {},
      breadcrumbStack: [
        {
          name: "全部数据",
          data: "",
        },
      ],
      moduleName,
      queryFormKeyName,
      /*列表外操作*/
      configsBtns: {
        btnType: BTN_GROUP_TYPE.Btn,
        btnList: [
          {
            type: BTN_TYPE.add,
            shiro: "sys:dept:save",
            click: () => _this.update({ row: {} }),
          },
        ],
      },
      /*列表内操作按钮*/
      configsListBtns: {
        btnType: BTN_GROUP_TYPE.Btnmini,
        btnList: [
          {
            type: BTN_TYPE.update,
            shiro: "sys:dept:update",
            click: (scope) => _this.update(scope.row),
          },
          // {
          //   type: BTN_TYPE.delete,
          //   shiro: "sys:dept:delete",
          //   click: (scope) => _this.deleteBy(scope.row.id),
          // },
        ],
      },
      configsMainPage: { moduleName, state, shiro: "sys:dept:update" },
      configsCFormQuery: {
        moduleName,
        useProvide: true,
        keyForm: queryFormKeyName, //在state里面对应的表单索引字段
        DSLFormItems: DSLFormItemsQuery, //查询表单的查询项描述
      },
      DSLTableHeader,
      moduleOptions: {
        getDataListURL: MODULE_BASE_URL,
        getDataListIsPage: true,
        exportURL: MODULE_BASE_URL + "/export",
        deleteURL: "/sys/dept",
      },
    };
  },
  mounted() {},
  methods: {
    update(row) {
      this.rowInfo = row;
      this.isShowDetail = true;
    },
    /* 点击面包屑 */
    showData(breadcrumb, index) {
      this.dom = "";
      this.max = 20;
      let { data: dataList, scrollTop } = breadcrumb;
      this.dataList = dataList;
      this.$nextTick(() => {
        this.$refs.lazyTable.scrollTop = scrollTop;
      });
      this.$refs.lazyTable.doLayout();
      this.breadcrumbStack.splice(index + 1);
    },
    /* 点击列表下一级 */
    showSubData(row) {
      this.$refs.lazyTable.doLayout();
      this.dom = "";
      this.max = 20;
      this.scrollData = [];
      if (this.getPureData(row.subData).length >= 20) {
        this.scrollData = row.subData;
        this.loadMore();
      } else {
        this.dataList = this.getPureData(row.subData);
      }
      this.breadcrumbStack[
        this.breadcrumbStack.length - 1
      ].scrollTop = this.$refs.lazyTable.scrollTop;
      this.breadcrumbStack.push({
        name: row.name,
        data: this.dataList,
      });
      this.$nextTick(() => {
        this.$refs.lazyTable.scrollTop = 0;
      });
    },
    getPureData(dataArray) {
      return map(dataArray, (data) => {
        let res = pick(data, ["id", "pid", "name", "parentName", "sort"]);
        if (data.children.length > 0) {
          res.subData = data.children;
        }
        return res;
      });
    },
    /* query */
    async overrideQuery() {
      try {
        this.dataListLoading = true;
        let { data: res } = await this.$http.get(
          this.moduleOptions.getDataListURL
        );
        return SuccessOrFailed(
          res,
          {
            fn: () => {
              this.dataList = this.getPureData(res.data);
              this.breadcrumbStack[0].data = this.dataList;
            },
          },
          {
            msg: res.msg,
            fn: () => {
              this.dataList = [];
            },
          }
        );
      } catch (error) {
        console.error(error);
      } finally {
        this.dataListLoading = false;
      }
    }, // 获取数据列表
    loadMore() {
      this.dataList = "";
      let newData = this.scrollData;
      let dataLn = newData.length + 20;
      this.dataList = this.getPureData(newData).slice(0, this.max);
      // 获取需要绑定的table
      this.dom = this.$refs.lazyTable.bodyWrapper;
      this.dom.addEventListener("scroll", () => {
        // 滚动距离
        let scrollTop = this.dom.scrollTop;
        // 变量windowHeight是可视区的高度
        let windowHeight = this.dom.clientHeight || this.dom.clientHeight;
        // 变量scrollHeight是滚动条的总高度
        let scrollHeight = this.dom.scrollHeight || this.dom.scrollHeight;
        if (Math.ceil(scrollTop + windowHeight) === Math.ceil(scrollHeight)) {
          // 获取到的不是全部数据 当滚动到底部 继续获取新的数据
          if (this.max <= dataLn) {
            this.max += 20;
            this.dataList = this.getPureData(this.scrollData).slice(
              0,
              this.max
            );
          }
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .sub-title-wrapper {
  cursor: pointer;
}
::v-deep .el-card {
  height: 100%;
}
::v-deep .el-card__body {
  height: 100%;
}
#id-dept-page {
  .data-breadcrumb:hover,
  .current-breadcrumb {
    > span {
      font-weight: 600;
    }
  }
  .data-breadcrumb {
    &:hover {
      cursor: pointer;
    }
  }

  .aui-card--fill {
    > .el-card__body {
      height: 100%;
    }

    .mod-enterprise__ps02 {
      height: 100%;
      display: flex;
      flex-flow: column nowrap;
      // .table-wrapper {
      //   flex: 1;
      //   overflow: auto;
      //   height: 100%;
      .sub-title-wrapper {
        display: flex;
        flex-flow: row nowrap;
        justify-content: flex-start;
        align-items: center;
        &.link {
          font-weight: 600;
          span + span {
            margin-left: 16px;
          }
          &:hover {
            cursor: pointer;
          }
        }
        .stamp {
          flex: 1;
        }
      }
      // }
    }
  }
}
</style>

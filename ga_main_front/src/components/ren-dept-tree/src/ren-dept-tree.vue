<template>
  <div class="ren-dept-tree">
    <el-input
      :value="deptNameLabel"
       autocomplete="off"
      :placeholder="placeholder"
      @focus="showDeptDialog"
    >
      <el-button
        slot="append"
        icon="el-icon-search"
        @click="showDeptDialog"
      />
    </el-input>
    <el-input
      :value="value"
      style="display: none"
    />
    <el-dialog
      :visible.sync="visibleDept"
      :modal="false"
      :title="placeholder"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="30%"
    >
      <el-form
        class="dept-tree-wrapper"
        :inline="true"
        size="mini"
      >
        <el-form-item :label="$t('keyword')">
          <el-input v-model="filterText" />
        </el-form-item>
        <el-form-item>
          <el-button type="default">
            {{ $t("query") }}
          </el-button>
        </el-form-item>
      </el-form>
      <el-tree
        ref="tree"
        :data="deptList"
        :default-expanded-keys="expandedKeys"
        :props="{ label: 'name', children: 'children' }"
        :expand-on-click-node="false"
        :filter-node-method="filterNode"
        :highlight-current="true"
        class="filter-tree"
        node-key="id"
      />
      <template slot="footer">
        <el-button
          type="default"
          size="mini"
          @click="cancelHandle()"
        >
          {{ $t("cancel") }}
        </el-button>
        <el-button
          v-if="query"
          type="info"
          size="mini"
          @click="clearHandle()"
        >
          {{ $t("clear") }}
        </el-button>
        <el-button
          type="primary"
          size="mini"
          @click="commitHandle()"
        >
          {{ $t("confirm") }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script lang="ts">
import { findNodeFromTreeById } from "@/utils/index.ts";
let deptList = [];

export default {
  name: "RenDeptTree",
  props: {
    value: {
      type: [Number, String],
      required: true,
    },
    deptName: {
      type: String,
      required: true,
    },
    query: {
      type: Boolean,
      required: false,
    },
    placeholder: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      filterText: "",
      visibleDept: false,
      deptList,
      deptNameLabel: "",
      expandedKeys: null,
      defaultProps: {
        children: "children",
        label: "label",
      },
    };
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
    deptName(val) {
      this.deptNameLabel = val;
    },
  },
  created() {
    /* 组件创建，value有值，需要为value转码为汉字名 */
    console.log("created dept tree", this.value);
    const stopWatchValue = this.$watch("value", (val, oldVal) => {
      if (val && !oldVal) {
        this.getCurrentNodeById(val);
      }
      stopWatchValue();
    });
  },
  methods: {
    showDeptDialog() {
      this.expandedKeys = null;
      if (this.$refs.tree) {
        this.$refs.tree.setCurrentKey(null);
      }
      this.visibleDept = true;
      this.getCurrentNodeById(this.value);
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    async getCurrentNodeById(id) {
      if (deptList.length === 0) {
        try {
          let { data: res } = await this.$http.get("/sys/dept/list");
          if (res.code !== 0) {
            return this.$message.error(res.msg);
          }
          deptList = res.data;
        } catch (error) {
          console.error(error);
        }
      }
      this.deptList = deptList;
      if (id) {
        type leaf = {
          id: string;
          name: string;
        };
        const resNode = findNodeFromTreeById<leaf>(this.deptList, id);
        if (resNode) {
          this.deptNameLabel = resNode.name;
        }
      }
    },
    cancelHandle() {
      this.visibleDept = false;
      this.deptList = [];
      this.filterText = "";
    },
    clearHandle() {
      this.$emit("input", "");
      this.$emit("update:deptName", "");
      this.deptNameLabel = "";
      this.visibleDept = false;
      this.deptList = [];
      this.filterText = "";
    },
    commitHandle() {
      const node = this.$refs.tree.getCurrentNode();
      if (!node) {
        this.$message.error(this.$t("dept.chooseerror"));
        return;
      }
      this.$emit("input", node.id);
      this.$emit("update:deptName", node.name);
      this.deptNameLabel = node.name;
      this.visibleDept = false;
      this.deptList = [];
      this.filterText = "";
    },
  },
};
</script>

<style lang="scss" >
.ren-dept-tree {
  .filter-tree {
    max-height: 500px;
    overflow: auto;
  }
}
.dept-tree-wrapper {
  display: flex;
  flex-flow: row nowrap;
  justify-content: center;
  align-items: center;

  .el-form-item.el-form-item--mini {
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;
    align-items: center;
    label.el-form-item__label {
      width: 110px;
    }
  }
}
</style>

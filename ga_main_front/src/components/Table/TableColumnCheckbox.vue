<template>
  <el-table-column
    align="center"
    width="50"
  >
    <template
      slot="header"
      slot-scope="scope"
    >
      <!-- TODO: scope不可省略，否则不会触发响应系统，原理待研究 -->
      <el-checkbox
        :value="checkAll"
        :indeterminate="isIndeterminate"
        :disabled="disabledForHeadCheckbox"
        @change="handleChangeCheckAll"
      />
    </template>
    <template slot-scope="scope">
      <el-checkbox
        :data-index="scope.$index"
        :disabled="disabled"
        :value="Boolean(selectedIndexCollection[scope.$index])"
        @change="handleCheckRowChange($event, scope.$index)"
      />
    </template>
  </el-table-column>
</template>
<script>
import merge from "lodash/merge";
import reduce from "lodash/reduce";

export default {
  name: "TableColumnCheckbox",
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    value: {
      required: true,
      type: Array,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      test: "",
      selectedIndexCollection: {},
    };
  },
  computed: {
    disabledForHeadCheckbox() {
      return this.$parent.data.length === 0 || this.disabled;
    },
    checkAll: {
      get() {
        if (this.disabledForHeadCheckbox) {
          return false;
        } else {
          for (let index = 0; index < this.$parent.data.length; index++) {
            if (!this.selectedIndexCollection[index]) {
              return false;
            }
          }
          return true;
        }
      },
      set(isCheckAll) {
        const _selectedIndexCollection = {};
        if (isCheckAll) {
          for (let index = 0; index < this.$parent.data.length; index++) {
            _selectedIndexCollection[index] = true;
          }
        }
        this.selectedIndexCollection = _selectedIndexCollection;
      },
    },
    isIndeterminate() {
      if (this.checkAll) {
        return false;
      }
      if (this.selectedIndexCollection.length > 0) {
        return true;
      }
      return false;
    },
  },
  watch: {
    selectedIndexCollection(selectedIndexCollection) {
      const selectedIndexArray = reduce(
        selectedIndexCollection,
        (_selectedIndexArray, isChecked, rowIndex) => {
          _selectedIndexArray.push(rowIndex);
          return _selectedIndexArray;
        },
        []
      );
      this.$emit("change", selectedIndexArray);
    },
  },
  methods: {
    handleChangeCheckAll(checkAll) {
      this.checkAll = checkAll;
    },
    handleCheckRowChange(isCheck, index) {
      const _selectedIndexCollection = merge({}, this.selectedIndexCollection);
      if (isCheck) {
        _selectedIndexCollection[index] = true;
      } else {
        delete _selectedIndexCollection[index];
      }
      this.selectedIndexCollection = _selectedIndexCollection;
    },
  },
};
</script>
<style lang="scss">
</style>
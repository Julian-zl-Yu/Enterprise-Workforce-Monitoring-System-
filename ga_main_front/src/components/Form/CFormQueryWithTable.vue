<template>
  <card>
    <!-- {{ stateModuleName }}{{ keyForm }} -->
    <el-form
      ref="refCFormQuery"
      :class="['customize-form', 'label-position-right', 'c-form-query']"
      :model="formModel"
      status-icon
      @keyup.enter.native="$emit('enter')"
    >
      <!-- flex 流式布局 -->
      <el-row
        :gutter="24"
        class="customize-row"
      >
        <el-col
          v-for="prop in DSLFormLayout"
          :key="prop"
          :xs="24"
          :md="checkItem(prop).span || 8"
        >
          <el-form-item :prop="prop">
            <label
              :for="checkItem(prop).prop"
              :title="checkItem(prop).label"
              class="el-form-item__label"
            >
              {{ checkItem(prop).label }}
            </label>
            <FormItem
              v-model="formModel[prop]"
              :configs="checkItem(prop)"
            />
          </el-form-item>
        </el-col>
        <el-col
          :xs="24"
          :md="totalItems.length > 3 && !isFold ? 24 : configs.btnspan || 8"
          :class="['c-form-query__btn', configs.btnAlign || '']"
        >
          <el-button
            type="primary"
            @click="$emit('query')"
          >
            {{ $t("query") }}
          </el-button>
          <el-button @click="formReset()">
            {{ $t("reset") }}
          </el-button>
          <!--查询项大于3的时候才显示折叠按钮-->

          <el-button
            v-if="totalItems.length > 3"
            type="text"
            @click="toggleFold"
          >
            {{ isFold ? "展开" : "收起" }}
            <i :class="isFold ? 'el-icon-arrow-down' : 'el-icon-arrow-up'" />
          </el-button>
        </el-col>
      </el-row>
    </el-form>
  </card>
</template>
<script>
import isPlainObject from "lodash/isPlainObject";
import _map from "lodash/map";
import FormItem from "./FormItem/FormItem";
/* 查询方法需要外部传入 */
export default {
  name: "CFormQuery",
  components: {
    FormItem,
  },
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    value: {
      type: [Object, Boolean],
      default() {
        return false;
      },
    },
    configs: {
      required: true,
      type: Object,
    },
  },
  data() {
    return {
      isFold: true,
      formRules: {},
    };
  },
  computed: {
    labelPosition() {
      
      return "right";
    },
    formModel: {
      get() {
        if (this.stateModuleName) {
          return this.$store.state[this.stateModuleName][this.keyForm];
        } else if (isPlainObject(this.value)) {
          return this.value;
        } else {
          console.error("未提供必需的CFormQuery v-model 或者 store 模块");
        }
      },
      set(formModel) {
        if (this.stateModuleName) {
          this.$store.state[this.stateModuleName][this.keyForm] = formModel;
        } else if (isPlainObject(this.value)) {
          this.$emit("change", formModel);
        } else {
          console.error("未提供必需的CFormQuery v-model 或者 store 模块");
        }
      },
    },
    /* state 对应当前表单model对象：默认是formData */
    keyForm() {
      return this.configs?.keyForm || "formData";
    },
    /* @namespace state 模块名字*/
    stateModuleName() {
      return this.configs?.moduleName || "";
    },
    DSLFormLayout() {
      let layout = this.configs?.DSLFormLayout || [];
      if (layout.length < 1) {
        layout = _map(this.configs?.DSLFormItems || [], (i) => i.prop);
      }
      return this.isFold ? layout.slice(0, 3) : layout;
    },
    DSLFormItems() {
      return this.configs?.DSLFormItems || [];
    },
    totalItems() {
      return _map(this.configs?.DSLFormItems, (i) => i) || [];
    },
  },
  destroyed() {},
  created() {},
  mounted() {},
  methods: {
    checkItem(prop) {
      let item = this.DSLFormItems[prop];
      if (!item) {
        console.error(prop);
        return {};
      } else {
        return item;
      }
    },
    toggleFold() {
      this.isFold = !this.isFold;
    },
    formReset() {
      this.$refs?.refCFormQuery?.resetFields();
      this.$emit("query");
    },
    getSpan(item) {
      return item?.span || 8;
    },
  },
};
</script>

<style lang="scss" scoped>
.c-form-query__btn {
  overflow: hidden;
  display: flex;
  flex-flow: row nowrap;
  justify-content: flex-end;
  align-items: center;
  &.right {
    justify-content: flex-end;
  }
}
</style>
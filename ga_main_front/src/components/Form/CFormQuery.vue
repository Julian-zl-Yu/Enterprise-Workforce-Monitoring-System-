<template>
  <Card>
    <!-- {{ stateModuleName }}{{ keyForm }} -->
    <el-form
      ref="refCFormQuery"
      :class="['customize-form label-position', labelPosition, 'c-form-query']"
      :model="formModel"
      status-icon
      @keyup.enter.native="$emit('enter')"
    >
      <!-- grid 流式布局 -->
      <div class="customize-row grid-container">
        <div
          v-for="prop in DSLFormLayout"
          :key="prop"
          class="c-form-query__item"
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
              :ref="'formItem_' + prop"
              v-model="formModel[prop]"
              :configs="checkItem(prop)"
            />
          </el-form-item>
        </div>
        <div
          :class="[
            'c-form-query__btn',
            configs.btnAlign || '',
            'span_' + (3 - (DSLFormLayout.length % 3)),
          ]"
        >
          <template v-if="noQuery"> </template>
          <template v-else>
            <el-button type="primary" @click="$emit('query')">
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
              <i
                :class="isFold ? 'el-icon-arrow-down' : 'el-icon-arrow-up'"
              /> </el-button
          ></template>
        </div>
      </div>
    </el-form>
  </Card>
</template>
<script>
import isPlainObject from "lodash/isPlainObject";
import _map from "lodash/map";
import cloneDeep from "lodash/cloneDeep";
import FormItem from "./FormItem/FormItem";
import { getLavelValue } from "./CFormMixin";
/* 查询方法需要外部传入 */
export default {
  mixins: [getLavelValue],
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
      privateFormData: {},
      isFold: false,
      formRules: {},
    };
  },
  computed: {
    noQuery() {
      return this.configs?.noQuery || false;
    },
    labelPosition() {
      return this?.configs?.labelPosition || "right";
    },
    formModel: {
      get() {
        if (isPlainObject(this.value)) {
          return this.value;
        } else if (this.stateModuleName) {
          return this.$store.state[this.stateModuleName][this.keyForm];
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
  created() {
    this.privateFormData = cloneDeep(this.formModel);
  },
  methods: {
    handleResize(e) {
      console.log("handleResize -> e", e);
    },
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
      console.log(
        `!this.configs?.resetDefaultField`,
        !this.configs?.resetDefaultField
      );
      if (this.configs?.resetDefaultField) {
        console.log(
          `this.configs?.resetDefaultField`,
          this.configs?.resetDefaultField
        );
        this.configs?.resetDefaultField.forEach((item) => {
          console.log(`item`, item);
          this.privateFormData[item] = this.formModel[item];
        });
        console.log(`this.privateFormData`, this.privateFormData);
      } else {
        this.privateFormData = {};
      }

      this.$emit("change", cloneDeep(this.privateFormData));
      this.$nextTick(() => this.$emit("query"));
    },
    getSpan(item) {
      return item?.span || 8;
    },
  },
};
</script>

<style lang="scss" scoped>
.grid-container {
  width: 100%;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  column-gap: 32px;
  row-gap: 8px;
  grid-auto-rows: 38px;
}

.c-form-query__item {
  grid-column-start: span 1;
}
.c-form-query__btn {
  overflow: hidden;
  display: flex;
  flex-flow: row nowrap;
  justify-content: flex-end;
  align-items: center;
  &.right {
    justify-content: flex-end;
  }
  &.span_ {
    @for $index from 1 through 3 {
      &#{$index} {
        grid-column-start: span #{$index};
      }
    }
  }
}

@media (max-width: 780px) {
  .c-form-query__item {
    grid-column-start: span 3;
  }
  .c-form-query__btn {
    &.span_ {
      @for $index from 1 through 3 {
        &#{$index} {
          grid-column-start: span 3;
        }
      }
    }
  }
}
</style>

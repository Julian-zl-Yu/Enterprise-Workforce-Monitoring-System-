<template>
  <el-table-column class="table-column" v-bind="bindProps">
    <!-- 项目审核 通过不通过 -->
    <div
      slot-scope="scope"
      :class="['cell-inner-wrapper', { link: configs.isLink }]"
      @click="clickCell(scope)"
    >
      <!-- {{ bindProps }} -->
      <!-- https://cn.vuejs.org/v2/guide/components-slots.html#%E4%BD%9C%E7%94%A8%E5%9F%9F%E6%8F%92%E6%A7%BD -->
      <slot v-if="isUseSlot" :scope="scope" />
      <template v-else-if="configs.type === FORM_ITEM_TYPE.PassOrNot">
        <PassOrNot :value="scope.row[configs.prop]" />
      </template>
      <template v-else-if="configs.component">
        <div
          :is="configs.component"
          v-model="scope.row[configs.prop]"
          v-bind="{
            isTable: true,
            isEdit: scope.$index === currentEditRowIndex,
            configs: configs,
            rowIndex: scope.$index,
            scope: scope,
          }"
        />
      </template>
      <!--  图片  -->
      <template v-else-if="configs.isimg">
        <div>
          {{ $log(scope.row[configs.prop]) }}
          <el-image
            style="width: 50px; height: 50px;"
            :src="url + scope.row[configs.prop] + token"
            :preview-src-list="[url + scope.row[configs.prop] + token]"
          >
            <div slot="error" class="image-slot"></div>
          </el-image>
        </div>
      </template>
      <!-- 默认直接显示 -->
      <template v-else>
        <!-- isEdit 在TableColumn中默认是false CForm中默认是true（不设置） -->
        <FormItem
          v-model="scope.row[configs.prop]"
          :is-table="true"
          :is-edit="scope.$index === currentEditRowIndex"
          :configs="configs"
          :row-index="scope.$index"
          :scope="scope"
        />
        <!-- {{ configs }}-{{ scope.row[configs.prop] }} -->
      </template>
    </div>
  </el-table-column>
</template>

<script>
import { FORM_ITEM_TYPE } from "../Types";
import FormItem from "@/components/Form/FormItem/FormItem";
import PassOrNot from "./TableColumnItems/PassOrNot";
import some from "lodash/some";
import merge from "lodash/merge";
import omit from "lodash/omit";
import isFunction from "lodash/isFunction";
import { SITE_CONFIG } from "@/main.config";

export default {
  name: "TableColumn",
  components: {
    FormItem,
    PassOrNot,
  },
  filters: {
    cons(val) {
      return val;
    },
  },
  props: {
    elconfigs: {
      type: Object,
      default() {
        return {};
      },
    },
    currentEditRowIndex: {
      type: Number,
      required: false,
      default() {
        return -1;
      },
    },
    configs: {
      required: false,
      type: Object,
      default() {
        return {};
      },
    },
  },
  data() {
    return {
      FORM_ITEM_TYPE,
      isUseSlot: false,
      token: sessionStorage.getItem("token"),
      url: SITE_CONFIG["apiURL"],
    };
  },
  computed: {
    bindProps() {
      /* this.configs 可以动态变更，优先级高于this.$attrs */
      return merge(
        {
          headerAlign: "center",
          align: "center",
          width: this.configs.width || "auto",
          fixed:this.configs.fixed || false
        },
        this.$attrs,
        /* configs formitem type 与 el column typ 冲突 需要剔除 */
        omit(this.configs, ["type"])
      );
    },
  },
  mounted() {
    this.isUseSlot =
      some(this.$scopedSlots, () => true) || some(this.$slots, () => true);
  },
  methods: {
    clickCell(scope) {
      if (isFunction(this.configs.click)) {
        this.configs.click(scope.row[this.configs.prop], scope);
      }
    },
  },
};
</script>
<style lang="scss">
div.cell {
  > .form-item {
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;
    align-items: center;
  }
}
</style>

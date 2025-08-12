<template>
  <fragment>
    <template v-if="canEdit">
      <RegionSelect
        ref="formItem"
        filterable
        clearable
        :region-pid="attrRegionPid"
        :value="value"
        :disabled="attrDisabled"
        :configs="configs"
        :props="{ expandTrigger: 'hover'}"
        @change="handleValueChange"
      />
    </template>
    <FormItemReadonly
      v-else
      :is-table="isTable"
      :data-trigger="setReadonlyText(value)"
      :value="textWhenReadOnly"
    />
  </fragment>
</template>

<script>
    import {
        model as baseModel,
        subCreated,
        subComputed,
        subProps,
        subComponents,
        subData,
    } from "@/components/Form/FormItem/mixinsFormItemBase";
    import {getAreaCodeTree} from "@/utils/index.ts";
    import {getAreaCodeNodeFromDB} from "@/utils/dataBase.ts";
    import isArray from "lodash/isArray";
    import last from "lodash/last";
    import RegionSelect from "./RegionSelect";
    import {REGION_TYPE} from "@/components/Types";

    export default {
        name: "ImplCascader",
        components: {
            RegionSelect
        },
        mixins: [
            subCreated,
            subData,
            baseModel,
            subProps,
            subComputed,
            subComponents,
        ],
        data() {
            return {
            };
        },
        computed: {
            attrRegionPid() {
                /*通过参数
                * regionType 确定根节点范围
                * 默认是全国
                * 转换成pid
                * 根据pid 查找范围
                * */

                /*默认是全国*/

                return "100000";
            },
        },
        methods: {
            async init() {

            },
            handleValueChange(value) {
                if (isArray(value)) {
                    value = last(value);
                }
                this.$emit("change", value);
            },
            async getBindIdIsAreaCode() {
                try {

                    let cascaderTree = await getAreaCodeTree();
                    return cascaderTree[0]?.children;
                } catch (error) {
                    return [];
                }
            },
            async setReadonlyText(value) {
                const result = await getAreaCodeNodeFromDB(value);
                if (isArray(result)) {
                    this.textWhenReadOnly = result.map((i) => i.name).join("/");
                }
                return false;
            },
        },
    };
</script>

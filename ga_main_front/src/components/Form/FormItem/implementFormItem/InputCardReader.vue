<template>
  <fragment>
    <!--isEdit {{ isEdit }} -->
    <!--canEdit {{canEdit}}-->
    <template v-if="canEdit">
      <el-input
        ref="formItem"
        class="idcardreader"
        clearable
        v-bind="$attrs"
        :type="inputType"
        :value="value"
        :placeholder="attrPlaceholder"
        :show-password="attrShowPassword"
        :disabled="attrDisabled"
        @input="handleValueChange"
      >
        <div
          slot="append"
          class="idcardreader-btn"
          title="读取身份证"
          @click="getInfoFromIDCard"
          v-if="!isShow"
        >
          <svg
            class="icon"
            style="
              width: 100%;
              height: 100%;
              vertical-align: middle;
              fill: currentColor;
              overflow: hidden;
            "
            viewBox="0 0 1417 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="1531"
          >
            <path
              d="M1053.926 141.211h-798.626c-62.983 0.064-114.023 51.105-114.087 114.082v513.408c0.064 62.983 51.105 114.023 114.083 114.087h798.632c62.983-0.064 114.024-51.105 114.087-114.082v-513.408c-0.064-62.983-51.105-114.023-114.082-114.087zM1110.965 768.702c-0.041 31.491-25.558 57.008-57.045 57.050h-798.62c-31.487-0.047-56.998-25.562-57.038-57.045v-513.405c0.040-31.491 25.558-57.008 57.045-57.049h798.62c31.487 0.047 56.998 25.562 57.039 57.045z"
              p-id="1532"
            />
            <path
              d="M946.142 534.59c30.804-26.183 50.735-64.688 50.735-108.152 0-78.633-63.979-142.613-142.613-142.613s-142.601 63.98-142.601 142.613c0 43.464 19.961 81.969 50.766 108.152-63.939 33.32-107.813 100.123-107.813 177.071 0 15.753 12.772 28.524 28.524 28.524 15.753 0 28.524-12.772 28.524-28.524 0-78.633 63.98-142.612 142.612-142.612s142.613 63.979 142.613 142.612c0 0 0 0 0 0 0 15.753 12.772 28.524 28.524 28.524 15.753 0 28.524-12.772 28.524-28.524 0 0 0 0 0 0-0.009-76.948-43.854-143.752-107.795-177.071zM768.702 426.437c0-47.257 38.308-85.563 85.564-85.563s85.563 38.308 85.563 85.563c0 47.257-38.308 85.563-85.563 85.563-47.234-0.052-85.51-38.33-85.563-85.559zM626.087 340.863h-342.264c-15.753 0-28.524 12.772-28.524 28.524s12.772 28.524 28.524 28.524h342.264c15.753 0 28.524-12.772 28.524-28.524s-12.772-28.524-28.524-28.524zM597.563 483.476h-313.74c-15.753 0-28.524 12.772-28.524 28.524s12.772 28.524 28.524 28.524h313.74c15.753 0 28.524-12.772 28.524-28.524s-12.772-28.524-28.524-28.524zM512 626.087h-228.176c-15.753 0-28.524 12.772-28.524 28.524s12.772 28.524 28.524 28.524h228.176c15.753 0 28.524-12.772 28.524-28.524s-12.772-28.524-28.524-28.524z"
              p-id="1533"
            />
          </svg>
        </div>
      </el-input>
    </template>
    <FormItemReadonly v-else :is-table="isTable" :value="value" />
  </fragment>
</template>

<script>
import { FORM_ITEM_TYPE } from "@/components/Types";
import { handleValueChange } from "@/components/Form/FormItem/commonFormItem";
import {
  model as baseModel,
  subCreated,
  subComputed,
  subProps,
  subComponents,
} from "@/components/Form/FormItem/mixinsFormItemBase";
import { readCard } from "@/utils/idCardReader";
// import { readCard } from "@/utils/idCardReaderHuaShi";
import { isString } from "lodash";

export default {
  name: "ImplInput",
  mixins: [subCreated, baseModel, subProps, subComputed, subComponents],
  computed: {
    inputType() {
      if (this.componentType === FORM_ITEM_TYPE.Textarea) {
        return "textarea";
      } else if (this.attrShowPassword) {
        return "password";
      } else {
        return "text";
      }
    },
    attrShowPassword() {
      return this?.configs?.showPassword || false;
    },
    isShow(){
      return !this?.configs?.isShowBtn || false
    }
  },
  methods: {
    async getInfoFromIDCard() {
      let res = await readCard();
      if (!this?.configs?.done) {
        alert("未覆盖done方法");
        console.error("未覆盖done方法");
        console.error(this.configs);
      } else {
        this?.configs?.done(res);
      }
    },
    init() {},
    handleValueChange,
  },
};
</script>

<style lang="scss" >
.idcardreader {
  > .el-input-group__append {
    padding: 0;
    .idcardreader-btn {
      width: 38px;
      margin: auto 20px;
      &:hover {
        cursor: pointer;
      }
    }
  }
}
</style>
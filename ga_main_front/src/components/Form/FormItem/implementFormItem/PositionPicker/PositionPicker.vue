<template>
  <fragment style="z-index: 99999">
    <template v-if="canEdit">
      <!-- {{ positionInfo }} -->
      <el-popover placement="top-start" width="200" trigger="manual">
        <div>经度：{{ positionInfo.longitude }}</div>
        <div>纬度: {{ positionInfo.latitude }}</div>
        <el-input
          ref="input"
          slot="reference"
          :disabled="disabled"
          :value="positionPickerAddress"
          v-bind="$attrs"
          class="position-picker"
          clearable
          @click.native="handleInputClick($event)"
        />
      </el-popover>
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
// import MapView from "./Test";
import MapView from "./MapView";
import { Position } from "@/components/Form/FormItem/implementFormItem/PositionPicker/Position";
import { $id } from "@/utils/iQuery";
import {
  model as baseModel,
  subCreated,
  subComputed,
  subProps,
  subComponents,
  subData,
} from "@/components/Form/FormItem/mixinsFormItemBase";
import merge from "lodash/merge";

const mixinProps = merge({}, subProps.props, {
  value: {
    type: [Object, String],
    default() {
      return Position.empty();
    },
  },
  range: {
    type: [String, Boolean],
    default: false,
  },
  disabled: Boolean,
  readonly: Boolean,
});

export default {
  name: "PositionPicker",
  mixins: [
    subCreated,
    subData,
    baseModel,
    { props: mixinProps },
    subComputed,
    subComponents,
  ],
  provide() {
    const _this = this;
    return {
      PositionPicker: _this,
    };
  },
  data() {
    return {
      positionInfo: {
        longitude: 0,
        latitude: 0,
        address: "",
        label: "",
      },
    };
  },
  computed: {
    positionRange() {
      let range = false;
      const prop = this?.configs?.range?.prop;
      if (prop) {
        range = (this.formData && this.formData[prop]) || false;
      }
      return range;
    },
    positionPickerAddress() {
      let address = "";
      if (this?.configs?.log_lat) {
        address = this.positionInfo.longitude
          ? `${
              this.positionInfo.longitude != 0
                ? this.positionInfo.longitude
                : ""
            }${
              this.positionInfo.longitude != 0 &&
              this.positionInfo.latitude != 0
                ? ","
                : ""
            }${
              this.positionInfo.latitude != 0 ? this.positionInfo.latitude : ""
            }`
          : this.positionInfo;
      } else {
        address = this.positionInfo.address;
      }
      return address;
    },
  },
  watch: {
    value(value) {
      this.positionInfo = value;
    },
    positionInfo(positionInfo) {
      this.$emit("change", positionInfo);
    },
  },
  mounted() {
    if (this.value.address) {
      this.positionInfo = this.value;
    }
  },
  methods: {
    init() {},
    setReadonlyText(value) {
      this.textWhenReadOnly = value.address ? `${value.address}` : value;
      return false;
    },
    closeMapView() {
      this.$layer.close(this.closeMapView.id);
    },
    clear() {
      this.$refs.input.clear();
      this.positionInfo = Position.empty();
      this.$emit("change", this.positionInfo);
    },
    handleInputClick($event) {
      if (this.disabled) {
        return false;
      }
      /*“I”代表点击的是清除按钮*/
      const isClickClearIcon = $event?.target?.tagName === "I";
      if (isClickClearIcon) {
        this.clear();
        return false;
      }
      const rootElement = $id("root-element");
      if (rootElement && rootElement.style) {
        /*open dialog*/
        rootElement.style.zIndex = "-1";
      } else {
        /* FIXED: 修复modal 的index 覆盖问题 */
        console.log(" 当前页面没有id为root-element的根元素");
      }
      this.closeMapView.id = this.$layer.iframe({
        content: {
          content: MapView, //传递的组件对象
          parent: this, //当前的vue对象
          data: {
            value: "",
            vm: this,
          }, //props
        },
        area: ["800px", "600px"],
        title: "请选择地址",
      });
    },
  },
};
</script>
<style lang="scss">
.vl-notify,
.vl-notify-main,
.vl-notify-alert,
.vl-notify-iframe {
  z-index: 2002 !important;
}
.amap-overviewcontrol {
  z-index: 2;
}
</style>
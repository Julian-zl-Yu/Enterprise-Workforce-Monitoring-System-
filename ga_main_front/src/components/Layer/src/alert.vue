<!--
@Author: 左盐
@Date:   2018-03-19 16:16:24
@Email:  huabinglan@163.com
@Project: xxx
@Last modified by:   左盐
@Last modified time: 2018-03-24 16:02:01
-->
<template>
  <base-drag
    :options="options"
    :cls="cls"
  >
    <div class="vl-notify-content">
      <i
        v-if="this.options.icon == 0"
        class="vlayer vlicon-warning"
        style="color:#f4ea2a;font-size:32px"
      />
      <i
        v-if="this.options.icon == 1"
        class="vlayer vlicon-error"
        style="color:#f4ea2a;font-size:32px"
      />
      <i
        v-if="this.options.icon == 2"
        class="vlayer vlicon-success"
        style="color:#00ba9b;font-size:32px"
      />
      <i
        v-if="this.options.icon == 3"
        class="vlayer vlicon-wenti"
        style="color:#f4ea2a;font-size:32px"
      />
      <div
        class="vl-notify-content-div"
        v-html="options.content"
      />
    </div>
    <div class="vl-notify-btns">
      <pzbutton
        btn="primary"
        size="small"
        @click.native="btnyes"
      >
        {{ btns[0] }}
      </pzbutton>
      <pzbutton
        v-if="typeof(options.cancel) == 'function' || options.cancel=='cancel'"
        btn="default"
        size="small"
        style="margin-left:10px;"
        @click.native="btncancel"
      >
        {{ btns[1] }}
      </pzbutton>
    </div>
  </base-drag>
</template>

<script>
import pzbutton from "./components/button/button.vue";
import helper from "./helper/helper";
import drag from "./components/drag/drag";

export default {
  components: {
    pzbutton,
    "base-drag": drag
  },
  props: {
    options: {
      type: Object,
      default: function() {
        return {};
      }
    }
  },
  data() {
    return {
      btns: [],
      cls: {
        "vl-notify-alert": true
      }
    };
  },
  async mounted() {
    await helper.sleep(20);
    this.formatBtnText();
    helper.hiddenScrollBar(this.options);
  },
  methods: {
    btnyes(event) {
      helper.btnyes(event, this.options);
    },
    btncancel(event) {
      helper.btncancel(event, this.options);
    },
    formatBtnText() {
      const a = this.options.btn;
      if (typeof a === "string") {
        this.btns = [this.options.btn, "取消"];
        return;
      }
      if (a instanceof Array) {
        this.btns = [a[0] || "确定", a[1] || "取消"];
      }
    }
  }
};
</script>

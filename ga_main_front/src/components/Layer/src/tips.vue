<template>
  <div
    :id="options.id"
    aria-hidden="false"
    :class="[
      'vl-notify vl-notify-tips',
      options.tips[1],
      options.isDirection ? options.isDirection : '',
      { 'no-class': options.noClass },
      'vl-notify-tips-' + options.tips[0],
    ]"
  >
    <div v-html="options.content" />

    <svg
      v-if="options.triangle"
      :class="'triangle ' + options.triangle"
      viewBox="0 0 60 60"
    >
      <polyline points="0,0 60,0 30,60" />
    </svg>
  </div>
</template>

<script>
export default {
  props: {
    options: {
      type: Object,
      default: function () {
        return {};
      },
    },
  },
  data() {
    return {
      timeout: "",
    };
  },
  computed: {
    offset() {
      return this.getOffset();
    },
  },
  async mounted() {
    let self = this;
    if (this.options.time == 0) {
      this.options.time = 2;
    }

    setTimeout(function () {
      self.clickBtnOK();
    }, self.options.time * 1000);

    await this.getOffset();
  },
  methods: {
    clickBtnOK: function () {
      let o = document.getElementById(this.options.id);
      if (o) {
        if (typeof this.options.yes == "function") {
          this.options.yes();
        }
        this.options.layer.close(this.options.id);
        // delete this.$layer.instances[this.options.id];
        // o.remove();
      }
    },
    sleep: function (ms) {
      return new Promise((resolve) => setTimeout(resolve, ms));
    },
    async getOffset() {
      try {
        await this.sleep(1);
        let o = document.querySelector(this.options.title); //用title传递that元素
        const bound = o.getBoundingClientRect();
        let oTips = document.querySelector("#" + this.options.id);
        let boundTips = oTips?.getBoundingClientRect() || false;
        if (!boundTips) return;

        /*  let scrollTop = 0; //this.getScrollTop();
        let left = o.offsetLeft;
        let top = o.offsetTop - scrollTop;

        while (o.offsetParent) {
          o = o.offsetParent;
          left += o.offsetLeft;
          top += o.offsetTop;
        } */

        const arrow = 8 + 1;
        const left = bound.x;
        const top = bound.y;

        let offset = {};
        switch (this.options.tips[0]) {
          case 0:
            offset = {
              left: left + "px",
              top: top - boundTips.height - arrow + "px",
            };
            break;
          case 1:
            offset = {
              left: left + bound.width + arrow + "px",
              top: top + "px",
            };
            break;
          case 2:
            offset = {
              left: left + "px",
              top: top + bound.height + arrow + "px",
            };
            break;
          case 3:
            offset = {
              left: left - boundTips.width - arrow + "px",
              top: top + "px",
            };
            break;
        }
        oTips.style.left = offset.left;
        oTips.style.top = offset.top;
        // return offset;
      } catch (error) {
        console.error(error);
      }
    },
    getScrollTop() {
      var scrollTop = 0;
      if (document.documentElement && document.documentElement.scrollTop) {
        scrollTop = document.documentElement.scrollTop;
      } else if (document.body) {
        scrollTop = document.body.scrollTop;
      }
      return scrollTop;
    },
  },
};
</script>

<style lang="scss">
.vl-notify.vl-notify-tips {
  &.no-class {
    background-color: transparent;
    color: unset;
    padding: 0;
  }

  @for $index from 0 through 4 {
    &.vl-notify-tips-#{$index}:after {
      border: 0 solid transparent;
    }
  }
  .triangle {
    &.top {
      top:100%;
      position: absolute;
      width: 15px;
      height: 15px;
      fill: white;
      stroke:black;
    }
  }
}
</style>

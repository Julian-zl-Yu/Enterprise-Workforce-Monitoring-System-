<template>
  <div class="sub-page">
    <slot
      name="child"
      :wrapper="wrapper"
    />
    <header
      v-if="header"
      class="sub-page__header"
      shadow="never"
    >
      <div class="sub-page__header_wrapper">
        <span class="h-title1">{{ header }}</span>
        <!-- <el-button type="primary" @click="goMain" > 返回 </el-button> -->
      </div>
    </header>
    <div
      class="sub-page__body"
      shadow="never"
    >
      <!-- <div v-if="false"> loading... <h1>{{ privateSubPagecount }}</h1> </div> -->
      <el-scrollbar
        ref="scrollbar"
        style="height: 100%"
      >
        <slot>暂无内容</slot>
      </el-scrollbar>
    </div>

    <footer
      class="sub-page__footer"
      shadow="never"
    >
      <slot name="footer" />
    </footer>
  </div>
</template>
<script>
import basePage from "@/mixins/basePage.ts";
import $ from "jquery";

export default {
  name: "SubPage",
  mixins: [basePage],
  props: {
    configs: {
      type: Object,
      required: false,
      default: () => ({}),
    },
    header: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      wrapper: "",
      privateIsLoading: true,
      privateSubPagecount: 0,
      /* moduleName  */
    };
  },
  computed: {
    notMain() {
      return this.currentView === "main";
    },
    isReadOnly() {
      return this?.currentView === "detail";
    },
    title() {
      const title = {
        add: "新增",
        update: "修改",
        detail: "查看",
      };
      console.log("SubPage -> title", title[this.currentView]);
      return `${this?.$route?.meta?.title}--${title[this.currentView]}`;
    },
  },
  mounted() {
    this.wrapper = "el-scrollbar__view_" + this._uid;
    const scrollbar = $(this.$refs.scrollbar.$el);
    const wrapper = scrollbar.find(">.el-scrollbar__wrap");
    wrapper.attr("id", this.wrapper);
  },
  /*  activated() {
             this.privateIsLoading = true;
             this.privateSubPagecount = 0;
             this.delay();
         }, */
  methods: {
    delay() {
      if (this.privateSubPagecount >= 5) {
        this.privateIsLoading = false;
      } else {
        this.privateSubPagecount = this.privateSubPagecount + 1;
        console.log(
          "delay -> this.privateSubPagecount",
          this.privateSubPagecount
        );
        setTimeout(() => {
          this.delay();
        }, 1000);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.sub-page {
  display: flex;
  flex-flow: column nowrap;

  .sub-page__header_wrapper {
    display: flex;
    flex-flow: row nowrap;
    justify-content: space-between;
    align-items: center;
    margin: 0 -12px;
    padding: 24px;
    background: white;
  }

  .sub-page__body {
    flex: 1;
    height: calc(100% - 60px);
  }

  .sub-page__footer {
    display: flex;
    flex-flow: row nowrap;
    justify-content: flex-end;
    align-items: center;
    height: 60px;
    padding: 24px;
    position: relative;
    z-index: 1;
    @include elevation4;
  }
}
</style>
<style lang="scss">
.sub-page {
  .sub-page__body {
    .el-scrollbar__wrap {
      overflow-x: hidden;
    }
  }
}
</style>
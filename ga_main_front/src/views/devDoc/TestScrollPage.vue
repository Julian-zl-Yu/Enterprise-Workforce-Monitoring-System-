<template>
  <div>
    <el-button>scroll</el-button>
    <div
      id="parent"
      class="test-scroll-page"
    >
      <div
        v-for="(item, index) in 100"
        :id="'id'+index"
        :key="index"
        class="child"
        @click="scroll(index)"
      >
        {{ index }}
      </div>
    </div>
  </div>
</template>
<script>
import { $$ } from "@/utils/iQuery";
import { scrollify } from "@/utils/index.ts";
window.scrollify = scrollify;
export default {
  name: "Demo",
  data() {
    return {};
  },
  mounted() {},
  methods: {
    scroll(id) {
      scrollify(`#id${id}`, "#parent");
    },
  },
};
</script>
<style lang="scss">
.test-scroll-page {
  width: 100%;
  height: 500px;
  overflow: auto;
  position: relative;
  padding:0;
  .child {
    background-color: rgb(149, 145, 145);
    margin: 10px;
    border: 1px solid greenyellow;
    border-radius: 4px;
    &:hover {
      cursor: pointer;
    }
  }
}
</style>
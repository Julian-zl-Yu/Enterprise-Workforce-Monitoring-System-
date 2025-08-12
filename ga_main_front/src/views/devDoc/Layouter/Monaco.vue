<template>
  <div
    ref="container"
    style="height:100%;"
  />
</template>
<script>
import * as monaco from "monaco-editor/esm/vs/editor/editor.api.js";
import isString from "lodash/isString";
import isArray from "lodash/isArray";

export default {
  name: "Monaco",
  model: {
    prop: "value",
    event: "change",
  },
  props: ["value", "isFormat"],
  data() {
    return {
      monacoInstance: false,
    };
  },
  mounted() {
    this.init();
    this.$watch(
      "value",
      (val) => {
        try {
          if (isArray(val) && isString(val[0])) {
            val = val.map((p) => [{ p }]);
            this.$emit("change", val);
          }
          this.monacoInstance &&
            this.monacoInstance.setValue(
              this.isFormat ? JSON.stringify(val, null, 2) : JSON.stringify(val)
            );
        } catch (error) {
          console.log(error);
        }
      },
      { immediate: true }
    );
  },
  methods: {
    setValue(model) {
      this.monacoInstance &&
        this.monacoInstance.setValue(JSON.stringify(model));
    },
    async init() {
      try {
        //创建编辑器
        this.monacoInstance = monaco.editor.create(this.$refs.container, {
          //内容
          // value: 'console.log("Hello world!");',
          //语言
          language: "javascript",
          //自适应调整
          automaticLayout: true,
          //主题，三款：vs、vs-dark、hc-black
          theme: "vs-dark",
          //代码略缩图
          minimap: {
            enabled: true,
          },
        });
        //内容改变事件
        /*       this.monacoInstance.onDidChangeModelContent(e => { });
         */
        //添加按键监听
        this.monacoInstance.addCommand(
          monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S,
          () => {
            try {
              const fnGetVal = new Function(
                `return ${this.monacoInstance.getValue()}`
              );
              this.$emit("change", fnGetVal());
            } catch (error) {
              console.error(error);
              this.setValue(this.value);
              const h = this.$createElement;
              this.$message({
                message: h("p", null, [
                  h("span", null, "JSON 有问题，不转换"),
                  h("i", { style: "color: teal" }),
                ]),
              });
            }
          }
        );
      } catch (error) {
        console.error(error);
      }
    },
  },
};
</script>
<style lang="scss">
</style>
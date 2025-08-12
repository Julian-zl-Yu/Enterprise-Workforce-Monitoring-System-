<template>
  <MainPage
    :configs="configsMainPage"
    class="dev-doc"
  >
    <div class="nav-bar">
      <router-link to="/dev/demo">
        <el-button
          type="primary"
          class="btn-switch"
        >
          组件演示
        </el-button>
      </router-link>
    </div>
    <div class="row-wrapper">
      <div class="toc-wrapp elevation4">
        <el-input
          v-model="filterText"
          placeholder="输入关键字进行过滤"
        />
        <el-tree
          ref="tree"
          class="filter-tree"
          :data="menuTree"
          default-expand-all
          :filter-node-method="filterNode"
          @node-click="treeClick"
        />
      </div>
      <div class="content-wrapper">
        <markdown-it-vue
          class="md-body"
          :content="content"
        />
      </div>
    </div>
  </MainPage>
</template>
<script>
    import { state, defaultFormQueryData } from "./state";
    // import MarkdownItVue from "markdown-it-vue";
    // import "markdown-it-vue/dist/markdown-it-vue.css";
    import mixinsBasePage from "@/mixins/basePage.ts";
    import menuTree from "./menuTree";
    import {getMdFile} from "./docFile";
    import $ from "jquery";
    import {ViewerItem} from "@/components/Form/Viewer/index";

    export default {
        name: "DevDoc",
        // components: {MarkdownItVue},
        mixins: [mixinsBasePage],
        data() {
            return {
                filterText: "",
                menuTree,
                content: "",
                configsMainPage: {moduleName: "devDoc", state}
            };
        },
        watch: {
            filterText(val) {
                this.$refs.tree.filter(val);
            },
            ["$route.query"](query, oldQuery) {
                if (query.content !== oldQuery.content) {
                    this.treeClick();
                }
            }
        },
        beforeDestroy() {
            $("body").off(".showImg");
        },
        created() {
            $("body").on("click.showImg", "img", this.handleClickImg);
        },
        mounted() {
            this.treeClick({label: "Demo", id: this?.$route?.query?.id || "demo"});
        },
        methods: {
            handleClickImg(e) {
                const $e = $(e.target);
                const src = $e.attr("src");
                const id = _.camelCase(src);
                this.$viewer(
                    [
                        new ViewerItem({
                            title: id,
                            name: id,
                            viewType: "img",
                            src
                        })
                    ],
                    0
                );
                console.log("handleClickImg -> e", e);
            },
            async treeClick(info) {
                let res = await getMdFile(info.id);
                this.content = res.default;
                this.goMain({id: info.id});
            },
            filterNode(value, data) {
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            },
            async handleFormSubmit() {
                return new Promise(resolve => setTimeout(resolve, 1000 * 2));
            }
        }
    };
</script>
<style lang="scss" scoped>
    .dev-doc {
        height: 100vh;
        overflow: hidden;

        .nav-bar {
            width: 100%;
            height: 54px;
            display: flex;
            flex-flow: row nowrap;
            align-items: center;
            @include elevation4;
            .btn-switch {
                margin-left: 12px;
            }
        }

        .row-wrapper {
            width: 100%;
            height: 95vh;
            padding: 12px;
            overflow: hidden;
            display: flex;
            flex-flow: row nowrap;
            justify-content: center;
            align-items: flex-start;
            .toc-wrapp {
                margin-right: 24px;
                padding: 24px;
            }
            .content-wrapper {
                flex: 1;
                overflow: auto;
                height: 100%;
                padding: 12px;
                @include elevation4;
            }
        }
    }
</style>
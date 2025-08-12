/**
 * https://cli.vuejs.org/zh/config/
 */
const TerserPlugin = require("terser-webpack-plugin");
const path = require("path");

function addStyleResource(rule) {
    rule.use("style-resource").loader("style-resources-loader")
        .options({
            patterns: [path.resolve(__dirname, "./src/assets/scss/variables.scss")],
        });
}


let thisExports = {
    pwa: {

        iconPaths: {

            favicon32: 'favicon.ico',

            favicon16: 'favicon.ico',

            appleTouchIcon: 'favicon.ico',

            maskIcon: 'favicon.ico',

            msTileImage: 'favicon.ico'

        }

    },
    css: {
        loaderOptions: {
            sass: {
                // This line must in sass option
                implementation: require('sass'),
            },
        },
    },
    ...(() => {
        /* 在开发模式下，单独有一个开发用的，热替换快一点 */
        return process.env.NODE_ENV === "production" ? {} : {
            devServer: {
                open: true,
                port: 8084,
                /* https://webpack.docschina.org/configuration/dev-server/#devserveroverlay */
                overlay: {
                    errors: true,
                    warnings: false
                },
            },
            pages: {
                index: {
                    entry: "src/main.ts",
                    template: "public/index.html",
                    filename: "index.html",
                    title: "开发模式标题",
                    chunks: ["chunk-vendors", "chunk-common", "index"]
                },
                /* 开发具体模块 */
                dev: {
                    entry: "src/dev/dev.ts",
                    template: "public/index.html",
                    filename: "dev.html",
                    title: "Develope",
                }
            }
        };
    })(),
    publicPath: process.env.NODE_ENV === "production" ? "./" : "/",
    chainWebpack: config => {
        if (process.env.NODE_ENV === "production") {
            /*
                        config.optimization.minimizer = [new TerserPlugin({
                            terserOptions: {
                                compress: {
                                    warnings: false,
                                    drop_console: true, //console
                                    drop_debugger: true,
                                    pure_funcs: ["console.log"], //移除console
                                    arrows: false,
                                    collapse_vars: false,
                                    comparisons: false,
                                    computed_props: false,
                                    hoist_funs: false,
                                    hoist_props: false,
                                    hoist_vars: false,
                                    inline: false,
                                    loops: false,
                                    negate_iife: false,
                                    properties: false,
                                    reduce_funcs: false,
                                    reduce_vars: false,
                                    switches: false,
                                    toplevel: false,
                                    typeofs: false,
                                    booleans: true,
                                    if_return: true,
                                    sequences: true,
                                    unused: true,
                                    conditionals: true,
                                    dead_code: true,
                                    evaluate: true
                                },
                                mangle: {
                                    safari10: true
                                }
                            },
                            sourceMap: false,
                            cache: true,
                            parallel: true,
                            extractComments: false
                        })];
                    */
        } else {
            config.module.rule("md").test(/\.md$/).use("raw-loader").loader("raw-loader").end();
        }

        const types = ["vue-modules", "vue", "normal-modules", "normal"];
        types.forEach(type => addStyleResource(config.module.rule("scss").oneOf(type)));
        /*      
        const svgRule = config.module.rule("svg");
        svgRule.uses.clear();
        svgRule.test(/\.svg$/).use("svg-sprite-loader").loader("svg-sprite-loader");
         */
    },
    /* 可以在线编译 */
    runtimeCompiler: true,
    productionSourceMap: false,
    lintOnSave: false,
};
console.log("thisExports", thisExports);
module.exports = thisExports;
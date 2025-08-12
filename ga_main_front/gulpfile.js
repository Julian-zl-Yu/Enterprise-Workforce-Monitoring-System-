var gulp = require("gulp");
var $ = require("gulp-load-plugins")();
var fs = require("fs-extra");
var path = require("path");
var del = require("del");
var theme = {};
var themeList = require("./src/element-ui/config.js").filter(item => !item.hasBuild);
var styleFileDir = "./src/assets/scss";
var styleFileDirTemp = `${styleFileDir}-temp`;
var themeFileDir = "./public/element-theme";
var et = require("element-theme");
var etOptions = require("./package.json")["element-theme"];
var themeFileName = etOptions.config.replace(/.*\/(.+\.scss)/, "$1");
console.log("themeFileName: ", themeFileName);

/**
 * 构建生成主题
 */
gulp.task("themes", async () => {
    function fnCreate(themeList) {
        if (themeList.length >= 1) {
            // 保存当前构建生成的主题对象
            theme = themeList[0];
            console.log("\n");
            console.log("-------------------- 待构建，主题 -------------------------");
            console.log(themeList);
            console.log("\n");
            console.log("-------------------- 构建中，主题 -------------------------");
            console.log(theme);
            console.log("\n");
            // 修改.scss临时文件中的$--color-primary主题变量值
            var data = fs.readFileSync(etOptions.config, "utf8");
            var result = data.replace(/\$--color-primary:(.*) !default;/, `$--color-primary:${theme.color} !default;`);
            fs.writeFileSync(path.resolve(etOptions.config), result);
            console.log("theme.name: ", theme.name);

            // 调用element-theme插件，生成element组件主题
            etOptions.out = `${themeFileDir}/${theme.name}`;
            console.log("etOptions: ",etOptions);
            et.run(etOptions, () => {
                // 生成后，构建同主题色ui.css项目主题
                gulp.start(["styles"], () => {
                    // 递归下一步
                    themeList.splice(0, 1);
                    fnCreate(themeList);
                });
            });
        } else {
            // 删除临时文件
            del(styleFileDirTemp);
            console.log("\n");
            console.log("-------------------- 构建完毕，删除临时文件 -------------------------");
            console.log(styleFileDirTemp);
            console.log("\n");

            // 删除主题不需要的部分文件
            var files = [
                `${themeFileDir}/**/*.css`,
                `!${themeFileDir}/**/index.css`,
                `!${themeFileDir}/**/ui.css`,
                `!${themeFileDir}/**/fonts`
            ];
            del(files);
            console.log("-------------------- 构建完毕，删除主题独立组件文件 -------------------------");
            console.log(files);
            console.log("\n");
        }
    }

    try {
        console.log("1.检查是否存在目标文件夹");

        if (await fs.exists(styleFileDirTemp)) {
            await del(styleFileDirTemp);
            console.log("2.删除临时文件，保证本次操作正常执行");
        }


        if (themeList.length <= 0) {
            return;
        }
        await fs.mkdir(styleFileDirTemp);
        console.log("3.创建临时文件夹");
        await fs.copy(styleFileDir, styleFileDirTemp);
        console.log("4.拷贝一份scss样式文件夹，作为构建的临时处理文件夹");
        await fs.copy(etOptions.config, `${styleFileDirTemp}/${themeFileName}`);
        console.log("5.拷贝element组件scss变量样式文件至临时处理文件夹中，并修改相应配置信息");
        console.log("6.开始构建生成");
        fnCreate(themeList);
    } catch (error) {
        console.error(error);
    }
});

gulp.task("styles", () => {
    return gulp.src([`${styleFileDirTemp}/ui.scss`])
        .pipe($.sass().on("error", $.sass.logError))
        .pipe($.autoprefixer({
            browsers: etOptions.browsers,
            cascade: false
        }))
        .pipe($.cleanCss())
        .pipe($.rename("ui.css"))
        .pipe(gulp.dest(`${themeFileDir}/${theme.name}`));
});
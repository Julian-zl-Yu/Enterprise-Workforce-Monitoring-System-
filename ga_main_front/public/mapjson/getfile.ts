const fs = require("fs-extra");
const path = require("path");
const _ = require("lodash");

(async () => {
    let codeNameMap = await fs.readJSON("./codeName.json");
    let nameCodeMap = _.reduce(codeNameMap, (targetObj, name, code) => {
        if (targetObj[name]) {
            if (/^51/g.test(code)) {
                console.error(`${name}重复${code}`);
            }
        } else {
            targetObj[name] = code;
        }
        return targetObj;
    }, {});

    await fs.ensureDirSync(path.resolve("./new"));
    let data = await fs.readJSON("./510000.json");
    /* 省 */
    data
        .features
        .map(f => {
            return f.id;
        })
        .forEach(async d => {
            /* 市 */
            let info = await fs.readJSON(`./${d}.json`);
            info.features
                .forEach(async f => {
                    if (!f.id) {
                        if (f.properties && f.properties.name) {
                            const name = f.properties.name;
                            const id = nameCodeMap[name];
                            if (/^51/g.test(id)) {
                                f.id = id;
                            } else {
                                console.error(`${name} 没有匹配的adcode ${id}`);
                            }
                        } else {
                            console.error(`${JSON.stringify(f)}`);
                        }
                    }

                    if (f.id) {
                        await fs.writeJSON(path.resolve(`./new/${f.id}.json`), {
                            type: "FeatureCollection",
                            features: [f]
                        });
                    } else {
                        console.error(f.properties && f.properties.name);
                    }
                });
        });

})();
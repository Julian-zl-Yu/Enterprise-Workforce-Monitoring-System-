const path = require("path");

function resolve(dir) {
    return path.join(__dirname, dir);
}

module.exports = {
    "jumpToAliasFile.alias": {
        "@": "./src",
    },
    resolve: {
        alias: {
            "@": resolve("src")
        }
    }
};
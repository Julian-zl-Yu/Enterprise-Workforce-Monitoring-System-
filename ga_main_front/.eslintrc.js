module.exports = {
    "env": {
        "browser": true,
        "es6": true
    },
    parser: '@typescript-eslint/parser',
    "extends": [
        "plugin:vue/essential",
        "plugin:vue/recommended",
        "eslint:recommended",
        "@vue/typescript"
    ],
    "parserOptions": {
        "ecmaFeatures": {
            "jsx": true
        },
        "ecmaVersion": 2018,
        "sourceType": "module"
    },
    "rules": {
        "indent": [
            "error",
            "space"
        ],
        "linebreak-style": [
            "error",
            "windows"
        ],
        "quotes": [
            "error",
            "double"
        ],
        "semi": [
            "error",
            "always"
        ]
    }
};
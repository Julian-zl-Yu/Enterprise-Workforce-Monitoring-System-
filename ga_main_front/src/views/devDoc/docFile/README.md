# GKCloud

# Getting start

[跨域资源共享](https://www.ruanyifeng.com/blog/2016/04/cors.html)option请求确认服务器是否支持，会出现两次请求，属正常情况。

- delete跨域错误 可能是接口错误，不支持批量删除（接口地址不对）

### svn 地址

- 客户端：https://www.xinyegk.com/svn/项目档案/2003金键云实名制系统/08 代码档案/企业端代码/security-enterprise-FrontBackSplit_2.5.0/security-enterprise-FrontBackSplit_2.5.0/security-enterprise-admin
- 服务端：https://www.xinyegk.com/svn/项目档案/2003金键云实名制系统/08 代码档案/企业端代码/security-enterprise-FrontBackSplit_2.5.0/security-enterprise-FrontBackSplit_2.5.0/security-enterprise
- src\views\devDoc\docFile\README.md

### 安装与运行
 
 参看 [security-enterprise-admin(前端文档).pdf](./security-enterprise-admin(前端文档).pdf)
 
 1. 服务端你们自己搞；
 1. 安装NodeJS运行环境[NodeJS](https://nodejs.org/en/) 选择安装位置（非系统盘，路径无中文无空格）无脑下一步直到安装完成；
 1. 完成后打开命令行工具 运行`npm config set registry https://registry.npm.taobao.org` 修改成国内源地址；
 1. 完成后在cd到前端项目根目录（与`package.json`同一级）运行 `npm install` 安装依赖；
 1. 完成后运行`npm start`即可启动开发模式下的前端项目，编译完成后会自动打开浏览器。默认端口根据`vue.config.js`里面的`port`;

 说明
 
- `package.json` 职责跟maven pom.xml 一致；
- `npm install` 会根据`package.json`文件安装依赖包;
- `npm install` 需要在第一次启动项目**前**、依赖包发生变动**后**，运行（如果发生变化未重新install，控制台会报错，根据提示，重新运行`npm install`即可）；
- `npm start`是`npm run start`的简写。npm 为常用命令`start build test`提供了省略`run`的简写，其他的命令需要写全，比如`npm run serve`
- `start`命令实际上对应`package.json`里面的`scripts`:

 ```bash
  "scripts": {
    "start": "npm run serve",//以开发模式启动项目
    "serve": "vue-cli-service serve",
    "build:old": "vue-cli-service build",
    "build": "vue-cli-service build --modern",
    ...
  },
 ```

```js
  route["component"] = () => import(`@/views/modules/${menuURL}`);
```

- [http://localhost:8084/#/dev/doc?id=readme](http://localhost:8084/#/dev/doc?id=readme) 是本文档开发模式下的线上预览地址

See [Configuration Reference](https://cli.vuejs.org/config/).

- https://cli.vuejs.org/zh/guide/mode-and-env.html
- https://cli.vuejs.org/zh/guide/mode-and-env.html#%E6%A8%A1%E5%BC%8F

- cross-env 跨平台运行环境设置
- npm i @babel/plugin-proposal-optional-chaining -D
- npm i -g typescript//(枚举 装饰器)

- register
  - 91310000775785552L

- 工具函数
  - [lodash](https://www.lodashjs.com/)
    - [lodash case converter](https://medium.com/@robertsavian/javascript-case-converters-using-lodash-4f2f964091cc)
  - [JavaScript工具函数](https://juejin.im/post/5edb6c6be51d4578a2555a9b#heading-205?tdsourcetag=s_pctim_aiomsg)  

- `$viewer()` 文件预览 通用方法
```js
//预览文件的数组，选择预览文件的下标
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
```

- getAreaCodeNodeFromDB(areacode,isAll) 获取地区中文名，如果 isAll为真值，显示所有级别

helper deepClone 有问题



## Store

model
rule

### 全局变量

- `window.AREA_CODE_TREE`:[lable,value,children]
- `window.AREA_CODE_MAP`:{id:name}
- `SITE_CONFIG.dictList`
- `window.AREA_CODE_ORIGIN `

### Types.ts

- 默认是数字0开始，0 假值 可以作为默认 判断
- 有一些，比如状态 需要字符串，可以定义为字符串

所有类型 
按钮
表单控件
第三方库 libScript.js

### 第三方库

高德地图
- [IP城市定位](https://lbs.amap.com/api/javascript-api/example/location/get-city-name-by-ip-location)
- [地图初始IP城市定位](https://lbs.amap.com/api/javascript-api/example/location/map-is-initially-loaded-into-the-current-city)
- [行政区查询](https://lbs.amap.com/api/javascript-api/guide/services/district-search)

- ~~[zangodb](https://github.com/erikolson186/zangodb)~
- [jsstore](https://jsstore.net/tutorial/in/)

### login

BtnDebounce ref submitBtn.submit() 包装类，统一处理防止重复提交

### CForm

- Form/index.js 注册 formItem相关的组件

作用： mounted时注册模块数据状态
@enter 表单回车
- 重置 reset dsl 保持原始的默认表单
- `readonly` 用于查看模式（纯数据展示），disabled用于disabled（能看见控件类型）
  - 独立各控件数据加载方法


  - 添加各自的mixin

configs 注意是址引用导致修改属性会影响其他引用的地方：DevDemo 两个items相同的CForm 一个是readOnly 设置readOnly属性导致另一个表也是readOnly，应该拷贝一个出来；
使用 `import cloneDeep from "lodash/cloneDeep"`


#### 详情模式

单独的数据
```html
        <!--区别disabled readOnly就是只加载展示的信息，没有element控件的渲染-->
        <div
                v-if="attrReadOnly"
                class="form-item all-item wrapper"
        >
            <!-- "form-item all-item wrapper"与tips 超长才显示有关联，应该保持一致 v-tips.js if (elP.className === "form-item all-item wrapper") -->
            <!-- {{ configs }} -->
            <div class="el-form-item__label now-you-see-me-readonly" :data-value="setReadonlyText(value)||'-'">
                {{ textWhenReadOnly }}
            </div>
        </div>
```

`:data-value=setReadonlyText` 触发数据更新,各组件需要实现获取显示内容的方法

#### 布局

- tableLayout:true以table表示列表 属性colspan rowspan 可用
  - DSLFormLayout为二维数组

  ```js
                 configsCFormWithLayout: {
                    moduleName: "DevDemo",
                    DSLFormItems,
                    DSLFormItemRule,
                    //tableLayout:true,//有DSLFormLayout即可
                    DSLFormLayout
                },
  ```
  
- 默认element layout el-row => el-col 属性span 可用


CBreadcrumb

jg08 {item,index}

### FormItem

type = Cascader
initComponentType = > fnc initCascader()
setReadonlyText()=>set方法用于只读模式
---
**isEdit**


#### 异步验证

```js
const AsyncValidatorRequired = {
    vm: this,/*当前vue实例*/
    form: "form",/*CForm 的ref name */
    prop: "devicename"/*需要校验的字段prop name*/
};

DSLFormItemRule.devicename.push(
    fnAsyncValidator({
        ...AsyncValidatorRequired,
        validateRequest: (value/* 需要验证的值 */) => this.$http.post("/sys/user/nfo", value)
    })
);

```

#### Money <===> Currency
```js
    cp01DTO_capitalcurrencytype: {
        prop: "cp01DTO_capitalcurrencytype",
        label: "注册资本币种",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "CAPITALCURRENCYTYPE"
    },
    cp01DTO_regcapital: {
        prop: "cp01DTO_regcapital",
        label: "注册资本",
        type: FORM_ITEM_TYPE.Money,
        currency: true//响应币种的变化
    }
...

this.$watch(
        "$store.state.EnterpriseCp02.formData.cp01DTO_capitalcurrencytype",
        cp01DTO_capitalcurrencytype => {
            this.configsCFormCp01DTO.DSLFormItems.cp01DTO_regcapital.currency = cp01DTO_capitalcurrencytype;
        },
        {immediate: true}
    );
```

### CFormQuery

作用：查询表单 默认有查询 重置 更多 按钮
当查询项超过三个出现更多按钮

- configsCFormQuery：{
   moduleName: "ps02",
        keyForm: "formQueryData",//在state里面对应的表单索引字段
        DSLFormItems: DSLFormItemsQuery//查询表单的查询项描述
}

BtnDebounce

```js


const isShowByScope = scope => {
        const PASS = "1"
        if (scope.row.status === PASS) {
            return false;
        } else {
            return true;
        }
    };
// ......
{
        type: BTN_TYPE.detail,
        shiro: "enterprise:pj06:checkProjectInfo",
        /*根据行记录控制按钮的显隐*/
        isShowByScope,
        text: "审核",
        click: scope => _this.goSub({v: "update", id: scope.row.pj0601})
},

```

table column
PassOrNot

- MainPage
  负责模块状态的注册
  =>configs{
    configsMainPage: {
          moduleName: "ps02",//state 模块名 this.$store.state.ps02引用
          state//具体的结构
          shiro:["token"]//可以是数组，多个token；可以是字符串，一个token
        },
  }
- `<div class="view">`
  默认显示的页面组件 通常是 查询框 查询列表
- `import basePage from "@/mixins/basePage.ts";`
  basePage基本的页面组件行为：goMain goSub currentView当前显示的组件
- SubPage
  -  `<AddOrUpdate v-if="~['update'].indexOf(currentView)" ref="addOrUpdate" @refreshDataList="getDataList" />` 外部组件用`~['update'].indexOf(currentView)`控制显隐
  - 


## regualr

enterprise/(\w*)([^']*?)'

enterprise/$1/$1'

## route

tab 切换不会改变所有参数，通过对比，不会触发更新。
如果参数变化，视为路由切换，提示可触发更新
created 时候调用的$route信息初始化会为参数添加_t参数

created => init => 
- 是否相同的路径 
  - 一个模块 main => sub => 不同 => getInfo
  - 不同模块 tab 切换 => 相同 不切换
使用 keep alive 缓存DOM

created => actived => int（区别是在active的里面调用init）
- v-if 在tab切换时有性能问题
 

## MainPage

## SubPage

defaultRowData 增加一行，默认的初始数据

## pdf viewer

- [pdf demo文件](http://localhost:8084/devdoc/demo/pdf.pdf)
- [预览地址](http://localhost:8084/pdfjs/web/viewer.html?file=http://localhost:8084/devdoc/demo/pdf.pdf&ssltoken=thisusertoken)

文件访问需要token 修改viewer.js 1769:
```js
    file = "file" in params ? params.file : _app_options.AppOptions.get("defaultUrl");
    const token= params.ssltoken;
    if(token){
    file=`${file}?token=${token}`;
    
    /* 修改不允许跨域 注释掉*/
    if (origin !== viewerOrigin && protocol !== "blob:") {
         throw new Error("file origin does not match viewer's");
       }
}
```

## map 根据汉字名通过indexedDB查询areaCode


## Cache

dataBase 添加 table kvCache 专用于数据项的缓存，每次进入系统会清空

DB_NAME修改数据库表结构之后需要增加DB_NAME的版本 old_name

## fixed-parent

## ScrollToPane

```js
  async mounted() {
  /* 获取信息后会对布局产生影响，需要在数据完成之后在修改滑动信息 */
    await this.getData();
    this.$nextTick(() => {
      this.configsScroll = {
        parent: this.$el,
        refs: this.$refs,
        contents: [
          { prop: "basic", title: "基本信息" },
          { prop: "social", title: "社保卡信息" },
          { prop: "bank", title: "银行信息" },
          { prop: "part", title: "参建信息" },
          { prop: "other", title: "附属信息" },
        ],
      };
    });
  },

```

## 表单数据

<Btn :configs="{ text: 'DevStoreForm', click: DevStoreForm }" />
<Btn :configs="{ text: 'DevRestoreForm', click: DevRestoreForm }" />

import { DevRestoreForm, DevStoreForm } from "@/utils/transformData";


DevStoreForm() {
  DevStoreForm(moduleName, this.formData);
},
DevRestoreForm() {
  DevRestoreForm(moduleName, (data: any) => {
    this.formData = data;
  });
},


let tm0101Label = await this.$refs.query.getLabel("tm0101");

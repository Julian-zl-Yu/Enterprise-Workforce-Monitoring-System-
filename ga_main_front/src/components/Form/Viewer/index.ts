import Viewer from "./Viewer";
import ViewerItemComponent from "./ViewerItem";
import ViewerWrapper from "./ViewerWrapper";
import Vue from "vue";
import MyElImage from "./image";
import {
    FILE_TYPE
} from "@/components/Types";
import { SITE_CONFIG } from "@/main.config";
import { isURL, isRelativeURL, isBlobURL } from "@/utils/validate.ts";
import { addToken, PDFAddToken } from "@/utils/index.ts";

export class ViewerItem {
    /*{
        id: 唯一标志,
        title:显示在页面的标题
        name: 原始类型 ,
        viewType: "img"预览组件用于判断显示方式的字段 与 types.ts FILE_TYPE 对应,
        src: "https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg"
        可能的功能 排序 过滤 需要时间 分类 或者其他的

    }*/
    constructor(options) {
        /* TODO:check options */
        this.id = options?.id || options?.ot0101 || "";
        this.ot0101 = options?.id || options?.ot0101 || "";
        this.title = options?.title || "";
        this.name = options?.name || "";
        this.viewType = options?.viewType || "img";
        let src = options?.src || options?.url;
        /*用于预览的图片和缩略图*/
        /*TODO:处理 pdf 和 图片的封面*/
        /**/
        if (src && isBlobURL(src)) {
            this.imgSrc = this.src = src;
        } else if (src) {
            /*如果是HTTP的URL？*/
            if (isURL(src)) {
                this.imgSrc = this.src = src;
                /*如果是相对路径的URL ./doc/img/a.png？*/
                /* !ERROR:禁止../一类的url */
            } else if (isRelativeURL(src)) {
                this.imgSrc = this.src = `${window.location.origin}/${src.substring(2)}`;
            } else {
                src = `${SITE_CONFIG.apiURL}${src}`;
                switch (this.viewType) {
                    case FILE_TYPE[FILE_TYPE.pdf]: {
                        this.src = PDFAddToken(src);
                        this.imgSrc = "";
                        break;
                    }
                    default: {
                        this.imgSrc = this.src = addToken(src);
                        break;
                    }
                }
            }
        } else {
            try {
                this.src = window.URL.createObjectURL(options);
                this.imgSrc = src;
            } catch {
                console.error("获取的ViewItem 信息有误");
            }
        }
    }
}

const imageComponent = new Vue({
    data() {
        return {
            initialIndex: 0,
            items: []
        };
    },
    methods: {
        init(viewItems, index) {
            this.initialIndex = index || 0;
            this.items = viewItems;
            this.$nextTick(() => this.$refs.previewer.clickHandler());
        },
    },
    template: `
        <MyElImage ref="previewer" style="width: 100px; height: 100px" :initialIndex="initialIndex" :items="items"/>
    `
});
export default {
    install: (Vue) => {
        Vue.component(MyElImage.name, MyElImage);
        Vue.component(ViewerItemComponent.name, ViewerItemComponent);
        Vue.component(ViewerWrapper.name, ViewerWrapper);
        Vue.component(Viewer.name, Viewer);
        setViewerToVuePrototype(Vue);
    }
};

function setViewerToVuePrototype(Vue) {
    /*lazyload*/
    let ViewerInstance = false;

    function Viewer(viewItems = {
        viewType: FILE_TYPE.img,
        src: "",
        srcList: ""
    }, index) {
       /*  ViewerInstance = ViewerInstance ? ViewerInstance : (() => {
            ViewerInstance = imageComponent.$mount();
            document.body.appendChild(ViewerInstance.$el);
            return ViewerInstance;
        })(); */

        // 解决当预览pdf后，在预览excle的问题
        ViewerInstance = (() => {
            ViewerInstance = imageComponent.$mount();
            document.body.appendChild(ViewerInstance.$el);
            return ViewerInstance;
        })();
        
        ViewerInstance.init(viewItems, index);
        return ViewerInstance;
    }

    Vue.prototype.$viewer = Viewer;
    /*
     * TODO:代理
     * Viewer instance 懒加载
     * viewer组件 实例方法
     * elimg 实例方法
     * 方法查找顺序：instance->viewerInstance->elImgInstance
     * 
     * */
    /*https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Proxy*/
    /* const handler = {
         get: function (target, prop, receiver) {
             console.log("setViewerToVuePrototype -> target, prop, receiver", target, prop, receiver);
             return;
         },
         apply: function (target /!*目标对象（函数）*!/, thisArg /!*被调用时的上下文对象*!/, argumentsList /!*被调用时的参数数组*!/) {
             ViewerInstance = ViewerInstance ? ViewerInstance : (() => {
                 ViewerInstance = imageComponent.$mount();
                 document.body.appendChild(ViewerInstance.$el);
                 return ViewerInstance;
             })();
             ViewerInstance.init(argumentsList[0]);
             ViewerInstance.show(argumentsList[1]);
             return ViewerInstance;
         }
     };
     const ViewerProxy = new Proxy(Viewer, handler);*/
}

export function makeViewerItem(ViewerItemOptionsArray = []): ViewerItem[] {

    /*const res = ViewerItemOptionsArray.map(options => new ViewerItem(options));
    return res;*/
    return ViewerItemOptionsArray.map(options => new ViewerItem(options));
}
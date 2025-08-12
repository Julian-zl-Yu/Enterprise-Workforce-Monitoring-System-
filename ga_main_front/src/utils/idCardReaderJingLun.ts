import Vue from "vue";
import {
    getNationByChineseName,
    addBase64ImageHeader,
} from "@/utils/index.ts";
import {
    toDate,
} from "@/utils/transformData";

const element = new Vue();

function privateAlert(msg) {
    element.$alert(msg);
}

//创建读卡控件
var CertCtl = new IDCertCtl();

//身份证读卡控件创建
function IDCertCtl() {
    //创建用于与服务交换数据的对象
    this.xhr = createXmlHttp();
    this.type = "CertCtl";
    this.height = 0;
    this.width = 0;
    //连接
    this.connect = CertCtl_connect;
    //断开
    this.disconnect = CertCtl_disconnect;
    //获取状态
    this.getStatus = CertCtl_getStatus;
    //读卡
    this.readCert = CertCtl_readCert;
    //读IC卡序列号
    this.readICCardSN = CertCtl_readICCardSN;
    //读身份证物理卡号
    this.readIDCardSN = CertCtl_readIDCardSN;
}

//创建XMLHttpRequest 对象，用于在后台与服务器交换数据
function createXmlHttp() {
    var xmlHttp = null;
    //根据window.XMLHttpRequest对象是否存在使用不同的创建方式
    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest(); //FireFox、Opera等浏览器支持的创建方式
    } else {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP"); //IE浏览器支持的创建方式
    }
    return xmlHttp;
}

//连接方法
function CertCtl_connect() {
    return new Promise((resolve, reject) => {
        CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/connect", true);
        CertCtl.xhr.onreadystatechange = () => {
            //返回值readyState   0: 请求未初始化
            //				    1: 服务器连接已建立
            //				    2：请求已接收
            //				    3: 请求处理中
            //				    4: 请求已完成，且响应已就绪
            //返回值status      200: "OK"
            //					404: 未找到页面
            //当返回值readyState为4且status为200时,为查询成功;
            console.log("CertCtl.xhr.readyState", CertCtl.xhr.readyState, "CertCtl.xhr.status", CertCtl.xhr.status);
            if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
                try {
                    resolve(JSON.parse(CertCtl.xhr.responseText));
                } catch (error) {
                    reject(error);
                }
            }
        };
        CertCtl.xhr.onerror = (e) => {
            reject(new Error("未启动读卡插件!"));
        };
        CertCtl.xhr.send();
    });
}

//断开方法
function CertCtl_disconnect() {
    var result = "";
    //创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
    CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/disconnect", false);
    //发送请求
    try {
        CertCtl.xhr.send();
    } catch (e) {
        console.error(e);
    }
    if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
        result = CertCtl.xhr.responseText;
    }
    return result;
}


//获取状态方法
function CertCtl_getStatus() {
    var result = "";
    //创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
    CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/getStatus", false);
    //发送请求
    try {
        CertCtl.xhr.send();
    } catch (e) {
        console.error(e);
    }
    if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
        result = CertCtl.xhr.responseText;
    }
    return result;
}

//执行读卡操作
function CertCtl_readCert() {
    return new Promise((resolve, reject) => {
        try {
            //创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
            CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/readCard", true);
            CertCtl.xhr.onreadystatechange = () => {
                console.log("CertCtl.xhr.readyState", CertCtl.xhr.readyState, "CertCtl.xhr.status", CertCtl.xhr.status);
                if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
                    resolve(CertCtl.xhr.responseText);
                }
            };
            //发送请求
            CertCtl.xhr.send();
        } catch (e) {
            reject(e);
        }
    });

}

//获取IC卡序列号
function CertCtl_readICCardSN() {
    var result = "";
    //创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
    CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/readICCardSN", false);
    //发送请求
    try {
        CertCtl.xhr.send();
    } catch (e) {
        console.error(e);
    }
    if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
        result = CertCtl.xhr.responseText;
    }
    return result;
}

//获取身份证物理卡号
function CertCtl_readIDCardSN() {
    var result = "";
    //创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
    CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/readIDCardSN", false);
    //发送请求
    try {
        CertCtl.xhr.send();
    } catch (e) {
        console.error(e);
    }
    if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
        result = CertCtl.xhr.responseText;
    }
    return result;
}

//连接方法
async function connect() {
    await CertCtl.connect();
    connect.isConnected = true;
    return connect.isConnected;
}

//断开连接方法
function disconnect() {
    try {
        //调用对应的断开连接方法,并赋值给result
        var result = CertCtl.disconnect();
        //如果result为空,代表读卡插件未启动
        if (result == "") {
            privateAlert("未启动读卡插件!");
        } else {
            //result页面回显
            this.result = result;
        }
    } catch (e) {
        console.error(e);
    }
}

//获取状态方法
function getStatus() {

    try {
        //调用对应的获取状态方法,并赋值给result
        var result = CertCtl.getStatus();
        //如果result为空,代表读卡插件未启动
        if (result == "") {
            privateAlert("未启动读卡插件!");
        } else {
            //result页面回显
            this.result = result;
        }
    } catch (e) {
        console.error(e);
    }
}

//读卡方法
async function readCert() {
    //调用对应的读卡方法
    let result = await CertCtl.readCert();
    result = JSON.parse(result);
    //如果result为空,代表读卡插件未启动
    /*"{"resultFlag":-1,"errorMsg":"端口打开失败"}*/
    if (result.resultFlag == -1) {
        switch (result.errorMsg) {
            case "端口打开失败":
                throw new Error("端口打开失败，请确认是否已连接读卡设备。");
                break;
            default:
                throw new Error(result.errorMsg);
                break;
        }
    } else {
        return result.resultContent;
    }
}

function readICCardSN() {

    try {
        //调用对应的获取状态方法,并赋值给result
        var result = CertCtl.readICCardSN();
        //如果result为空,代表读卡插件未启动
        if (result == "") {
            privateAlert("未启动读卡插件!");
        } else {
            //result页面回显
            this.result = result;
        }
    } catch (e) {
        console.error(e);
    }
}

function readIDCardSN() {

    try {
        //调用对应的获取状态方法,并赋值给result
        var result = CertCtl.readIDCardSN();
        //如果result为空,代表读卡插件未启动
        if (result == "") {
            privateAlert("未启动读卡插件!");
        } else {
            //result页面回显
            this.result = result;
        }
    } catch (e) {
        console.error(e);
    }
}

export async function readCard() {
    /*
    bornDay: ""
    certAddress: ""
    certNumber: ""
    certOrg: ""
    certType: 1
    effDate: "20110613"
    expDate: "20210613"
    gender: 1
    identityPhotoSrc: "V0xmAH4AMgAA/4UcUVFRPnEN1WTzFsVvimauUWbGYvF5ejWjPrYt3a0ZrKj9jb6diBpSF34JRdw7o1X0LWEMPLfyg6URMIwzrlHaXVTz1wm+OuQ8OatCx2bRUa7XUlFRWj6QRZWnXwvQUo90qAwsN9eSaYVQefGEqQALPOsqKA6IpYUMOxb3WxcELG/jjAQZc5r2jF7GMVOv4SX0CrTs1KYjAs4KA5hkudLbl+9W5NM11a99zEyIuJF9HKhpydxgUurXCBxSDfSZRRfwtdAUfHO0ej1Yn5OE4ZteaVfRBg+h+ffMLujMT5LnP7vm5QtItAYLTVf21AsdWFm2lkQ/IeVEY4aT92Mkm19gxWuxufBDhgIhGs2wdN38MoXfP9ee5v6nCEMaXKqdvoGlalZZO2dJ+kdNIAy8EUdikyZjcCKFUt47VQB1UvhykyzVpiYPX1p/HK3adE2LN+likQy5QYjKe+VAOef/Y1PFB493on+GTOhxQ6xJ6nMJA5amtnjECTDyhArN7fw0oiwd0P0ka6Dwh2pGljpHeIph0YZuvtf0qQJwY74UXKfzgjmLyGGVBtkqFV/b23A69O7/G4r8N7Ex8E6rPx55tpOznj4ZInu7SSwHfma95UVSqtyjH58VdrSwTT6ObI4Iv+ORYWytb2Ok7zYOivqrqdoxLO8ujCkZcGeVPGK0JJ3HxPsEraNnS6U8RLE8ofTYE/qCbZKm8SJRGW6pCMialHJrMa5RqzLiE9i7Ub93Uoh2baAWdF+Qkqbdi441LlU9gH07q0GZ/WtOWTMZ8fyBCZFkpInNAfhNptleLJzAUcg6hCElz0J0MArVbbSXQxNZzleBfmb6CVr4l33tS62uUSG/ZR4+7ILVlLvmxQNzqebpHX6trxSv08bp8ZcQkB1WK3SfCbnQjvT4ueHvb5AXqn6LXlREoYy3WQFHve4AUHPO5ugfr14PO/9LJCHN7aQzS4iIMHTy7HDU1ySew3Rn5bi8yOnBAOW3NvCDznCJkbhycGV4xuw59t8imOcXe3XXeQf4dag4lnyRI6atFSrqycnU6GFgOSisoK5RRolK7aREDYrVssx0WOUD7kU5UELNxr5oyZxLocDpWAWkGLLlb8/4YlXOL5hnntiRyX+jAkZeoTpzAZbX9P6Cb5taPgIxUiPwD58BiQ6sbxbrLYKTv7Ip7W2id4C+Mt1Wmf7sclqt7jjWjXt9EP+ZDbg5SI+pRsdJm91+fqbPCXvFevUIc3tN3OCaJOG9m8ZeE2laPpBFlaCCJVF6+L4u6bwRu0+x03ROOKLXmxZT5P3SMn/c02vYnqBJHYHBCc+jSboe07EW8P7A1eUDgz2YdVJv/lkiGRSdkQYakw=="
    identityPic: "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"
    nation: "汉"
    partyName: ""
    */
    element.$loading();
    try {
        return (connect.isConnected || await connect()) ? await readCert() : false;
    } catch (error) {
        //"未启动读卡插件!"
        privateAlert(error.message);
    } finally {
        element.$loading().close();
    }
    return false;
}

export function getPersonInfoFrom(info) {
    const headimageurl = addBase64ImageHeader(
        info.identityPic
    );
    const name = info.partyName;
    const idcardnumber = info.certNumber;
    const gender = String(info.gender);
    const bornDay = info.bornDay;
    /* 住址 */
    const address = info.certAddress;
    /* 民族 */
    const nation = getNationByChineseName(info.nation);
    /* (1-中国居民身份证，50-外国人永久居住证证，54-台湾居住证，55-港澳居住证) */
    const idcardtype = info.certType === 1 ? "1" : "2";
    /* 发证机关 */
    const grantorg = info.certOrg;
    const startdate = toDate(info.effDate);
    const isForever = !/^[\d]+$/.test(info.expDate);
    const expirydate = toDate(
        isForever ? "29991230" : info.expDate
    );
    /* 出生日期 */
    const birthday = toDate(info.bornDay);

    return {
        bornDay,
        gender,
        headimageurl,
        name,
        idcardnumber,
        address,
        nation,
        idcardtype,
        grantorg,
        startdate,
        expirydate,
        birthday,
    };
}
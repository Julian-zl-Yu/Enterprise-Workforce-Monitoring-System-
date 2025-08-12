//@ts-nocheck
import Vue from "vue";
import isString from "lodash/isString";
const element = new Vue();

function privateAlert(msg) {
    element.$alert(msg);
}


function ajax(options) {
    if (options.type == null) {
        options.type = "POST";
    }

    if (options.url == null) {
        options.url = "";
    }

    if (options.timeout == null) {
        options.timeout = 5000;
    }

    if (options.onComplate == null) {
        options.onComplate = function () {
        }
    }

    if (options.onError == null) {
        options.onError = function () {
        }
    }

    if (options.onSuccess == null) {
        options.onSuccess = function () {
        }
    }

    if (options.data) {
        options.data = "";
    }

    var xml;
    if (typeof ActiveXObject != 'undefined') {
        var aVersions = ["Microsoft.XMLHTTP", "Msxml2.XMLHttp.6.0", "Msxml2.XMLHttp.5.0", "Msxml2.XMLHttp.4.0", "Msxml2.XMLHttp.3.0"];
        for (var i = 0; i < aVersions.length; i++) {
            try {
                xml = new ActiveXObject(aVersions[i]);
            } catch (e) { }
        }
    } else if (typeof XMLHttpRequest != 'undefined') {
        xml = new XMLHttpRequest();
    }

    xml.open(options.type, options.url, true);

    var timeoutLength = options.timeout;

    var requestDone = false;

    setTimeout(function () {
        requestDone = true;
    }, timeoutLength);

    xml.onreadystatechange = function () {

        if (xml.readyState == 4 && !requestDone) {

            if (httpSuccess(xml)) {

                options.onSuccess(httpData(xml));
            }

            else {
                options.onError();
            }

            options.onComplate();

            xml = null;
        }
    };

    xml.send();

    function httpSuccess(r) {
        try {
            return !r.status && location.protocol == "file:"
                ||
                (r.status >= 200 && r.status <= 300)
                ||
                r.status == 304
                ||

                navigator.userAgent.indexOf("Safari") >= 0
                && typeof r.status == "undefined";
        } catch (e) {
        }
        return false;
    }

    function httpData(r) {
        var ct = r.getResponseHeader("responseType");
        if (ct) {
            if (ct == "script") {
                eval.call(window, data);
            }
            if (ct == "xml") {
                return r.responseXML;
            }
            if (ct == "json") {
                return JSON.parse(r.responseText);
            }
        }
        return r.responseText;
    }
}


function connect() {
    function onSuccess(data) {
        let str = "提示:" + data.match("\"errorMsg\" : (.*)")[1] + "\n返回值：" + data.match("\"resultFlag\" : (.*)")[1];
        alert(str)
    }

    // clearForm();
    var options = new Object();
    options.type = "GET";
    //options.url = "/OpenDevice";
    options.url = "http://127.0.0.1:19196/OpenDevice";
    options.timeout = 5000;
    options.onSuccess = onSuccess;
    ajax(options);
}


function disconnect() {

    function onSuccess(data) {
        let str = "提示:" + data.match("\"errorMsg\" : (.*)")[1] + "\n返回值：" + data.match("\"resultFlag\" : (.*)")[1];
        alert(str)
    }

    // clearForm();
    var options = new Object();
    options.type = "GET";
    //options.url="CloseDevice";
    options.url = "http://127.0.0.1:19196/CloseDevice";
    options.timeout = 5000;
    options.onSuccess = onSuccess;
    ajax(options);
}

function readCert() {
	function onSuccess(data){
		var errorMsg = data.match("\"errorMsg\" : (.*)")[1];
		var resultFlag = data.match("\"resultFlag\" : (.*)")[1];
        let str = "提示:" + errorMsg + "\n返回值：" + resultFlag;
        alert(str)
		
	}
	// clearForm();
	var startDt = new Date();
	var options=new Object();
	options.type="GET";
	//options.url="readcard";
	options.url = "http://127.0.0.1:19196/readcard";
	options.timeout=5000;
	options.onSuccess=onSuccess;
	ajax(options);
}

async function ReadDevcie() {
    var options = new Object();
    options.type = "GET";
    options.url = "readcard";
    options.timeout = 5000;
    return ajax(options);
}


export async function readCard() {
    element.$loading();
    try {

        connect()

        setTimeout(() => {
            readCert()
        }, 2000);

        // await OpenDevice();
        // const res = await ReadDevcie();
        // await CloseDevice();
        return res;
    } catch (msg) {
        //"未启动读卡插件!"
        privateAlert(`${msg},请确认已开启读卡服务并已连接读卡器`);
    } finally {
        element.$loading().close();
    }
    return false;
}
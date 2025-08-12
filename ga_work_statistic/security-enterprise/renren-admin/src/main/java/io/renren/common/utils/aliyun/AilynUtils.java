package io.renren.common.utils.aliyun;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * 阿里云接口调用工具类
 *
 * @Author huangxi
 * @Date 2020-08-20 13:50
 */

public class AilynUtils {
    /**
     * 获取企业公司信息接口地址
     */
    private final static String API_STORE_URL = "http://cardnotwo.market.alicloudapi.com/company";

    /**
     * 阿里账号AppSecret
     */
    private final static String APP_SECRET = "0470766a8e2440e6bbd59ca4520861b7";

    /**
     * 阿里云AppCode
     */
    private final static String APP_CODE = "8a1f5f40d1764f73ad99818f4e51c64e";


    /**
     * 查询企业的工商信息
     *
     * @param queryParam 完整的公司名称、注册号、信用代码、组织机构代码、税务登记号
     * @return string 响应结果字符串
     */
    public static CompanyDTO getApiStore(String queryParam) {
        HashMap<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("com", queryParam);
        paramMap.put("AppSecret", APP_SECRET);
        String responseBody = HttpRequest.post(API_STORE_URL)
                .form(paramMap)
                //注意APPCODE后边有空格
                .header("Authorization", "APPCODE " + APP_CODE)
                .execute()
                .body();
        JSONObject jsonObject = JSON.parseObject(responseBody);
        return JSONObject.toJavaObject(jsonObject.getJSONObject("result"), CompanyDTO.class);
    }
}

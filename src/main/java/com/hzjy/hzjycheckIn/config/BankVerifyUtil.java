package com.hzjy.hzjycheckIn.config;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BankVerifyUtil {
    @Value("${bank.verify.appCode}")
    private String appcode;

    public String verify(String bankCardNumber, String idCard, String mobile, String name) {
        String host = "https://jumdres.market.alicloudapi.com";
        String path = "/bankcard/4-validate";
        String method = "POST";

        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("bankcard_number", bankCardNumber);
        bodys.put("idcard_number", idCard);
        bodys.put("mobile_number", mobile);
        bodys.put("name", name);
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            String result = EntityUtils.toString(response.getEntity());

            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}

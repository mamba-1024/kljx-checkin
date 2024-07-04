package com.hzjy.hzjycheckIn.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BankAuthenticationUtil {

    private static final String APP_ID = "your_app_id";  // 替换为你的APP_ID
    private static final String APP_KEY = "your_app_key";  // 替换为你的APP_KEY
    private static final String AUTH_URL = "https://api.test.com/auth";  // 实名认证接口URL，替换为真实的URL

    /**
     * 银行卡四要素实名认证
     *
     * @param realName    真实姓名
     * @param idCard      身份证号码
     * @param bankCardNo  银行卡号
     * @param mobilePhone 预留手机号
     * @return 认证结果，true为认证成功，false为认证失败
     */
    public static boolean bankAuthentication(String realName, String idCard, String bankCardNo, String mobilePhone) {
        RestTemplate restTemplate = new RestTemplate();

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Ca-Key", APP_ID);

        // 构建请求参数
        Map<String, String> body = new HashMap<>();
        body.put("realName", realName);
        body.put("idCard", idCard);
        body.put("bankCardNo", bankCardNo);
        body.put("mobilePhone", mobilePhone);

        // 对请求参数进行签名
        String sign = DigestUtils.md5Hex(APP_KEY + getSignContent(body) + APP_KEY).toUpperCase();
        headers.set("X-Ca-Signature", sign);

        // 发送POST请求
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(AUTH_URL, HttpMethod.POST, entity, String.class);

        // 处理认证结果
        if (response.getStatusCode().is2xxSuccessful()) {
            String result = response.getBody();
            // 根据实际接口返回的格式解析认证结果
            // 这里只是简单示例，认为返回了{"code":0,"message":"成功"}
            return result.contains("\"code\":0");
        } else {
            throw new RuntimeException("银行卡实名认证失败，HTTP响应码：" + response.getStatusCodeValue());
        }
    }

    /**
     * 对请求参数进行签名
     *
     * @param params 请求参数
     * @return 签名结果
     */
    private static String getSignContent(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append(entry.getValue());
        }
        return Base64.encodeBase64String(sb.toString().getBytes(StandardCharsets.UTF_8));
    }
}


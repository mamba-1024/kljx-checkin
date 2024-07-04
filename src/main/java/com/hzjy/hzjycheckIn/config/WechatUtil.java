package com.hzjy.hzjycheckIn.config;

import com.alibaba.fastjson.JSONObject;
import com.hzjy.hzjycheckIn.dto.WechatResultByCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信工具類
 */
@Component
public class WechatUtil {


    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.appSecret}")
    private String appSecret;

    @Autowired
    private RestTemplate restTemplate;

    public WechatResultByCode getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String response = restTemplate.getForObject(url + "?appid={appid}&secret={secret}&js_code={js_code}&grant_type={grant_type}", String.class, params);
        WechatResultByCode wechatResultByCode = JSONObject.parseObject(response, WechatResultByCode.class);
        return wechatResultByCode;
    }


}

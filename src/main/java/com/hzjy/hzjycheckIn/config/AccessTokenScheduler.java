package com.hzjy.hzjycheckIn.config;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class AccessTokenScheduler {

    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.appSecret}")
    private String appSecret;

    public static final Map<String, String> accessTokenMap = new HashMap<>();

    @Scheduled(fixedDelay = 7000000)
    @PostConstruct
    public void refreshAccessToken() {
        String accessToken = getAccessToken(appId, appSecret);
        accessTokenMap.put("accessToken",accessToken);
        System.out.println("refreshed accessToken: " + accessToken);
    }


    public String getAccessToken(String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String responseBody = responseEntity.getBody();
        return JSON.parseObject(responseBody).getString("access_token");
    }

}


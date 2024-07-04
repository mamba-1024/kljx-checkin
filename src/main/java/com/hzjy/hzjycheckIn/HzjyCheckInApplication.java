package com.hzjy.hzjycheckIn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication()
@MapperScan("com.hzjy.hzjycheckIn.mapper")
@EnableScheduling
public class HzjyCheckInApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzjyCheckInApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}

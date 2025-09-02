package com.hzjy.hzjycheckIn.config;

// 已弃用，改用本地文件存储
/*
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig{

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
*/

/**
 * OSS配置类已弃用
 * 项目已迁移到远程文件存储，不再使用阿里云OSS
 * 请使用 RemoteFileService 进行文件操作
 */
public class OssConfig {
    // 此类已弃用
}


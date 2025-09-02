package com.hzjy.hzjycheckIn.util;

import com.hzjy.hzjycheckIn.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 文件URL工具类
 * 用于统一处理文件URL的转换，支持多环境文件存储服务
 */
@Component
public class FileUrlUtil {

    @Autowired
    private FileService fileService;

    /**
     * 将相对路径转换为完整的访问URL
     * @param filePath 文件相对路径
     * @return 完整的访问URL
     */
    public String getFileUrl(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return "";
        }
        
        // 如果已经是完整URL，直接返回
        if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
            return filePath;
        }
        
        // 使用文件服务获取URL
        return fileService.getFileUrl(filePath);
    }

    /**
     * 批量转换文件URL
     * @param filePaths 文件路径数组
     * @return 转换后的URL数组
     */
    public String[] getFileUrls(String[] filePaths) {
        if (filePaths == null || filePaths.length == 0) {
            return new String[0];
        }
        
        String[] urls = new String[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            urls[i] = getFileUrl(filePaths[i]);
        }
        return urls;
    }

    /**
     * 检查是否为远程存储URL（用于兼容性处理）
     * @param url URL字符串
     * @return 是否为远程存储URL
     */
    public boolean isRemoteUrl(String url) {
        return StringUtils.hasText(url) && 
               (url.contains("oss-cn-hangzhou.aliyuncs.com") || 
                url.contains("aliyuncs.com") ||
                url.contains("cos.") ||
                url.contains("qiniu.com"));
    }

    /**
     * 从远程存储URL中提取相对路径
     * @param remoteUrl 远程存储完整URL
     * @return 相对路径
     */
    public String extractPathFromRemoteUrl(String remoteUrl) {
        if (!isRemoteUrl(remoteUrl)) {
            return remoteUrl;
        }
        
        // 从远程存储URL中提取文件路径
        String[] parts = remoteUrl.split("/");
        if (parts.length > 3) {
            StringBuilder path = new StringBuilder();
            for (int i = 3; i < parts.length; i++) {
                if (i > 3) {
                    path.append("/");
                }
                path.append(parts[i]);
            }
            return path.toString();
        }
        
        return remoteUrl;
    }
} 
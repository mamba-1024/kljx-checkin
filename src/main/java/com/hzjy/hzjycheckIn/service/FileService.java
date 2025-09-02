package com.hzjy.hzjycheckIn.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 统一文件存储服务接口
 * 支持多环境实现：本地存储（prod）、远程存储（dev）
 */
public interface FileService {
    
    /**
     * 上传文件到指定目录
     * @param file 上传的文件
     * @param directory 存储目录，如 "images", "auth" 等
     * @return 文件相对路径
     * @throws IOException 文件操作异常
     */
    String uploadFile(MultipartFile file, String directory) throws IOException;
    
    /**
     * 上传文件到默认目录
     * @param file 上传的文件
     * @return 文件相对路径
     * @throws IOException 文件操作异常
     */
    String uploadFile(MultipartFile file) throws IOException;
    
    /**
     * 删除文件
     * @param filePath 文件相对路径
     * @return 是否删除成功
     */
    boolean deleteFile(String filePath);
    
    /**
     * 获取文件的完整访问URL
     * @param filePath 文件相对路径
     * @return 完整的访问URL
     */
    String getFileUrl(String filePath);
    
    /**
     * 检查文件是否存在
     * @param filePath 文件相对路径
     * @return 文件是否存在
     */
    boolean fileExists(String filePath);
} 
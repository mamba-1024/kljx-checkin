package com.hzjy.hzjycheckIn.service.impl;

import com.hzjy.hzjycheckIn.service.FileService;
import com.hzjy.hzjycheckIn.service.LocalFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 本地文件存储服务实现类
 * 在生产环境（prod）中使用，将文件存储到ECS服务器本地
 */
@Service
@Profile("prod")  // 只在生产环境激活
@Slf4j
public class LocalFileServiceImpl implements FileService, LocalFileService {

    @Value("${local.file.upload.path:/var/hzjy_upload}")
    private String uploadPath;

    @Value("${local.file.access.url:http://localhost:8088/files}")
    private String accessUrl;

    @Value("${server.port:8088}")
    private String serverPort;

    @PostConstruct
    public void init() {
        log.info("本地文件存储服务启动 [PROD环境] - 上传路径: {}, 访问URL: {}", uploadPath, accessUrl);
        
        // 确保上传目录存在
        try {
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                log.info("创建上传目录: {}", uploadPath);
            }

            // 创建子目录
            createSubDirectories();

        } catch (IOException e) {
            log.error("创建上传目录失败: {}", e.getMessage(), e);
        }
    }

    private void createSubDirectories() throws IOException {
        String[] subDirs = { "images", "auth", "products", "actions", "employees" };
        for (String subDir : subDirs) {
            Path subPath = Paths.get(uploadPath, subDir);
            if (!Files.exists(subPath)) {
                Files.createDirectories(subPath);
                log.info("创建子目录: {}", subPath);
            }
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String directory) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID().toString() + extension;

        // 构建存储路径
        Path targetDir = Paths.get(uploadPath, directory);

        if (!Files.exists(targetDir)) {

            Files.createDirectories(targetDir);
        }

        Path targetPath = targetDir.resolve(fileName);
        // 保存文件
        try {
            Files.copy(file.getInputStream(), targetPath);
            log.info("文件上传成功: {}", targetPath);

            // 返回相对路径
            return directory + "/" + fileName;
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        log.info("后天文件上传444444");
        return uploadFile(file, "images");
    }

    @Override
    public boolean deleteFile(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return false;
        }

        try {
            Path fullPath = Paths.get(uploadPath, filePath);
            if (Files.exists(fullPath)) {
                Files.delete(fullPath);
                log.info("文件删除成功: {}", fullPath);
                return true;
            }
        } catch (IOException e) {
            log.error("文件删除失败: {}", e.getMessage(), e);
        }
        return false;
    }

    @Override
    public String getFileUrl(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return "";
        }

        // 如果已经是完整URL，直接返回
        if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
            return filePath;
        }

        // 构建完整的访问URL
        return accessUrl + "/" + filePath;
    }

    @Override
    public boolean fileExists(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return false;
        }

        Path fullPath = Paths.get(uploadPath, filePath);
        return Files.exists(fullPath);
    }

    private String getFileExtension(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "";
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }
}
package com.hzjy.hzjycheckIn.controller.web;

import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传控制器
 * 支持多环境：开发环境使用SSH远程存储，生产环境使用本地存储
 */
@RestController
// @RequestMapping("/file")
@Api(tags = "文件上传")
@Slf4j
public class FileUploadController {

    @Autowired
    private FileService fileService;

    @ApiOperation("通用文件上传")
    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileService.uploadFile(file);
            String fileUrl = fileService.getFileUrl(filePath);
            log.info("文件上传成功: {}", fileUrl);
            return Result.success("文件上传成功", fileUrl);  // 返回完整URL而不是相对路径
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }

    @ApiOperation("微信认证文件上传")
    @PostMapping("/wechat/upload")
    public Result<String> uploadWechatFile(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileService.uploadFile(file, "auth");
            String fileUrl = fileService.getFileUrl(filePath);
            log.info("微信认证文件上传成功: {}", fileUrl);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("微信认证文件上传失败: {}", e.getMessage(), e);
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }

    @ApiOperation("后台文件上传")
    @PostMapping("/backend/upload")
    public Result<String> uploadBackendFile(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileService.uploadFile(file);
            String fileUrl = fileService.getFileUrl(filePath);
            log.info("后台文件上传成功: {}", fileUrl);
            return Result.success(fileUrl);  // 返回完整URL
        } catch (IOException e) {
            log.error("后台文件上传失败: {}", e.getMessage(), e);
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }

    @ApiOperation("产品图片上传")
    @PostMapping("/product/upload")
    public Result<String> uploadProductFile(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileService.uploadFile(file, "products");
            String fileUrl = fileService.getFileUrl(filePath);
            log.info("产品图片上传成功: {}", fileUrl);
            return Result.success(fileUrl);  // 返回完整URL
        } catch (IOException e) {
            log.error("产品图片上传失败: {}", e.getMessage(), e);
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }

    @ApiOperation("企业动态图片上传")
    @PostMapping("/action/upload")
    public Result<String> uploadActionFile(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileService.uploadFile(file, "actions");
            String fileUrl = fileService.getFileUrl(filePath);
            log.info("企业动态图片上传成功: {}", fileUrl);
            return Result.success(fileUrl);  // 返回完整URL
        } catch (IOException e) {
            log.error("企业动态图片上传失败: {}", e.getMessage(), e);
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }

    @ApiOperation("员工证件照上传")
    @PostMapping("/employee/upload")
    public Result<String> uploadEmployeeFile(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileService.uploadFile(file, "employees");
            String fileUrl = fileService.getFileUrl(filePath);
            log.info("员工证件照上传成功: {}", fileUrl);
            return Result.success(fileUrl);  // 返回完整URL
        } catch (IOException e) {
            log.error("员工证件照上传失败: {}", e.getMessage(), e);
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/delete")
    public Result<Boolean> deleteFile(@RequestParam("filePath") String filePath) {
        boolean success = fileService.deleteFile(filePath);
        if (success) {
            log.info("文件删除成功: {}", filePath);
            return Result.success(true);
        } else {
            log.error("文件删除失败: {}", filePath);
            return Result.fail("文件删除失败");
        }
    }

    @ApiOperation("获取文件URL")
    @GetMapping("/url")
    public Result<String> getFileUrl(@RequestParam("filePath") String filePath) {
        String fileUrl = fileService.getFileUrl(filePath);
        return Result.success(fileUrl);
    }

    /**
     * 连接测试接口（调试用）
     * 注意：此接口仅在dev环境下有效（RemoteFileService）
     */
    @GetMapping("/test-connection")
    public Result<String> testConnection() {
        try {
            // 简单的文件存在性测试
            boolean canConnect = fileService.fileExists("test");
            return Result.success("文件服务连接测试完成，可用性: " + canConnect);
        } catch (Exception e) {
            log.error("文件服务连接测试失败", e);
            return Result.fail("文件服务连接测试失败: " + e.getMessage());
        }
    }


} 
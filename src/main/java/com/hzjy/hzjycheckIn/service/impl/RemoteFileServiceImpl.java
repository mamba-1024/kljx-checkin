package com.hzjy.hzjycheckIn.service.impl;

import com.hzjy.hzjycheckIn.service.FileService;
import com.hzjy.hzjycheckIn.service.RemoteFileService;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 远程文件存储服务实现类
 * 在开发环境（dev）中使用，通过SSH/SCP协议将文件上传到远程服务器
 */
@Service
@Profile("dev")  // 只在开发环境激活
@Slf4j
public class RemoteFileServiceImpl implements FileService, RemoteFileService {

    @Value("${remote.file.ssh.host}")
    private String sshHost;

    @Value("${remote.file.ssh.port:22}")
    private int sshPort;

    @Value("${remote.file.ssh.username}")
    private String sshUsername;

    @Value("${remote.file.ssh.password:}")
    private String sshPassword;

    @Value("${remote.file.ssh.privateKeyPath:}")
    private String privateKeyPath;

    @Value("${remote.file.upload.path}")
    private String uploadPath;

    @Value("${remote.file.access.url}")
    private String accessUrl;

    @PostConstruct
    public void init() {
        log.info("远程文件存储服务启动 [DEV环境] - SSH主机: {}, 端口: {}, 用户: {}", sshHost, sshPort, sshUsername);
        log.info("远程文件访问URL: {}", accessUrl);
        // 使用JSch兼容的新密钥测试连接
        testConnection();
    }

    private void testConnection() {
        JSch jsch = new JSch();
        Session session = null;
        try {
            log.info("开始创建SSH会话...");
            session = createSession(jsch);
            
            log.info("开始连接SSH服务器: {}@{}:{}", sshUsername, sshHost, sshPort);
            session.connect();
            log.info("SSH连接测试成功");
            
            // 测试目录是否存在，如果不存在则创建
            createRemoteDirectories(session);
            
        } catch (JSchException e) {
            log.error("JSch SSH连接失败 - 错误类型: {}, 错误信息: {}", e.getClass().getSimpleName(), e.getMessage());
            log.error("详细错误信息: ", e);
            
            // 尝试输出更多调试信息
            if (e.getMessage().contains("USERAUTH fail")) {
                log.error("认证失败！可能的原因:");
                log.error("1. SSH服务器不接受此公钥");
                log.error("2. 私钥文件权限问题");
                log.error("3. 服务器SSH配置限制");
                log.error("4. JSch版本兼容性问题");
            }
        } catch (Exception e) {
            log.error("SSH连接测试失败: {}", e.getMessage(), e);
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    private Session createSession(JSch jsch) throws JSchException {
        Session session = jsch.getSession(sshUsername, sshHost, sshPort);

        if (StringUtils.hasText(privateKeyPath)) {
            // 处理波浪号路径
            String resolvedPath = privateKeyPath;
            if (privateKeyPath.startsWith("~/")) {
                resolvedPath = System.getProperty("user.home") + privateKeyPath.substring(1);
            }
            log.info("使用SSH私钥路径: {}", resolvedPath);
            
            // 验证私钥文件是否存在
            java.io.File keyFile = new java.io.File(resolvedPath);
            if (!keyFile.exists()) {
                throw new JSchException("SSH私钥文件不存在: " + resolvedPath);
            }
            if (!keyFile.canRead()) {
                throw new JSchException("SSH私钥文件无法读取，请检查权限: " + resolvedPath);
            }
            
            jsch.addIdentity(resolvedPath);
            log.info("SSH私钥加载成功，文件大小: {} 字节", keyFile.length());
        } else if (StringUtils.hasText(sshPassword)) {
            session.setPassword(sshPassword);
            log.info("使用密码认证模式");
        } else {
            throw new JSchException("SSH密码或私钥路径未配置");
        }

        // 关闭主机密钥检查
        session.setConfig("StrictHostKeyChecking", "no");
        
        // 设置认证方法优先级，优先使用公钥认证
        session.setConfig("PreferredAuthentications", "publickey,password");
        
        // 扩展算法支持以兼容更多SSH服务器
        session.setConfig("PubkeyAcceptedAlgorithms", "ssh-rsa,rsa-sha2-256,rsa-sha2-512,ssh-ed25519,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521");
        session.setConfig("server_host_key", "ssh-rsa,ssh-ed25519,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521");
        session.setConfig("kex", "diffie-hellman-group16-sha512,diffie-hellman-group14-sha256,diffie-hellman-group14-sha1,diffie-hellman-group1-sha1,ecdh-sha2-nistp256,ecdh-sha2-nistp384,ecdh-sha2-nistp521");
        session.setConfig("cipher.s2c", "aes128-ctr,aes192-ctr,aes256-ctr,aes128-cbc,aes192-cbc,aes256-cbc,3des-cbc");
        session.setConfig("cipher.c2s", "aes128-ctr,aes192-ctr,aes256-ctr,aes128-cbc,aes192-cbc,aes256-cbc,3des-cbc");
        session.setConfig("mac.s2c", "hmac-sha2-256,hmac-sha2-512,hmac-sha1");
        session.setConfig("mac.c2s", "hmac-sha2-256,hmac-sha2-512,hmac-sha1");
        
        // 设置连接和认证超时
        session.setTimeout(30000); // 30秒超时
        session.setServerAliveInterval(30000); // 30秒保活
        session.setServerAliveCountMax(3); // 最多3次保活失败
        
        log.info("SSH会话配置完成，开始尝试连接...");
        return session;
    }

    private void createRemoteDirectories(Session session) throws JSchException, IOException {
        String[] subDirs = {"images", "auth", "products", "actions", "employees"};
        
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        try {
            // 创建主目录和子目录
            String command = String.format("mkdir -p %s", uploadPath);
            for (String subDir : subDirs) {
                command += String.format(" %s/%s", uploadPath, subDir);
            }
            
            channel.setCommand(command);
            channel.connect();
            
            // 等待命令执行完成
            while (!channel.isClosed()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            log.info("远程目录创建完成");
        } finally {
            if (channel.isConnected()) {
                channel.disconnect();
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

        // 构建远程文件路径
        String remotePath = uploadPath + "/" + directory + "/" + fileName;

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp sftpChannel = null;

        try {
            session = createSession(jsch);
            session.connect();

            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            // 确保远程目录存在
            String remoteDir = uploadPath + "/" + directory;
            try {
                sftpChannel.stat(remoteDir);
            } catch (SftpException e) {
                // 目录不存在，创建目录
                sftpChannel.mkdir(remoteDir);
                log.info("创建远程目录: {}", remoteDir);
            }

            // 上传文件
            InputStream inputStream = file.getInputStream();
            sftpChannel.put(inputStream, remotePath);
            inputStream.close();

            log.info("文件上传成功: {}", remotePath);

            // 返回相对路径
            return directory + "/" + fileName;

        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new IOException("文件上传失败: " + e.getMessage(), e);
        } finally {
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        return uploadFile(file, "images");
    }

    @Override
    public boolean deleteFile(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return false;
        }

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp sftpChannel = null;

        try {
            session = createSession(jsch);
            session.connect();

            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            String remotePath = uploadPath + "/" + filePath;
            sftpChannel.rm(remotePath);

            log.info("文件删除成功: {}", remotePath);
            return true;

        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage(), e);
            return false;
        } finally {
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
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

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp sftpChannel = null;

        try {
            session = createSession(jsch);
            session.connect();

            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            String remotePath = uploadPath + "/" + filePath;
            sftpChannel.stat(remotePath);
            return true;

        } catch (Exception e) {
            return false;
        } finally {
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
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
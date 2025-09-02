package com.hzjy.hzjycheckIn.util;

import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 密码工具类
 * 提供密码加密、验证等功能
 */
@Slf4j
public class PasswordUtil {

    /**
     * 加密密码
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        if (password == null || password.isEmpty()) {
            return null;
        }
        
        // 生成随机盐值
        String salt = RandomUtil.randomString(8);
        
        // 使用BCrypt加密密码
        String hashedPassword = BCrypt.hashpw(password + salt, BCrypt.gensalt());
        
        log.debug("密码加密完成，盐值长度: {}", salt.length());
        return hashedPassword;
    }

    /**
     * 加密密码（指定盐值）
     * @param password 原始密码
     * @param salt 盐值
     * @return 加密后的密码
     */
    public static String encryptPassword(String password, String salt) {
        if (password == null || password.isEmpty()) {
            return null;
        }
        
        if (salt == null || salt.isEmpty()) {
            salt = RandomUtil.randomString(8);
        }
        
        // 使用BCrypt加密密码
        String hashedPassword = BCrypt.hashpw(password + salt, BCrypt.gensalt());
        
        log.debug("密码加密完成，使用指定盐值，盐值长度: {}", salt.length());
        return hashedPassword;
    }

    /**
     * 验证密码
     * @param inputPassword 输入的密码
     * @param storedPassword 存储的密码
     * @param salt 密码盐
     * @return 是否验证通过
     */
    public static boolean validatePassword(String inputPassword, String storedPassword, String salt) {
        if (storedPassword == null || inputPassword == null) {
            return false;
        }

        // 如果没有盐值，直接比较密码（兼容旧数据）
        if (salt == null || salt.isEmpty()) {
            return inputPassword.equals(storedPassword);
        }

        // 使用BCrypt验证密码
        try {
            return BCrypt.checkpw(inputPassword + salt, storedPassword);
        } catch (Exception e) {
            log.error("密码验证异常", e);
            return false;
        }
    }

    /**
     * 生成随机盐值
     * @return 随机盐值
     */
    public static String generateSalt() {
        return RandomUtil.randomString(8);
    }

    /**
     * 检查密码强度
     * @param password 密码
     * @return 密码强度等级：0-弱，1-中，2-强
     */
    public static int checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        int score = 0;
        
        // 长度检查
        if (password.length() >= 8) {
            score++;
        }
        
        // 包含数字
        if (password.matches(".*\\d.*")) {
            score++;
        }
        
        // 包含小写字母
        if (password.matches(".*[a-z].*")) {
            score++;
        }
        
        // 包含大写字母
        if (password.matches(".*[A-Z].*")) {
            score++;
        }
        
        // 包含特殊字符
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            score++;
        }

        return Math.min(score, 2);
    }
} 
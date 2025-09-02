package com.hzjy.hzjycheckIn.util;

import cn.hutool.core.util.RandomUtil;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtUtils {
    private static final String SECRET_KEY = "p46opqc2qma96l7cis9s8f8zt75ifhod807v91i66x070ksw2vrcoojvpd4j1b19"; // 用于签名的密钥
    private static final long EXPIRATION_TIME = 864_000_000L; // 有效期为 10 天
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    public static String generateToken(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder().setSubject(subject).setIssuedAt(now).setExpiration(expiration).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public static String validateTokenAndGetSubject(String token) {
        try {
            // 清理token，移除可能的空格和换行符
            token = token.trim();
            log.info("开始验证JWT token: {}", token);
            String subject = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
            log.info("JWT验证成功，subject: {}", subject);
            return subject;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT验证失败: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public static void main(String[] args) {
        // 测试使用员工ID生成和验证token
        String employeeId = "123";
        String token = generateToken(employeeId);
        System.out.println("生成的token: " + token);
        
        try {
            String validatedEmployeeId = validateTokenAndGetSubject(token);
            System.out.println("验证成功，员工ID: " + validatedEmployeeId);
        } catch (Exception e) {
            System.out.println("验证失败: " + e.getMessage());
        }
    }
}


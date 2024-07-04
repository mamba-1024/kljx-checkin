package com.hzjy.hzjycheckIn.util;

import cn.hutool.core.util.RandomUtil;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {
    private static final String SECRET_KEY = "p46opqc2qma96l7cis9s8f8zt75ifhod807v91i66x070ksw2vrcoojvpd4j1b19"; // 用于签名的密钥
    private static final long EXPIRATION_TIME = 864_000_000L; // 有效期为 10 天

    public static String generateToken(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder().setSubject(subject).setIssuedAt(now).setExpiration(expiration).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public static String validateTokenAndGetSubject(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public static void main(String[] args) {
        System.out.printf(generateToken(RandomUtil.randomString(12)));
    }
}


//package com.hzjy.hzjycheckIn.interceptor;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.crypto.SecretKey;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtInterceptor implements HandlerInterceptor {
//
//    // 这里的密钥可以通过随机生成，也可以写死在代码里
//    private static final String SECRET_KEY = "secret_key";
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 获取请求头中的Token
//        String token = request.getHeader("Authorization");
//
//        if (token == null || !token.startsWith("Bearer ")) {
//            // 如果请求头中没有Token或Token格式不正确，返回401 Unauthorized
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            return false;
//        }
//
//        try {
//            // 解析Token，获取Payload中的信息
//            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token.replace("Bearer ", ""))
//                    .getBody();
//
//            // 将Payload中的信息放到请求的Attribute中，方便后续操作
//            request.setAttribute("userId", claims.get("userId"));
//            request.setAttribute("username", claims.get("username"));
//            request.setAttribute("role", claims.get("role"));
//
//            return true;
//        } catch (Exception e) {
//            // 如果解析Token出现异常，返回401 Unauthorized
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            return false;
//        }
//    }
//}
//

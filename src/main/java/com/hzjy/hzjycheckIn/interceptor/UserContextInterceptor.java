package com.hzjy.hzjycheckIn.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.config.EmployeeContext;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import com.hzjy.hzjycheckIn.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class UserContextInterceptor implements HandlerInterceptor {

    @Autowired
    private EmployeeService employeeService;

//    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("UserContextInterceptor拦截请求: {}", requestURI);
        
        String token = request.getHeader("Authorization");
        log.info("Authorization header: {}", token);
        
        if (token != null && !token.trim().isEmpty()) {
            // 检查token是否为空
            if (token.trim().isEmpty()) {
                log.warn("Authorization头中的token为空");
                writeUnauthorizedResponse(response, "Token为空");
                return false;
            }
            
            try {
                // 验证JWT token并获取员工ID
                String employeeId = JwtUtils.validateTokenAndGetSubject(token);
                log.info("JWT验证成功，员工ID: {}", employeeId);
                
                // 从缓存中获取员工信息
                Employee user = employeeService.getEmployeeByToken(token);
                if (user != null) {
                    log.info("从缓存获取到员工信息: {}", user.getName());
                    //每次去数据库取最新的数据
                    user = employeeService.getById(user.getId());
                    EmployeeContext.setEmployee(user);
                    return true;
                } else {
                    log.warn("缓存中未找到员工信息，token: {}", token);
                    writeUnauthorizedResponse(response, "Token无效或已过期");
                    return false;
                }
            } catch (Exception e) {
                log.error("JWT验证失败: {}", e.getMessage(), e);
                writeUnauthorizedResponse(response, "Token验证失败");
                return false;
            }
        } else {
            log.warn("请求头中没有Authorization token或token为空");
            writeUnauthorizedResponse(response, "缺少JWT token");
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        EmployeeContext.removeUser();
    }

    /**
     * 写入401未授权响应
     */
    private void writeUnauthorizedResponse(HttpServletResponse response, String message) {
        try {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            
            Result<String> result = Result.fail(401, message);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(result);
            
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            log.error("写入401响应失败", e);
        }
    }
}

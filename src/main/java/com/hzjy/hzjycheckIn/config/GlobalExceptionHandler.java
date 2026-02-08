package com.hzjy.hzjycheckIn.config;

import com.hzjy.hzjycheckIn.common.Result;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理JWT相关异常
     */
    @ExceptionHandler({JwtException.class, IllegalArgumentException.class})
    @ResponseBody
    public ResponseEntity<Result<String>> handleJwtException(Exception e, HttpServletRequest request) {
        log.error("JWT验证异常: {} - {}", request.getRequestURI(), e.getMessage());
        
        Result<String> result = Result.fail(401, "Token验证失败，请重新登录");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<Result<String>> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常: {} - {}", request.getRequestURI(), e.getMessage(), e);
        
        // 如果是JWT相关的运行时异常，返回401
        if (e.getMessage() != null && e.getMessage().contains("Invalid JWT token")) {
            Result<String> result = Result.fail(401, "Token验证失败，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        
        Result<String> result = Result.fail("服务器内部错误");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Result<String>> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: {} - {}", request.getRequestURI(), e.getMessage(), e);
        
        Result<String> result = Result.fail("系统异常，请稍后重试");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
} 
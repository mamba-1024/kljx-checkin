package com.hzjy.hzjycheckIn.config;

import com.hzjy.hzjycheckIn.interceptor.BackendContextInterceptor;
import com.hzjy.hzjycheckIn.interceptor.UserContextInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {


    @Autowired
    private BackendContextInterceptor backendContextInterceptor;

    @Autowired
    private UserContextInterceptor userContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludeBackendSwaggerPath = Arrays.asList("/swagger-ui/**", "/v2/**", "/webjars/**", "/swagger-resources/**", "/swagger-ui.html", "/wechat/**", "/backend/**", "/index/**");
        List<String> excludeWechatSwaggerPath = Arrays.asList("/swagger-ui/**", "/v2/**", "/webjars/**", "/swagger-resources/**", "/swagger-ui.html", "/wechat/**", "/attendance/**", "/employee/**", "/employeeDetails/**", "/index/**", "/workShift/**", "/backend/login/login", "/backend/attendance/export/daily", "backend/attendance/export/month", "/backend/employee/export");
        // 添加JWT拦截器，拦截所有请求
        registry.addInterceptor(backendContextInterceptor).addPathPatterns("/**").excludePathPatterns(excludeWechatSwaggerPath);
        registry.addInterceptor(userContextInterceptor).addPathPatterns("/**").excludePathPatterns(excludeBackendSwaggerPath);
    }

    @Bean
    public OncePerRequestFilter contentCachingRequestFilter() {
        OncePerRequestFilter oncePerRequestFilter = new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
                ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
                ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
                filterChain.doFilter(wrappedRequest, wrappedResponse);
                String requestURI = wrappedRequest.getRequestURI();
                log.info("request path is[{}]", requestURI);
                if (requestURI.contains("upload") || requestURI.contains("export")) {
                    log.info("http response status is {}", wrappedResponse.getStatus());
                } else {
                    log.info("http ip is {}, request:{}", wrappedRequest.getRemoteAddr(), new String(wrappedRequest.getContentAsByteArray()));
                    log.info("http response status is {} content is {}", wrappedResponse.getStatus(), new String(wrappedResponse.getContentAsByteArray()));

                }
                wrappedResponse.copyBodyToResponse();
            }

            @Override
            protected boolean shouldNotFilter(HttpServletRequest httpServletRequest) {
                List<String> excludeSwaggerPath = Arrays.asList("/swagger-ui", "/v2", "/webjars", "/swagger-resources", "/swagger-ui.html");
                String requestURI = httpServletRequest.getRequestURI();
                for (String excludePath : excludeSwaggerPath) {
                    if (requestURI.startsWith(excludePath)) {
                        return true;
                    }
                }
                return false;
            }
        };
        return oncePerRequestFilter;
    }
}
package com.hzjy.hzjycheckIn.config;

import com.hzjy.hzjycheckIn.interceptor.BackendContextInterceptor;
import com.hzjy.hzjycheckIn.interceptor.UserContextInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    @Value("${local.file.upload.path:/var/hzjy_upload}")
    private String uploadPath;

    @Autowired
    private BackendContextInterceptor backendContextInterceptor;

    @Autowired
    private UserContextInterceptor userContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludeBackendSwaggerPath = Arrays.asList("/swagger-ui/**", "/v2/**", "/webjars/**", "/swagger-resources/**", "/swagger-ui.html", "/wechat/**", "/backend/**", "/index/**", "/employee/login/login", "/employee/login/register");
        List<String> excludeWechatSwaggerPath = Arrays.asList("/swagger-ui/**", "/v2/**", "/webjars/**", "/swagger-resources/**", "/swagger-ui.html", "/wechat/**", "/attendance/**", "/employee/**", "/employeeDetails/**", "/index/**", "/workShift/**", "/backend/login/login", "/backend/attendance/export/daily", "backend/attendance/export/month", "/backend/employee/export", "/employee/login/login", "/employee/login/register");
        
        // 添加JWT拦截器，拦截所有请求
        registry.addInterceptor(backendContextInterceptor).addPathPatterns("/**").excludePathPatterns(excludeWechatSwaggerPath);
        registry.addInterceptor(userContextInterceptor).addPathPatterns("/**").excludePathPatterns(excludeBackendSwaggerPath);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射，用于访问上传的文件
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadPath + "/");

        log.info("配置静态资源映射: /files/** -> {}", uploadPath);
    }

    /**
     * 配置跨域资源共享（CORS）
     * 解决前端调用后端接口时的跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 允许所有路径的跨域访问
                .allowedOrigins("*")  // 允许所有域名跨域访问
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")  // 允许的HTTP方法
                .allowedHeaders("*")  // 允许所有请求头，包括Authorization、Content-Type等
                .allowCredentials(false)  // 使用通配符域名时必须设置为false
                .maxAge(3600);  // 预检请求的缓存时间（秒）
        
        log.info("已配置CORS跨域支持，允许所有域名访问");
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
                    log.info("http ip is {}, request:{}", wrappedRequest.getRemoteAddr(), new String(wrappedRequest.getContentAsByteArray(), "UTF-8"));
                    log.info("http response status is {} content is {}", wrappedResponse.getStatus(), new String(wrappedResponse.getContentAsByteArray(), "UTF-8"));

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
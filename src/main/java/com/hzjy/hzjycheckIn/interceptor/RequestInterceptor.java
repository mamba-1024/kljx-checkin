//package com.hzjy.hzjycheckIn.interceptor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Arrays;
//import java.util.Map;
//
//@Component
//public class RequestInterceptor implements HandlerInterceptor {
//
//    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        // 打印请求入参
//        Map<String, String[]> paramMap = request.getParameterMap();
//        if (paramMap != null && !paramMap.isEmpty()) {
//            logger.info("请求入参：");
//            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
//                logger.info("{}: {}", entry.getKey(), Arrays.toString(entry.getValue()));
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//                           ModelAndView modelAndView) {
//        // 打印响应出参
//        if (modelAndView != null && modelAndView.getModelMap() != null && !modelAndView.getModelMap().isEmpty()) {
//            logger.info("响应出参：");
//            for (Map.Entry<String, Object> entry : modelAndView.getModelMap().entrySet()) {
//                logger.info("{}: {}", entry.getKey(), entry.getValue());
//            }
//        }
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        // do nothing
//    }
//}

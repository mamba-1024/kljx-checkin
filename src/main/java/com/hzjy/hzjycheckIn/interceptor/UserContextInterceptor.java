package com.hzjy.hzjycheckIn.interceptor;

import com.hzjy.hzjycheckIn.config.EmployeeContext;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import com.hzjy.hzjycheckIn.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserContextInterceptor implements HandlerInterceptor {

    @Autowired
    private EmployeeService employeeService;

//    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token != null) {
            try {
                String andGetSubject = JwtUtils.validateTokenAndGetSubject(token);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            Employee user = employeeService.getEmployeeByToken(token);
            if (user != null) {
                //每次去数据库取最新的数据
                user = employeeService.getById(user.getId());
                EmployeeContext.setEmployee(user);
                return true;
            }
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        EmployeeContext.removeUser();
    }
}

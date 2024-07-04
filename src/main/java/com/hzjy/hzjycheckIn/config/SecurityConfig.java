package com.hzjy.hzjycheckIn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs")
//                .permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin() // 定义登录表单相关配置
//                .and()
//                .logout() // 定义注销相关配置
//                .and()
//                .csrf().disable(); // 禁用CSRF保护（仅供开发环境使用）
//    }
//}

@Configuration
@EnableWebSecurity
@Order(-2147483648)
public class SecurityConfig extends WebSecurityConfigurerAdapter  {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("hajy")
                .password("{noop}123456") // 使用 "{noop}" 前缀表示密码以明文形式存储
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/webjars/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic();
    }
}

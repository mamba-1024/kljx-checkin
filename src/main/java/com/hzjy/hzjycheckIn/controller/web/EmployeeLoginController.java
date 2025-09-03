package com.hzjy.hzjycheckIn.controller.web;

import cn.hutool.core.util.RandomUtil;
import com.hzjy.hzjycheckIn.util.PasswordUtil;
import com.hzjy.hzjycheckIn.util.JwtUtils;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.config.CacheMap;
import com.hzjy.hzjycheckIn.dto.EmployeeLoginDTO;
import com.hzjy.hzjycheckIn.dto.EmployeeLoginByUsernameDTO;
import com.hzjy.hzjycheckIn.dto.EmployeeLoginResponseDTO;
import com.hzjy.hzjycheckIn.dto.EmployeeRegisterDTO;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import com.hzjy.hzjycheckIn.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 员工登录控制器
 */
@RestController
@RequestMapping("/employee/login")
@Api(tags = "员工登录")
@Slf4j
@Validated
public class EmployeeLoginController {

    @Autowired
    private EmployeeService employeeService;

    @ApiOperation("员工用户名密码登录")
    @PostMapping("/loginByName")
    public Result<EmployeeLoginResponseDTO> loginByName(@Valid @RequestBody EmployeeLoginByUsernameDTO loginDTO) {
        try {
            log.info("员工登录请求: 用户名={}", loginDTO.getUserName());
            // 根据用户名查询员工
            Employee employee = employeeService.getEmployeeByUsername(loginDTO.getUserName());
            if (employee == null) {
                log.warn("员工登录失败: 用户名不存在, 用户名={}", loginDTO.getUserName());
                return Result.fail("用户名不存在，请检查后重试");
            }

            // 检查员工状态
            if (Boolean.FALSE.equals(employee.getStatus())) {
                log.warn("员工登录失败: 账号已停用, 员工ID={}", employee.getId());
                return Result.fail("您的账号已停用，请联系管理员");
            }

            if (Boolean.FALSE.equals(employee.getOnBoard())) {
                log.warn("员工登录失败: 已离职, 员工ID={}", employee.getId());
                return Result.fail("您已离职，无法登录系统");
            }

            // 验证密码
            if (!validatePassword(loginDTO.getPassword(), employee.getPassword(), employee.getSalt())) {
                log.warn("员工登录失败: 密码错误, 员工ID={}", employee.getId());
                return Result.fail("密码错误，请检查后重试");
            }

            // 生成JWT token
            String token = JwtUtils.generateToken(String.valueOf(employee.getId()));
            
            // 将员工信息存入缓存
            CacheMap.employeeMap.put(token, employee);

            // 构建响应数据
            EmployeeLoginResponseDTO response = new EmployeeLoginResponseDTO();
            response.setToken(token);
            response.setEmployeeId(employee.getId());
            response.setName(employee.getName());
            response.setNickname(employee.getNickname());
            response.setAvatar(employee.getAvatar());
            response.setPhone(maskPhone(employee.getPhone()));
            response.setLevel(employee.getLevel());
            response.setIsAuthenticated(employee.getIsAuthenticated());
            response.setAuditStatus(employee.getAuditStatus());
            response.setStatus(employee.getStatus());
            response.setOnBoard(employee.getOnBoard());

            log.info("员工登录成功: 员工ID={}, 姓名={}", employee.getId(), employee.getName());
            return Result.success(response);

        } catch (Exception e) {
            log.error("员工登录异常: 用户名={}, 错误信息={}", loginDTO.getUserName(), e.getMessage(), e);
            return Result.fail("登录失败，请稍后重试");
        }
    }

    @ApiOperation("员工手机号密码登录")
    @PostMapping("/login")
    public Result<EmployeeLoginResponseDTO> login(@Valid @RequestBody EmployeeLoginDTO loginDTO) {
        try {
            log.info("员工登录请求: 手机号={}", loginDTO.getPhone());

            // 根据手机号查询员工
            Employee employee = employeeService.getEmployeeByPhone(loginDTO.getPhone());
            if (employee == null) {
                log.warn("员工登录失败: 手机号不存在, 手机号={}", loginDTO.getPhone());
                return Result.fail("手机号不存在，请检查后重试");
            }

            // 检查员工状态
            if (Boolean.FALSE.equals(employee.getStatus())) {
                log.warn("员工登录失败: 账号已停用, 员工ID={}", employee.getId());
                return Result.fail("您的账号已停用，请联系管理员");
            }

            if (Boolean.FALSE.equals(employee.getOnBoard())) {
                log.warn("员工登录失败: 已离职, 员工ID={}", employee.getId());
                return Result.fail("您已离职，无法登录系统");
            }

            // 验证密码
            if (!validatePassword(loginDTO.getPassword(), employee.getPassword(), employee.getSalt())) {
                log.warn("员工登录失败: 密码错误, 员工ID={}", employee.getId());
                return Result.fail("密码错误，请检查后重试");
            }

            // 生成JWT token
            String token = JwtUtils.generateToken(String.valueOf(employee.getId()));
            
            // 将员工信息存入缓存
            CacheMap.employeeMap.put(token, employee);

            // 构建响应数据
            EmployeeLoginResponseDTO response = new EmployeeLoginResponseDTO();
            response.setToken(token);
            response.setEmployeeId(employee.getId());
            response.setName(employee.getName());
            response.setNickname(employee.getNickname());
            response.setAvatar(employee.getAvatar());
            response.setPhone(maskPhone(employee.getPhone()));
            response.setLevel(employee.getLevel());
            response.setIsAuthenticated(employee.getIsAuthenticated());
            response.setAuditStatus(employee.getAuditStatus());
            response.setStatus(employee.getStatus());
            response.setOnBoard(employee.getOnBoard());

            log.info("员工登录成功: 员工ID={}, 姓名={}", employee.getId(), employee.getName());
            return Result.success(response);

        } catch (Exception e) {
            log.error("员工登录异常: 手机号={}, 错误信息={}", loginDTO.getPhone(), e.getMessage(), e);
            return Result.fail("登录失败，请稍后重试");
        }
    }

    /**
     * 验证密码
     * @param inputPassword 输入的密码
     * @param storedPassword 存储的密码
     * @param salt 密码盐
     * @return 是否验证通过
     */
    private boolean validatePassword(String inputPassword, String storedPassword, String salt) {
        return PasswordUtil.validatePassword(inputPassword, storedPassword, salt);
    }

    /**
     * 脱敏手机号
     * @param phone 原始手机号
     * @return 脱敏后的手机号
     */
    private String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    @ApiOperation("员工注册（设置密码）")
    @PostMapping("/register")
    public Result<Boolean> register(@Valid @RequestBody EmployeeRegisterDTO registerDTO) {
        try {
            log.info("员工注册请求: 手机号={}", registerDTO.getPhone());

            // 验证密码一致性
            if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
                return Result.fail("两次输入的密码不一致");
            }

            // 检查密码强度
            int passwordStrength = PasswordUtil.checkPasswordStrength(registerDTO.getPassword());
            if (passwordStrength == 0) {
                return Result.fail("密码强度太弱，请设置更复杂的密码");
            }

            // 根据手机号查询员工
            Employee employee = employeeService.getEmployeeByPhone(registerDTO.getPhone());
            if (employee == null) {
                log.warn("员工注册失败: 手机号不存在, 手机号={}", registerDTO.getPhone());
                return Result.fail("手机号不存在，请联系管理员添加员工信息");
            }

            // 检查是否已经设置过密码
            if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
                return Result.fail("该员工已设置过密码，请直接登录");
            }

            // 生成盐值和加密密码
            String salt = PasswordUtil.generateSalt();
            String encryptedPassword = PasswordUtil.encryptPassword(registerDTO.getPassword(), salt);

            // 更新员工密码信息
            Employee updateEmployee = new Employee();
            updateEmployee.setId(employee.getId());
            updateEmployee.setPassword(encryptedPassword);
            updateEmployee.setSalt(salt);

            boolean success = employeeService.updateById(updateEmployee);
            if (success) {
                log.info("员工注册成功: 员工ID={}, 姓名={}", employee.getId(), employee.getName());
                return Result.success(true);
            } else {
                log.error("员工注册失败: 更新数据库失败, 员工ID={}", employee.getId());
                return Result.fail("注册失败，请稍后重试");
            }

        } catch (Exception e) {
            log.error("员工注册异常: 手机号={}, 错误信息={}", registerDTO.getPhone(), e.getMessage(), e);
            return Result.fail("注册失败，请稍后重试");
        }
    }
} 
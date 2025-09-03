package com.hzjy.hzjycheckIn.controller.web;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.config.CacheMap;
import com.hzjy.hzjycheckIn.config.WechatUtil;
import com.hzjy.hzjycheckIn.dto.EmployeeRegisterDTO;
import com.hzjy.hzjycheckIn.dto.EmployeeRegisterByUsernameDTO;
import com.hzjy.hzjycheckIn.dto.WechatLoginDTO;
import com.hzjy.hzjycheckIn.dto.WechatResultByCode;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import com.hzjy.hzjycheckIn.util.JwtUtils;
import com.hzjy.hzjycheckIn.util.PasswordUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/wechat")
public class WeChatController {

    private static final Logger log = LoggerFactory.getLogger(WeChatController.class);

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WechatUtil wechatUtil;


    @ApiOperation("微信一键登录")
    @PostMapping("/login")
    public Result<String> weLogin(@RequestBody Map<String, String> params) {
        String code = params.get("code");
        // 通过code换取openid
        WechatResultByCode wechatResultByCode = wechatUtil.getOpenId(code);
        // 根据openid查询用户是否存在
        Employee employee = employeeService.getEmployeeByOpenId(wechatResultByCode.getOpenid());
        String token = RandomUtil.randomString(12);
        if (employee != null) {
            CacheMap.employeeMap.put(token, employee);
            // 如果用户已存在，则返回用户信息
            return new Result(0, "登录成功", token);
        }
        // 如果用户不存在，则新增用户,根据openId去获取用户信息
        Result phoneIsNull = Result.fail(4001, "手机号码为空");
        phoneIsNull.setData(token);
        CacheMap.wechatResultMap.put(token, wechatResultByCode);
        return phoneIsNull;
    }


    @ApiOperation("获取手机号码")
    @PostMapping("/getPhoneNum")
    public Result<String> getPhoneNum(@RequestBody WechatLoginDTO wechatLoginDTO) {

        // 获取session key
        WechatResultByCode wechatResultByCode = wechatUtil.getOpenId(wechatLoginDTO.getCode());
        // 判断是否有值
        if (Objects.isNull(wechatResultByCode)) {
            Result.fail(400, "微信登陆失败");
        }

        // 解密数据
        String phone222 = decryptPhoneNumber(wechatLoginDTO.getEncryptedData(), wechatResultByCode.getSessionKey(), wechatLoginDTO.getIv());
        String phone = decrypt(wechatLoginDTO.getEncryptedData(), wechatResultByCode.getSessionKey(), wechatLoginDTO.getIv());

        Employee employeeByPhone = employeeService.getEmployeeByPhone(phone);
        String token = JwtUtils.generateToken(RandomUtil.randomString(12));
        if (Objects.isNull(employeeByPhone)) {
            employeeByPhone = new Employee();
            employeeByPhone.setName("员工" + phone.substring(7)); // 使用手机号后4位作为默认姓名
            employeeByPhone.setOpenId(wechatResultByCode.getOpenid());
            employeeByPhone.setPhone(phone);
            employeeByPhone.setIsAuthenticated(Boolean.FALSE);
            employeeByPhone.setPoints(0);
            employeeByPhone.setCurrentMonthTime(BigDecimal.ZERO);
            employeeByPhone.setLeftWorkTime(BigDecimal.ZERO);
            employeeByPhone.setLevel(0);

            boolean save = employeeService.save(employeeByPhone);
            employeeByPhone.setId(employeeByPhone.getId());
        }
        CacheMap.employeeMap.put(token, employeeByPhone);
        return Result.success(token);
    }


    public  String decryptPhoneNumber(String encryptedData, String sessionKey, String iv) {
        try {
            byte[] encryptedDataByte = Base64.decodeBase64(encryptedData);
            byte[] sessionKeyByte = Base64.decodeBase64(sessionKey);
            byte[] ivByte = Base64.decodeBase64(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(sessionKeyByte, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedDataByte = cipher.doFinal(encryptedDataByte);

            return new String(decryptedDataByte, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 解密数据
    private String decrypt(String encryptedData, String sessionkey, String iv) {
        byte[] encrypData = Base64Utils.decodeFromString(encryptedData);
        byte[] ivData = Base64Utils.decodeFromString(iv);
        byte[] sessionKey = Base64Utils.decodeFromString(sessionkey);
        String resultString = null;
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivData);
        SecretKeySpec keySpec = new SecretKeySpec(sessionKey, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            resultString = new String(cipher.doFinal(encrypData), "UTF-8");
        } catch (Exception e) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
                resultString = new String(cipher.doFinal(encrypData), "UTF-8");
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
        JSONObject object = JSONObject.parseObject(resultString);
        // 拿到手机号码
        String phone = object.getString("phoneNumber");
        return phone;
    }

    @ApiOperation("员工注册（通过手机号和密码）")
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody EmployeeRegisterDTO registerDTO) {
        try {
            log.info("员工注册请求: 手机号={}", registerDTO.getPhone());

            // 验证密码一致性
            if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
                return Result.fail("两次输入的密码不一致");
            }

            // 检查密码强度
//            int passwordStrength = PasswordUtil.checkPasswordStrength(registerDTO.getPassword());
//            if (passwordStrength == 0) {
//                return Result.fail("密码强度太弱，请设置更复杂的密码");
//            }

            // 根据手机号查询员工
            Employee employee = employeeService.getEmployeeByPhone(registerDTO.getPhone());
            if (employee == null) {
                // 如果员工不存在，创建新员工
                employee = new Employee();
                employee.setName("员工" + registerDTO.getPhone().substring(7)); // 使用手机号后4位作为默认姓名
                employee.setPhone(registerDTO.getPhone());
                employee.setIsAuthenticated(Boolean.FALSE);
                employee.setPoints(0);
                employee.setCurrentMonthTime(BigDecimal.ZERO);
                employee.setLeftWorkTime(BigDecimal.ZERO);
                employee.setLevel(0);
                employee.setStatus(Boolean.TRUE);
                employee.setOnBoard(Boolean.TRUE);
                employee.setAuditStatus("WAIT_AUDIT");
                
                // 生成盐值和加密密码
                String salt = PasswordUtil.generateSalt();
                String encryptedPassword = PasswordUtil.encryptPassword(registerDTO.getPassword(), salt);
                employee.setPassword(encryptedPassword);
                employee.setSalt(salt);

                // 保存新员工
                boolean saveSuccess = employeeService.save(employee);
                if (!saveSuccess) {
                    log.error("员工注册失败: 保存新员工失败, 手机号={}", registerDTO.getPhone());
                    return Result.fail("注册失败，请稍后重试");
                }
                
                log.info("员工注册成功: 创建新员工, 员工ID={}, 手机号={}", employee.getId(), registerDTO.getPhone());
            } else {
                // 如果员工已存在，检查是否已经设置过密码
                if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
                    return Result.fail("该手机号已注册，请直接登录");
                }

                // 生成盐值和加密密码
                String salt = PasswordUtil.generateSalt();
                String encryptedPassword = PasswordUtil.encryptPassword(registerDTO.getPassword(), salt);

                // 更新员工密码信息
                Employee updateEmployee = new Employee();
                updateEmployee.setId(employee.getId());
                updateEmployee.setPassword(encryptedPassword);
                updateEmployee.setSalt(salt);

                boolean updateSuccess = employeeService.updateById(updateEmployee);
                if (!updateSuccess) {
                    log.error("员工注册失败: 更新密码失败, 员工ID={}", employee.getId());
                    return Result.fail("注册失败，请稍后重试");
                }
                
                log.info("员工注册成功: 更新密码, 员工ID={}, 手机号={}", employee.getId(), registerDTO.getPhone());
            }

            // 生成token并缓存员工信息
            String token = JwtUtils.generateToken(RandomUtil.randomString(12));
            CacheMap.employeeMap.put(token, employee);
            
            return Result.success(token);

        } catch (Exception e) {
            log.error("员工注册异常: 手机号={}, 错误信息={}", registerDTO.getPhone(), e.getMessage(), e);
            return Result.fail("注册失败，请稍后重试");
        }
    }

    @ApiOperation("员工注册（通过用户名和密码）")
    @PostMapping("/registerByName")
    public Result<String> registerByUsername(@Valid @RequestBody EmployeeRegisterByUsernameDTO registerDTO) {
        try {
            log.info("员工注册请求: 用户名={}", registerDTO.getUserName());
            // 验证密码一致性
            if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
                return Result.fail("两次输入的密码不一致");
            }
            
            // 根据用户名查询员工
            Employee employee = employeeService.getEmployeeByUsername(registerDTO.getUserName());
            if (employee == null) {
                // 用户名不存在，创建新员工
                employee = new Employee();
                // 设置用户名
                employee.setName(registerDTO.getUserName());
                // 设置手机号为空
                employee.setPhone(null);
                // 设置默认已经实名认证
                employee.setIsAuthenticated(Boolean.TRUE); // 默认已经实名认证
                // 设置积分
                employee.setPoints(0);
                // 设置当前月工时
                employee.setCurrentMonthTime(BigDecimal.ZERO);
                // 设置剩余工时
                employee.setLeftWorkTime(BigDecimal.ZERO);
                // 设置等级
                employee.setLevel(0);
                // 设置状态
                employee.setStatus(Boolean.TRUE);
                // 设置在职状态
                employee.setOnBoard(Boolean.TRUE);
                // 设置审核状态
                employee.setAuditStatus("WAIT_AUDIT");

                // 生成盐值和加密密码
                String salt = PasswordUtil.generateSalt();
                String encryptedPassword = PasswordUtil.encryptPassword(registerDTO.getPassword(), salt);
                employee.setPassword(encryptedPassword);
                employee.setSalt(salt);

                // 保存新员工
                boolean saveSuccess = employeeService.save(employee);
                if (!saveSuccess) {
                    log.error("员工注册失败: 保存新员工失败, 用户名={}", registerDTO.getUserName());
                    return Result.fail("注册失败，请稍后重试");
                }
                
                log.info("员工注册成功: 创建新员工, 员工ID={}, 用户名={}", employee.getId(), registerDTO.getUserName());
            } else {
                // 如果员工已存在，检查是否已经设置过密码
                if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
                    return Result.fail("该用户名已注册，请直接登录");
                }

                // 生成盐值和加密密码
                String salt = PasswordUtil.generateSalt();
                String encryptedPassword = PasswordUtil.encryptPassword(registerDTO.getPassword(), salt);

                // 更新员工密码信息
                Employee updateEmployee = new Employee();
                updateEmployee.setId(employee.getId());
                updateEmployee.setPassword(encryptedPassword);
                updateEmployee.setSalt(salt);

                boolean updateSuccess = employeeService.updateById(updateEmployee);
                if (!updateSuccess) {
                    log.error("员工注册失败: 更新密码失败, 员工ID={}", employee.getId());
                    return Result.fail("注册失败，请稍后重试");
                }
                
                log.info("员工注册成功: 更新密码, 员工ID={}, 用户名={}", employee.getId(), registerDTO.getUserName());
            }

            // 生成token并缓存员工信息
            String token = JwtUtils.generateToken(RandomUtil.randomString(12));
            CacheMap.employeeMap.put(token, employee);
            
            return Result.success(token);
        } catch (Exception e) {
            log.error("员工注册异常: 用户名={}, 错误信息={}", registerDTO.getUserName(), e.getMessage(), e);
            return Result.fail("注册失败，请稍后重试");
        }
    }

}

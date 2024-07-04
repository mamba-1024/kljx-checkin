package com.hzjy.hzjycheckIn.controller.web;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.config.CacheMap;
import com.hzjy.hzjycheckIn.config.WechatUtil;
import com.hzjy.hzjycheckIn.dto.WechatLoginDTO;
import com.hzjy.hzjycheckIn.dto.WechatResultByCode;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import com.hzjy.hzjycheckIn.util.JwtUtils;
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

@RestController
@RequestMapping("/wechat")
public class WeChatController {

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

}

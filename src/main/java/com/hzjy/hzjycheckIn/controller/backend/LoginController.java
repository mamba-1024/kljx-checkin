package com.hzjy.hzjycheckIn.controller.backend;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.dto.AccountDTO;
import com.hzjy.hzjycheckIn.entity.Account;
import com.hzjy.hzjycheckIn.service.AccountService;
import com.hzjy.hzjycheckIn.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 登陆接口
 */
@RestController
@RequestMapping("backend/login")
public class LoginController {

    @Autowired
    private AccountService accountService;

    @PostMapping("login")
    public Result<String> login(@RequestBody AccountDTO accountDTO) {

        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", accountDTO.getName());
        Account account = accountService.getOne(queryWrapper);
        if (Objects.isNull(account)) {
            return Result.fail("用户不存在");
        }

        if (!account.getPassword().equals(accountDTO.getPassword())) {
            return Result.fail("密码有误");
        }
        String token = JwtUtils.generateToken(RandomUtil.randomString(12));
        System.out.print("token: ");
        System.out.print(token);
        return Result.success(token);
    }


}

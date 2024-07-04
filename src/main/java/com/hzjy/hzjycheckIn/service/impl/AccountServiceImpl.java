package com.hzjy.hzjycheckIn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzjy.hzjycheckIn.entity.Account;
import com.hzjy.hzjycheckIn.mapper.AccountMapper;
import com.hzjy.hzjycheckIn.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
}

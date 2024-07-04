package com.hzjy.hzjycheckIn.config;

import com.hzjy.hzjycheckIn.dto.WechatResultByCode;
import com.hzjy.hzjycheckIn.entity.Employee;

import java.util.HashMap;
import java.util.Map;

public class CacheMap {

    public static final Map<String, WechatResultByCode> wechatResultMap = new HashMap<>();

    public static final Map<String, Employee> employeeMap = new HashMap<>();

}

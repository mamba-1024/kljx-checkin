package com.hzjy.hzjycheckIn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzjy.hzjycheckIn.entity.EmployeeAttendance;
import com.hzjy.hzjycheckIn.mapper.EmployeeAttendanceMapper;
import com.hzjy.hzjycheckIn.service.EmployeeAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class EmployeeAttendanceServiceImpl extends ServiceImpl<EmployeeAttendanceMapper, EmployeeAttendance> implements EmployeeAttendanceService {

    @Autowired
    private EmployeeAttendanceMapper employeeAttendanceMapper;

    @Override
    public Map<String, BigDecimal> selectLastDayWorkCount() {
        return employeeAttendanceMapper.selectLastDayWorkCount();
    }
}

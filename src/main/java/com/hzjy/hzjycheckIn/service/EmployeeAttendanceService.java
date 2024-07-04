package com.hzjy.hzjycheckIn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzjy.hzjycheckIn.entity.Account;
import com.hzjy.hzjycheckIn.entity.EmployeeAttendance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
public interface EmployeeAttendanceService extends IService<EmployeeAttendance> {

    Map<String, BigDecimal> selectLastDayWorkCount();
}

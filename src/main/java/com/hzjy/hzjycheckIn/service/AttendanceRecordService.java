package com.hzjy.hzjycheckIn.service;

import com.hzjy.hzjycheckIn.dto.AttendanceSummaryDTO;
import com.hzjy.hzjycheckIn.entity.AttendanceRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.entity.EmployeeAttendance;
import com.hzjy.hzjycheckIn.entity.WorkShift;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
public interface AttendanceRecordService extends IService<AttendanceRecord> {

    AttendanceSummaryDTO calculateStatistics(List<AttendanceRecord> records, List<WorkShift> workShifts);

    Boolean doAttendance(Employee employee, WorkShift workShift, LocalDate nowDate, Integer punchType, EmployeeAttendance employeeAttendance);

}

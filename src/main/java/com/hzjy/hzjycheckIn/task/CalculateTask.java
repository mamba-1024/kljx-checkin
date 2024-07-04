package com.hzjy.hzjycheckIn.task;

import com.hzjy.hzjycheckIn.config.PunchType;
import com.hzjy.hzjycheckIn.entity.AttendanceRecord;
import com.hzjy.hzjycheckIn.entity.EmployeeAttendance;
import com.hzjy.hzjycheckIn.entity.WorkShift;
import com.hzjy.hzjycheckIn.service.AttendanceRecordService;
import com.hzjy.hzjycheckIn.service.EmployeeAttendanceService;
import com.hzjy.hzjycheckIn.service.WorkShiftService;
import com.hzjy.hzjycheckIn.util.BigDecimalGetHoursUtil;
import com.hzjy.hzjycheckIn.util.DurationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hzjy.hzjycheckIn.util.DurationUtil.getDuration;

@Component
@Slf4j
public class CalculateTask {


    @Autowired
    private AttendanceRecordService attendanceRecordService;
    @Autowired
    private WorkShiftService workShiftService;

    @Autowired
    private EmployeeAttendanceService employeeAttendanceService;







}

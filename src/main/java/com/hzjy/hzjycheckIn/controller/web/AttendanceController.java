package com.hzjy.hzjycheckIn.controller.web;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.config.EmployeeContext;
import com.hzjy.hzjycheckIn.config.PunchType;
import com.hzjy.hzjycheckIn.dto.AttendanceDTO;
import com.hzjy.hzjycheckIn.dto.AttendanceInfoDTO;
import com.hzjy.hzjycheckIn.dto.AttendanceQueryDTO;
import com.hzjy.hzjycheckIn.dto.AttendanceSummaryDTO;
import com.hzjy.hzjycheckIn.entity.AttendanceRecord;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.entity.EmployeeAttendance;
import com.hzjy.hzjycheckIn.entity.WorkShift;
import com.hzjy.hzjycheckIn.enums.AuditStatus;
import com.hzjy.hzjycheckIn.service.AttendanceRecordService;
import com.hzjy.hzjycheckIn.service.EmployeeAttendanceService;
import com.hzjy.hzjycheckIn.service.WorkShiftService;
import com.hzjy.hzjycheckIn.util.BigDecimalGetHoursUtil;
import com.hzjy.hzjycheckIn.util.DurationUtil;
import com.hzjy.hzjycheckIn.util.JudgeWorkShiftUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApiOperation("打卡")
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private WorkShiftService workShiftService;

    @Autowired
    private AttendanceRecordService recordService;

    @Autowired
    private EmployeeAttendanceService employeeAttendanceService;

    @ApiOperation("初始化打卡信息")
    @GetMapping("/init")
    public Result<AttendanceInfoDTO> init() {
        Employee employee = EmployeeContext.getEmployee();
        //获取当前时间
        //判断今天是否打卡
        List<WorkShift> workShifts = workShiftService.list();
        AttendanceInfoDTO attendanceInfoDTO = new AttendanceInfoDTO();
        LocalDate nowDate = LocalDate.now();
        boolean isSummer = judgeIsSummer(nowDate);
        if (isSummer) {
            workShifts = workShifts.stream().filter(o -> o.isSummer()).collect(Collectors.toList());
            attendanceInfoDTO.setSeasonalName("夏令时");
        } else {
            workShifts = workShifts.stream().filter(o -> !o.isSummer()).collect(Collectors.toList());
            attendanceInfoDTO.setSeasonalName("冬令时");
        }
        attendanceInfoDTO.setWorkShifts(workShifts);

        QueryWrapper<AttendanceRecord> attendanceRecordQueryWrapper = new QueryWrapper<>();
        attendanceRecordQueryWrapper.eq("employee_id", employee.getId());
        attendanceRecordQueryWrapper.eq("punch_date", nowDate);
        List<AttendanceRecord> attendanceRecords = recordService.list(attendanceRecordQueryWrapper);
        attendanceInfoDTO.setAttendanceRecords(attendanceRecords);

        WorkShift defaultWorkShift = workShifts.get(0);
        //判断当前班次是否打过卡了
        for (WorkShift workShift : workShifts) {
            boolean attendanced = isAttendanced(workShift, attendanceRecords);
            if (!attendanced) {
                defaultWorkShift = workShift;
                break;
            }
        }

        attendanceInfoDTO.setDefaultWorkShift(defaultWorkShift);

        return Result.success(attendanceInfoDTO);
    }

    private boolean judgeIsSummer(LocalDate currentDate) {

        // 获取6月1号和9月30号的LocalDate
        LocalDate juneFirst = LocalDate.of(currentDate.getYear(), Month.MAY, 31);
        LocalDate septemberThirtieth = LocalDate.of(currentDate.getYear(), Month.OCTOBER, 1);

        // 使用 isAfter 和 isBefore 方法来判断
        if (currentDate.isAfter(juneFirst) && currentDate.isBefore(septemberThirtieth)) {
            return true;
        }
        return false;
    }

    private boolean isAttendanced(WorkShift workShift, List<AttendanceRecord> attendanceRecords) {
        if (CollectionUtil.isEmpty(attendanceRecords)) {
            return Boolean.FALSE;
        }
        List<AttendanceRecord> records = attendanceRecords.stream().filter(o -> o.getWorkShiftId().equals(workShift.getId())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(records) || records.size() < 2) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    @ApiOperation("打卡")
    @PostMapping("doAttendance")
    public Result<Boolean> attendance(@RequestBody AttendanceDTO attendanceDTO) {
        Employee employee = EmployeeContext.getEmployee();
        Boolean isAuthenticated = employee.getIsAuthenticated();
        //校验员工状态能否打卡
        if (Boolean.FALSE.equals(isAuthenticated)) {
            return Result.fail("请先实名认证之后再来打卡");
        }
        if (!AuditStatus.PASS.equals(employee.getAuditStatus())) {
            return Result.fail("请等待管理员审核通过再来打卡");
        }
        if (Boolean.FALSE.equals(employee.getStatus())) {
            return Result.fail("您的账号已停用");
        }
        if (Boolean.FALSE.equals(employee.getOnBoard())) {
            return Result.fail("您已离职");
        }
        //获取打卡班次
        Integer workShiftId = attendanceDTO.getWorkShiftId();
        // 根据班次ID获取班次信息
        WorkShift workShift = workShiftService.getById(workShiftId);
        if (Objects.isNull(workShift)) {
            return Result.fail("打卡班次不存在");
        }
        LocalDate nowDate = LocalDate.now();
        Integer punchType = attendanceDTO.getPunchType();
        //上班打卡需要判断
        if (punchType == PunchType.CHECK_IN) {
            //最早打卡时间问题
            LocalTime startLimit = workShift.getStartLimit();
            if (LocalTime.now().isBefore(startLimit)) {
                return Result.fail("当前班次请于：" + startLimit.format(DateTimeFormatter.ofPattern("HH时mm分")) + "之后打卡");
            }
            LocalTime shiftEnd = getShiftEndTime(workShift);
            if (Objects.nonNull(shiftEnd) && LocalTime.now().isAfter(shiftEnd)) {
                return Result.fail("当前时间已经超过当前班次的下班时间，上班打卡失败");
            }

        }

        //查询当天打卡情况
        QueryWrapper<EmployeeAttendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("punch_date", nowDate);
        queryWrapper.eq("employee_id", employee.getId());

        EmployeeAttendance employeeAttendance = employeeAttendanceService.getOne(queryWrapper);
        if (Objects.isNull(employeeAttendance)) {
            employeeAttendance = new EmployeeAttendance();
            employeeAttendance.setEmployeeId(employee.getId());
            employeeAttendance.setPunchDate(nowDate);
            employeeAttendance.setCreateAt(LocalDateTime.now());
        }
        LocalDateTime commonPunchStart = employeeAttendance.getCommonPunchStart();
        LocalDateTime commonPunchEnd = employeeAttendance.getCommonPunchEnd();
        LocalDateTime amStartTime = employeeAttendance.getAmStartTime();
        LocalDateTime amEndTime = employeeAttendance.getAmEndTime();
        LocalDateTime pmStartTime = employeeAttendance.getPmStartTime();
        LocalDateTime pmEndTime = employeeAttendance.getPmEndTime();
        LocalDateTime overPunchStart = employeeAttendance.getOverPunchStart();
        LocalDateTime overPunchEnd = employeeAttendance.getOverPunchEnd();
        //上班打卡
        if (PunchType.CHECK_IN == punchType) {
            //判断当前班次是不是打过卡了
            if (JudgeWorkShiftUtil.isCommonShift(workShiftId)) {
                // 根据班次信息设置amStartTime和pmStartTime
                // 上午班次
                if (JudgeWorkShiftUtil.isAmShift(workShiftId)) {
                    // 上班卡没有打卡
                    if (Objects.isNull(amStartTime)) {
                        LocalDateTime amStartTimeNow = LocalDateTime.now();
                        employeeAttendance.setAmStartTime(amStartTimeNow);
                        employeeAttendance.setAmWorkShiftId(workShiftId);
                        // 设置普通打卡开始时间
                        employeeAttendance.setCommonPunchStart(amStartTimeNow);
                        employeeAttendance.setCommonWorkShiftId(workShiftId);
                    } else {
                        // 上班卡已经打卡
                        return Result.fail("对不起，当前班次已经打过上班卡了");
                    }
                } else {
                    // // 判断上午下班卡是否打过
                    // if (Objects.isNull(amEndTime)) {
                    //     return Result.fail("上午下班卡没打，不能打下午上班卡，请切换【上午】班次打下班卡");
                    // }
                    // 下午班次上班卡没有打卡
                    if (Objects.isNull(pmStartTime)) {
                        employeeAttendance.setPmStartTime(LocalDateTime.now());
                        employeeAttendance.setPmWorkShiftId(workShiftId);
                    } else {
                        // 下午上班卡已经打卡
                        return Result.fail("对不起，当前班次已经打过上班卡了");
                    }
                }
                // if (Objects.isNull(commonPunchStart) || LocalDateTime.now().isBefore(commonPunchStart)) {
                //     employeeAttendance.setCommonPunchStart(LocalDateTime.now());
                // }
                // employeeAttendance.setCommonWorkShiftId(workShiftId);
            } else {
                // 加班班次
                // 判断下午下班卡是否打过
                // if (Objects.isNull(pmEndTime)) {
                //     return Result.fail("【下午】下班卡没打，不能打加班卡，请切换【下午】班次打下班卡");
                // }
                if (Objects.nonNull(overPunchStart)) {
                    return Result.fail("对不起，当前班次已经打过上班卡了");
                }
                employeeAttendance.setOverPunchStart(LocalDateTime.now());
                employeeAttendance.setOverWorkShiftId(workShiftId);
            }
        } else {
            // 下班打卡
            if (JudgeWorkShiftUtil.isCommonShift(workShiftId)) {
                // 上午班次
                if (JudgeWorkShiftUtil.isAmShift(workShiftId)) {
                    if (Objects.isNull(amEndTime)) {
                        LocalDateTime amEndTimeNow = LocalDateTime.now();
                        employeeAttendance.setAmEndTime(amEndTimeNow);
                        employeeAttendance.setAmWorkShiftId(workShiftId);

                         // 计算上午上班时长
                        Duration amDuration = DurationUtil.getDuration(amStartTime.toLocalTime(), amEndTimeNow.toLocalTime(), workShift);
                        employeeAttendance.setAmPunchDuration(BigDecimalGetHoursUtil.minutesToHours(amDuration.toMinutes()));
                        // 上午下班后，普遍班次的工作时长等于上午上班时长
                        employeeAttendance.setCommonPunchDuration(BigDecimalGetHoursUtil.minutesToHours(amDuration.toMinutes()));
                    } else {
                        return Result.fail("对不起，当前班次已经打过下班卡了");
                    }
                } else {
                    // 下午班次
                    if (Objects.isNull(pmEndTime)) {
                        LocalDateTime pmEndTimeNow = LocalDateTime.now();
                        employeeAttendance.setPmEndTime(pmEndTimeNow);
                        employeeAttendance.setPmWorkShiftId(workShiftId);
                        // 下午下班打卡 设置普通打卡结束时间
                        employeeAttendance.setCommonPunchEnd(pmEndTimeNow);
                        employeeAttendance.setCommonWorkShiftId(workShiftId);
                        // 计算下午上班时长
                        Duration pmDuration = DurationUtil.getDuration(pmStartTime.toLocalTime(), pmEndTimeNow.toLocalTime(), workShift);
                        employeeAttendance.setPmPunchDuration(BigDecimalGetHoursUtil.minutesToHours(pmDuration.toMinutes()));
                        // commonPunchDuration = amPunchDuration + pmPunchDuration
                        employeeAttendance.setCommonPunchDuration(employeeAttendance.getAmPunchDuration().add(employeeAttendance.getPmPunchDuration()));
                    } else {
                        return Result.fail("对不起，当前班次已经打过下班卡了");
                    }
                }
            } else {
                // 加班班次
                if (Objects.nonNull(overPunchEnd)) {
                    return Result.fail("对不起，当前班次已经打过卡了");
                }
                // if (Objects.isNull(overPunchStart)) {
                //     return Result.fail("当前班次上班卡没打，不能打下班卡");
                // }
                LocalDateTime overPunchEndNow = LocalDateTime.now();
                employeeAttendance.setOverPunchEnd(overPunchEndNow);
                Duration duration = DurationUtil.getDuration(overPunchStart.toLocalTime(), overPunchEndNow.toLocalTime(), workShift);
                employeeAttendance.setOverPunchDuration(BigDecimalGetHoursUtil.minutesToHours(duration.toMinutes()));
            }
        }

        recordService.doAttendance(employee, workShift, nowDate, punchType, employeeAttendance);

        return Result.success(Boolean.TRUE);
    }


    @ApiOperation("查看考勤记录")
    @PostMapping("attendanceRecord")
    public Result<AttendanceSummaryDTO> attendanceRecord(@RequestBody AttendanceQueryDTO attendanceQueryDTO) {
        Employee employee = EmployeeContext.getEmployee();
        //查询班次信息
        List<WorkShift> workShifts = workShiftService.list();
        //插入打卡记录
        LocalDate now = attendanceQueryDTO.getQueryMonth();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate nextMonthFirstDay = firstDayOfMonth.plusMonths(1);
        //判断今天是否打卡
        QueryWrapper<AttendanceRecord> attendanceRecordQueryWrapper = new QueryWrapper<>();
        attendanceRecordQueryWrapper.eq("employee_id", employee.getId());
        attendanceRecordQueryWrapper.between("punch_date", firstDayOfMonth, nextMonthFirstDay);
        // employee_id 和 punch_date 查询当天打卡情况, 正常情况会查出4条记录，分别是上午上班，上午下班，下午上班，下午下班
        List<AttendanceRecord> attendanceRecords = recordService.list(attendanceRecordQueryWrapper);
        AttendanceSummaryDTO attendanceSummaryDTO = recordService.calculateStatistics(attendanceRecords, workShifts);
        return Result.success(attendanceSummaryDTO);
    }


    private LocalTime getShiftEndTime(WorkShift workShift) {
        LocalTime pmEnd = workShift.getEndTime();
        if (Objects.nonNull(pmEnd) && !LocalTime.MIDNIGHT.equals(pmEnd)) {
            return pmEnd;
        }
        LocalTime amEnd = workShift.getEndTime();
        if (Objects.nonNull(amEnd) && !LocalTime.MIDNIGHT.equals(amEnd)) {
            return amEnd;
        }
        return null;
    }

}

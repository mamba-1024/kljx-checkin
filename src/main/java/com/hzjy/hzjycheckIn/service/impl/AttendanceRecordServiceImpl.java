package com.hzjy.hzjycheckIn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzjy.hzjycheckIn.config.PunchType;
import com.hzjy.hzjycheckIn.dto.AttendanceDayInfoDTO;
import com.hzjy.hzjycheckIn.dto.AttendanceSummaryDTO;
import com.hzjy.hzjycheckIn.entity.AttendanceRecord;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.entity.EmployeeAttendance;
import com.hzjy.hzjycheckIn.entity.WorkShift;
import com.hzjy.hzjycheckIn.mapper.AttendanceRecordMapper;
import com.hzjy.hzjycheckIn.service.AttendanceRecordService;
import com.hzjy.hzjycheckIn.service.EmployeeAttendanceService;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import com.hzjy.hzjycheckIn.util.BigDecimalGetHoursUtil;
import com.hzjy.hzjycheckIn.util.JudgeWorkShiftUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@Service
public class AttendanceRecordServiceImpl extends ServiceImpl<AttendanceRecordMapper, AttendanceRecord> implements AttendanceRecordService {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeAttendanceService employeeAttendanceService;

    @Override
    public AttendanceSummaryDTO calculateStatistics(List<AttendanceRecord> records, List<WorkShift> workShifts) {
        Map<Integer, List<WorkShift>> listMap = workShifts.stream().collect(Collectors.groupingBy(WorkShift::getId));
        List<AttendanceDayInfoDTO> attendanceDayInfoDTOS = new ArrayList<>();
        //每天每个班次的时长
        Map<LocalDate, List<AttendanceRecord>> dateListMap = records.stream().collect(Collectors.groupingBy(o -> o.getCreateTime().toLocalDate()));
        Set<LocalDate> localDates = dateListMap.keySet();

        Map<String, BigDecimal> shiftDurationMap = new HashMap<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (LocalDate localDate : localDates) {
            AttendanceDayInfoDTO attendanceDayInfoDTO = new AttendanceDayInfoDTO();
            attendanceDayInfoDTOS.add(attendanceDayInfoDTO);
            attendanceDayInfoDTO.setLocalDate(localDate);
            Duration todayDuration = Duration.ZERO;
            attendanceDayInfoDTO.setDuration(BigDecimal.ZERO);
            List<AttendanceDayInfoDTO.AttendanceDayDetailInfo> dayDetailInfos = new ArrayList<>();
            attendanceDayInfoDTO.setAttendanceDayDetailInfos(dayDetailInfos);

            List<AttendanceRecord> attendanceRecords = dateListMap.get(localDate);

            Map<Integer, List<AttendanceRecord>> workShiftMap = attendanceRecords.stream().collect(Collectors.groupingBy(o -> o.getWorkShiftId()));
            Set<Integer> workShiftSet = workShiftMap.keySet();
            for (Integer workShiftId : workShiftSet) {
                AttendanceDayInfoDTO.AttendanceDayDetailInfo dayDetailInfo = new AttendanceDayInfoDTO.AttendanceDayDetailInfo();
                List<WorkShift> workShiftList = listMap.get(workShiftId);
                WorkShift workShift = workShiftList.get(0);
                String shiftName = workShift.getShiftName();
                dayDetailInfo.setShiftName(shiftName);

                List<AttendanceRecord> attendanceRecordList = workShiftMap.get(workShiftId);
                attendanceRecordList.sort(Comparator.comparing(AttendanceRecord::getPunchType));
                //签到时间
                AttendanceRecord attendanceRecord = attendanceRecordList.get(0);
                StringBuilder showTime = new StringBuilder();
                dayDetailInfo.setStartTime(attendanceRecord.getPunchTime());
                showTime.append(dateTimeFormatter.format(attendanceRecord.getPunchTime())).append("   ~  ");
                if (attendanceRecordList.size() > 1) {
                    AttendanceRecord endAttendanceRecord = attendanceRecordList.get(1);
                    dayDetailInfo.setEndTime(endAttendanceRecord.getPunchTime());
                    showTime.append(dateTimeFormatter.format(endAttendanceRecord.getPunchTime()));
                    Duration todayWorkShiftDuration = getDuration(attendanceRecord, endAttendanceRecord, workShift);
                    dayDetailInfo.setDuration(BigDecimalGetHoursUtil.minutesToHours(todayWorkShiftDuration.toMinutes()));
                    BigDecimal totalShiftDuration = shiftDurationMap.get(shiftName);
                    todayDuration = todayDuration.plus(todayWorkShiftDuration);
                    attendanceDayInfoDTO.setDuration(BigDecimalGetHoursUtil.minutesToHours(todayDuration.toMinutes()));
                    if (Objects.isNull(totalShiftDuration)) {
                        totalShiftDuration = BigDecimal.ZERO;
                    }
                    totalShiftDuration = totalShiftDuration.add(BigDecimalGetHoursUtil.minutesToHours(todayWorkShiftDuration.toMinutes()));
                    shiftDurationMap.put(shiftName, totalShiftDuration);
                }
                dayDetailInfo.setShowTime(showTime.toString());
                dayDetailInfos.add(dayDetailInfo);
            }

        }

        return new AttendanceSummaryDTO(attendanceDayInfoDTOS, shiftDurationMap);
    }

    @Override
    public Boolean doAttendance(Employee employee, WorkShift workShift, LocalDate nowDate, Integer punchType, EmployeeAttendance employeeAttendance) {
        //插入打卡记录
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setEmployeeId(employee.getId());
        attendanceRecord.setPunchTime(LocalDateTime.now());
        attendanceRecord.setPunchType(punchType);
        attendanceRecord.setWorkShiftId(workShift.getId());
        attendanceRecord.setPunchDate(nowDate);
        if (punchType == PunchType.CHECK_OUT) {
            //打卡时长，小时
            BigDecimal hours;
            if (JudgeWorkShiftUtil.isCommonShift(workShift.getId())) {
                hours = employeeAttendance.getCommonPunchDuration();
            } else {
                hours = employeeAttendance.getOverPunchDuration();
            }

            BigDecimal currentMonthTime = employee.getCurrentMonthTime().add(hours);
            employee.setCurrentMonthTime(currentMonthTime.setScale(1, BigDecimal.ROUND_HALF_UP));
            employee.setTotalWorkTime(employee.getTotalWorkTime().add(hours));
            BigDecimal leftWorkTime = employee.getLeftWorkTime();
            leftWorkTime = leftWorkTime.add(hours);
            if (leftWorkTime.compareTo(BigDecimal.valueOf(9.5)) >= 0) {
                employee.setPoints(employee.getPoints() + 1);
                employee.setLevel(employee.getPoints() / 110);
                leftWorkTime = leftWorkTime.subtract(BigDecimal.valueOf(9.5));
            }
            employee.setLeftWorkTime(leftWorkTime);
            employeeService.updateById(employee);
        }
        employeeAttendanceService.saveOrUpdate(employeeAttendance);
        this.save(attendanceRecord);
        return Boolean.TRUE;
    }


    private Duration getDuration(AttendanceRecord prevRecord, AttendanceRecord endWorkRecord, WorkShift workShift) {
        LocalTime startDateTime = prevRecord.getPunchTime().toLocalTime();
        LocalTime endDateTime = endWorkRecord.getPunchTime().toLocalTime();

        // 打卡班次为常规班次的时候，中间的11:30-12:00不算时间

        if (startDateTime.isBefore(workShift.getStartTime())) {
            startDateTime = workShift.getStartTime();
        }
        if (endDateTime.isAfter(workShift.getEndTime())) {
            endDateTime = workShift.getEndTime();
        }
        if (startDateTime.isAfter(endDateTime)) {
            return Duration.ZERO;
        }
        if (workShift.getExcludeStart() != null) {
            LocalTime lunchTimeStart = workShift.getExcludeStart();
            Duration start = Duration.ZERO;
            Duration end = Duration.ZERO;
            if (startDateTime.isBefore(lunchTimeStart)) {
                if (endDateTime.isBefore(lunchTimeStart)) {
                    start = Duration.between(startDateTime, endDateTime);
                } else {
                    start = Duration.between(startDateTime, lunchTimeStart);
                }
            }
            start = start.compareTo(Duration.ZERO) >= 0 ? start : Duration.ZERO;

            LocalTime lunchTimeEnd = workShift.getExcludeEnd();
            if (endDateTime.isAfter(lunchTimeEnd)) {
                if (startDateTime.isAfter(lunchTimeEnd)) {
                    end = Duration.between(startDateTime, endDateTime);
                } else {
                    end = Duration.between(lunchTimeEnd, endDateTime);
                }
            }
            end = end.compareTo(Duration.ZERO) >= 0 ? end : Duration.ZERO;
            return start.plus(end);
        }

        return Duration.between(startDateTime, endDateTime);
    }


    private AttendanceRecord getPrevRecord(AttendanceRecord record, Map<LocalDate, List<AttendanceRecord>> dateListMap) {
        LocalDate date = record.getPunchTime().toLocalDate();
        List<AttendanceRecord> records = getRecordsByDate(date, dateListMap);
        int index = records.indexOf(record);
        if (index > 0) {
            return records.get(index - 1);
        }
        return null;
    }

    private List<AttendanceRecord> getRecordsByDate(LocalDate date, Map<LocalDate, List<AttendanceRecord>> dateListMap) {
        // 从数据库中获取当天的打卡记录，此处省略
        return dateListMap.get(date);
    }

    private WorkShift getWorkShiftById(int workShiftId, List<WorkShift> workShifts) {
        for (WorkShift workShift : workShifts) {
            if (workShift.getId() == workShiftId) {
                return workShift;
            }
        }
        return null;
    }


}

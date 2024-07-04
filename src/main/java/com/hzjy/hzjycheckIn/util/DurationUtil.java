package com.hzjy.hzjycheckIn.util;

import com.hzjy.hzjycheckIn.entity.WorkShift;

import java.time.Duration;
import java.time.LocalTime;

public class DurationUtil {

    public static Duration getDuration(LocalTime startDateTime, LocalTime endDateTime, WorkShift workShift) {
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

}

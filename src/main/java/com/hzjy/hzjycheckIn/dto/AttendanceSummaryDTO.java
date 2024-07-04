package com.hzjy.hzjycheckIn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class AttendanceSummaryDTO {

    private List<AttendanceDayInfoDTO> attendanceDayInfoDTOS;

    private Map<String, BigDecimal> shiftDurationMap;

}




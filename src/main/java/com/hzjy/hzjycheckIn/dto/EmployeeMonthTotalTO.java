package com.hzjy.hzjycheckIn.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmployeeMonthTotalTO {

    private Long employeeId;

    private BigDecimal totalCommonDuration;
    private BigDecimal totalOverDuration;

    private BigDecimal totalDuration;

}

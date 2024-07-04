package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmployeeAttendance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工ID
     */
    private Integer employeeId;

    /**
     * 打卡日期
     */
    private LocalDate punchDate;

    /**
     * 普通打卡开始时间
     */
    private LocalDateTime commonPunchStart;
    /**
     * 普通打卡结束时间
     */
    private LocalDateTime commonPunchEnd;

    /**
     * 普通打卡班次ID
     */
    private Integer commonWorkShiftId;
    /**
     * 普通打卡时长
     */
    private BigDecimal commonPunchDuration;
    /**
     * 加班打卡开始时间
     */
    private LocalDateTime overPunchStart;
    /**
     * 加班打卡结束时间
     */
    private LocalDateTime overPunchEnd;
    /**
     * 加班打卡总时长
     */
    private BigDecimal overPunchDuration;

    /**
     * 加班打卡班次ID
     */
    private Integer overWorkShiftId;

    private LocalDateTime createAt;


}

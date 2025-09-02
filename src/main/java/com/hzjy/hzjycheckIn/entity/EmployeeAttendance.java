package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工考勤实体类
 * </p>
 * <p>
 * 用于记录员工每日的打卡信息，包括普通班次和加班班次的打卡时间、时长等数据
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@Getter
@Setter
public class EmployeeAttendance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工ID
     * 关联employee表的id字段
     */
    private Integer employeeId;

    /**
     * 打卡日期
     * 记录员工打卡的具体日期
     */
    private LocalDate punchDate;

    /**
     * 普通打卡开始时间
     * 员工正常班次的上班打卡时间
     */
    private LocalDateTime commonPunchStart;
    
    /**
     * 普通打卡结束时间
     * 员工正常班次的下班打卡时间
     */
    private LocalDateTime commonPunchEnd;

    /**
     * 普通打卡班次ID
     * 关联work_shift表的班次ID
     */
    private Integer commonWorkShiftId;
    
    /**
     * 普通打卡时长
     * 员工正常班次的工作时长（小时）
     */
    private BigDecimal commonPunchDuration;
    
    /**
     * 加班打卡开始时间
     * 员工加班班次的开始打卡时间
     */
    private LocalDateTime overPunchStart;
    
    /**
     * 加班打卡结束时间
     * 员工加班班次的结束打卡时间
     */
    private LocalDateTime overPunchEnd;
    
    /**
     * 加班打卡总时长
     * 员工加班班次的工作时长（小时）
     */
    private BigDecimal overPunchDuration;

    /**
     * 加班打卡班次ID
     * 关联work_shift表的加班班次ID
     */
    private Integer overWorkShiftId;

    /**
     * 创建时间
     * 记录创建该考勤记录的时间
     */
    private LocalDateTime createAt;

}

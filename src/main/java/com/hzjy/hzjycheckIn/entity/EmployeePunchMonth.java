package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 员工月度打卡统计实体类
 * </p>
 * <p>
 * 用于记录员工每月的打卡统计信息，包括普通班次和加班班次的工作时长统计
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@Getter
@Setter
public class EmployeePunchMonth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工ID
     * 关联employee表的id字段，标识统计记录属于哪个员工
     */
    private Integer employeeId;

    /**
     * 打卡月份
     * 统计的月份，格式通常为：YYYY-MM，如：2024-01
     */
    private String punchMonth;

    /**
     * 普通班次打卡时长
     * 员工在普通班次的工作时长统计（小时）
     */
    private double commonPunch;

    /**
     * 加班班次打卡时长
     * 员工在加班班次的工作时长统计（小时）
     */
    private double overPunch;

}

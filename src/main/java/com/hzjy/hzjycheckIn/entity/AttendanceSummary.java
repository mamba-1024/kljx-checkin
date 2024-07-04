package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@Getter
@Setter
@TableName("attendance_summary")
public class AttendanceSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 统计记录ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工ID，外键，关联employee表的id字段
     */
    private Integer employeeId;

    /**
     * 考勤日期
     */
    private LocalDate attendanceDate;

    /**
     * 班次ID，外键，关联work_shift表的id字段
     */
    private Integer workShiftId;

    /**
     * 是否迟到，0表示否，1表示是
     */
    private Integer isLate;

    /**
     * 是否早退，0表示否，1表示是
     */
    private Integer isEarlyLeave;

    /**
     * 是否缺勤，0表示否，1表示是
     */
    private Integer isAbsent;

    /**
     * 创建时间，默认为当前时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间，默认为当前时间，且每次更新时自动更新
     */
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 工作时长（小时）
     */
    private BigDecimal workHours;
}

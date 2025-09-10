package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("attendance_record")
public class AttendanceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 打卡记录ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工ID，外键，关联employee表的id字段
     */
    private Integer employeeId;

    /**
     * 打卡时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime punchTime;

    /**
     * 打卡类型，0表示上班打卡，1表示下班打卡
     */
    private Integer punchType;

    /**
     * 打卡地点
     */
    private String location;

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
     * 班次ID
     */
    private Integer workShiftId;

    /**
     * 打卡日期
     */
    private LocalDate punchDate;

}

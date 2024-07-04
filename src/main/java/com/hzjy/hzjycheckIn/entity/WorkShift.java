package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 *
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:56
 */
@Getter
@Setter
@TableName("work_shift")
public class WorkShift implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班次ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 班次名称
     */
    private String shiftName;

    /**
     * 上班时间
     */
    private LocalTime startTime;

    /**
     * 下班时间
     */
    private LocalTime endTime;

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
     * 班次排除开始时间
     */
    private LocalTime excludeStart;

    /**
     * 班次排除结束时间
     */
    private LocalTime excludeEnd;

    /**
     * 最早打卡时间
     */
    private LocalTime startLimit;

    private boolean isSummer;
}

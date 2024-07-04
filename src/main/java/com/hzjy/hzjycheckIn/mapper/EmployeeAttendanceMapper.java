package com.hzjy.hzjycheckIn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzjy.hzjycheckIn.entity.EmployeeAttendance;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
public interface EmployeeAttendanceMapper extends BaseMapper<EmployeeAttendance> {

    @Select("SELECT\n" +
            "\tCOUNT( CASE WHEN ea.common_punch_duration >0 THEN 1 END )  AS totalCommon,\n" +
            "\t\tCOUNT( CASE WHEN ea.over_punch_duration >0 THEN 1 END ) AS totalOver \n" +
            "FROM\n" +
            "\temployee_attendance ea \n" +
            "WHERE\n" +
            "\tea.punch_date = DATE_SUB(\n" +
            "\tCURDATE(),\n" +
            "\tINTERVAL 1 DAY)")
    Map<String, BigDecimal> selectLastDayWorkCount();
}

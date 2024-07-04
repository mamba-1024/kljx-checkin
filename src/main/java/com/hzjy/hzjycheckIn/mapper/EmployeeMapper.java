package com.hzjy.hzjycheckIn.mapper;

import com.hzjy.hzjycheckIn.dto.EmployeeMonthTotalTO;
import com.hzjy.hzjycheckIn.dto.MonthExportDTO;
import com.hzjy.hzjycheckIn.dto.backend.EmployeeAttendanceVO;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select("SELECT\n" +
            "\tCOUNT( CASE WHEN enable_date IS NOT NULL THEN 1 END ) AS yesterday_enabled_count,\n" +
            "\tCOUNT( CASE WHEN disable_date IS NOT NULL THEN 1 END ) AS yesterday_disabled_count,\n" +
            "\tCOUNT( CASE WHEN DATE( enable_date ) = DATE_SUB( CURDATE(), INTERVAL 1 DAY ) THEN 1 END ) AS yesterday_new_count \n" +
            "FROM\n" +
            "\temployee;;")
    Map<String,Long> selectLastDayCount();

    @Select(" SELECT\n" +
            "            CONCAT('LV',\n" +
            "                   CASE level\n" +
            "                       WHEN 0 THEN '0'\n" +
            "                       WHEN 1 THEN '1'\n" +
            "                       WHEN 2 THEN '2'\n" +
            "                       WHEN 3 THEN '3'\n" +
            "                       ELSE CAST(level AS CHAR)\n" +
            "                       END\n" +
            "                ) AS level_display,\n" +
            "            COUNT(*) AS count\n" +
            "        FROM\n" +
            "            employee\n" +
            " WHERE `status` IS NOT NULL "+
            "        GROUP BY\n" +
            "            level_display;")
    List<Map<String, Object>> queryLevelCount();

    @Select("SELECT\n" +
            "  employee_id,\n" +
            "  COALESCE(SUM(common_punch_duration), 0) AS total_common_duration,\n" +
            "  COALESCE(SUM(over_punch_duration), 0) AS total_over_duration,\n" +
            "  COALESCE(SUM(common_punch_duration), 0) + COALESCE(SUM(over_punch_duration), 0) AS total_duration\n" +
            "FROM\n" +
            "  employee_attendance\n" +
            "WHERE\n" +
            "  MONTH(punch_date) =MONTH(#{monthExportDTO.monthDate})\n" + // 使用monthExportDTO中的属性作为参数
            "GROUP BY\n" +
            "  employee_id;\n")
    List<EmployeeMonthTotalTO> queryMonth(@Param("monthExportDTO") MonthExportDTO monthExportDTO);

}

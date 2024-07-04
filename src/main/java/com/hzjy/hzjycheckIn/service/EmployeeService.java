package com.hzjy.hzjycheckIn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzjy.hzjycheckIn.dto.AuditRejectDTO;
import com.hzjy.hzjycheckIn.dto.EmployeeMonthTotalTO;
import com.hzjy.hzjycheckIn.dto.MonthExportDTO;
import com.hzjy.hzjycheckIn.dto.backend.EmployeeAttendanceVO;
import com.hzjy.hzjycheckIn.dto.query.EmployeeQueryQO;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
public interface EmployeeService extends IService<Employee> {

    Employee getEmployeeByOpenId(String openid);

    Employee getEmployeeByPhone(String phone);

    Employee getEmployeeByToken(String token);

    Page<Employee> listEmployee(EmployeeQueryQO employeeQueryQO);

    Boolean enable(int id);

    Boolean disable(int id);

    Boolean resign(int id);

    Boolean incumbency(int id);

    Boolean clearPoints(int id);

    Boolean auditPass(int id);

    Boolean auditReject(AuditRejectDTO auditRejectDTO);

    Page<Employee> listEmployeeAuditList(EmployeeQueryQO employeeQueryQO);

    Map<String,Long> selectLastDayCount();

    List<Employee> selectBig10();

    Integer countWaitAudit();

    List<Map<String, Object>> queryLevelCount();

    List<EmployeeMonthTotalTO> queryMonth(MonthExportDTO monthExportDTO);
}

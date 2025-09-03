package com.hzjy.hzjycheckIn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzjy.hzjycheckIn.config.CacheMap;
import com.hzjy.hzjycheckIn.dto.AuditRejectDTO;
import com.hzjy.hzjycheckIn.dto.EmployeeMonthTotalTO;
import com.hzjy.hzjycheckIn.dto.MonthExportDTO;
import com.hzjy.hzjycheckIn.dto.backend.EmployeeAttendanceVO;
import com.hzjy.hzjycheckIn.dto.query.EmployeeQueryQO;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.enums.AuditStatus;
import com.hzjy.hzjycheckIn.mapper.EmployeeMapper;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee getEmployeeByOpenId(String openid) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("open_id", openid);
        return this.getOne(queryWrapper);
    }

    @Override
    public Employee getEmployeeByPhone(String phone) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        return this.getOne(queryWrapper);
    }

    @Override
    public Employee getEmployeeByUsername(String userName) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", userName);
        return this.getOne(queryWrapper);
    }

    @Override
    public Employee getEmployeeByToken(String token) {
        Employee employee = CacheMap.employeeMap.get(token);
        return employee;
    }

    @Override
    public Page<Employee> listEmployee(EmployeeQueryQO employeeQueryQO) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_authenticated", Boolean.TRUE);
        queryWrapper.like(StringUtils.isNotEmpty(employeeQueryQO.getName()), "name", employeeQueryQO.getName());
        queryWrapper.like(StringUtils.isNotEmpty(employeeQueryQO.getPhone()), "phone", employeeQueryQO.getPhone());
        queryWrapper.eq(Objects.nonNull(employeeQueryQO.getLevel()), "level", employeeQueryQO.getLevel());
        queryWrapper.eq(Objects.nonNull(employeeQueryQO.getStatus()), "status", employeeQueryQO.getStatus());
        queryWrapper.eq(Objects.nonNull(employeeQueryQO.getOnBoard()), "on_board", employeeQueryQO.getOnBoard());
        queryWrapper.in("audit_status", Arrays.asList(AuditStatus.PASS));
        LocalDate enableDateEnd = Objects.isNull(employeeQueryQO.getEnableDateEnd()) ? null : employeeQueryQO.getEnableDateEnd().plusDays(1);
        LocalDate disableDateEnd = Objects.isNull(employeeQueryQO.getDisableDateEnd()) ? null : employeeQueryQO.getDisableDateEnd().plusDays(1);
        queryWrapper.between(Objects.nonNull(employeeQueryQO.getEnableDateStart()) || Objects.nonNull(enableDateEnd), "enable_date", employeeQueryQO.getEnableDateStart(), enableDateEnd);
        queryWrapper.between(Objects.nonNull(employeeQueryQO.getDisableDateStart()) || Objects.nonNull(disableDateEnd), "disable_date", employeeQueryQO.getDisableDateStart(), disableDateEnd);
        queryWrapper.orderByDesc(Arrays.asList("enable_date", "disable_date"));
        Page<Employee> page1 = new Page<>(employeeQueryQO.getPage(), employeeQueryQO.getPageSize());
        Page<Employee> page = this.page(page1, queryWrapper);

        return page;
    }

    @Override
    public Boolean enable(int id) {
        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("status", Boolean.TRUE);
        updateWrapper.set("enable_date", LocalDateTime.now());
        updateWrapper.set("disable_date", null);
        this.update(updateWrapper);
        return Boolean.TRUE;
    }

    @Override
    public Boolean disable(int id) {
        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("status", Boolean.FALSE);
        updateWrapper.set("enable_date", null);
        updateWrapper.set("disable_date", LocalDateTime.now());
        this.update(updateWrapper);
        return Boolean.TRUE;
    }

    @Override
    public Boolean resign(int id) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setOnBoard(Boolean.FALSE);
        this.updateById(employee);
        return Boolean.TRUE;
    }

    @Override
    public Boolean incumbency(int id) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setOnBoard(Boolean.TRUE);
        this.updateById(employee);
        return Boolean.TRUE;
    }

    @Override
    public Boolean clearPoints(int id) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setPoints(0);
        employee.setLevel(0);
        this.updateById(employee);
        return Boolean.TRUE;
    }

    @Override
    public Boolean auditPass(int id) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setPoints(0);
        employee.setAuditStatus(AuditStatus.PASS);
        employee.setOnBoard(Boolean.TRUE);
        employee.setStatus(Boolean.TRUE);
        employee.setOnboardingDate(LocalDateTime.now());
        employee.setEnableDate(LocalDateTime.now());
        this.updateById(employee);
        return Boolean.TRUE;
    }

    @Override
    public Boolean auditReject(AuditRejectDTO auditRejectDTO) {
        Employee employee = new Employee();
        employee.setId(auditRejectDTO.getId());
        employee.setPoints(0);
        employee.setAuditStatus(AuditStatus.REJECT);
        employee.setRejectReason(auditRejectDTO.getRejectReason());
        this.updateById(employee);
        return Boolean.TRUE;
    }

    @Override
    public Page<Employee> listEmployeeAuditList(EmployeeQueryQO employeeQueryQO) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_authenticated", Boolean.TRUE);
        queryWrapper.like(StringUtils.isNotEmpty(employeeQueryQO.getName()), "name", employeeQueryQO.getName());
        queryWrapper.like(StringUtils.isNotEmpty(employeeQueryQO.getPhone()), "phone", employeeQueryQO.getPhone());
        queryWrapper.eq(Objects.nonNull(employeeQueryQO.getLevel()), "level", employeeQueryQO.getLevel());
        queryWrapper.eq(Objects.nonNull(employeeQueryQO.getStatus()), "status", employeeQueryQO.getStatus());
        queryWrapper.eq(Objects.nonNull(employeeQueryQO.getOnBoard()), "on_board", employeeQueryQO.getOnBoard());
        queryWrapper.eq(Objects.nonNull(employeeQueryQO.getAuditStatus()), "audit_status", employeeQueryQO.getAuditStatus());

        LocalDate resignEnd = Objects.isNull(employeeQueryQO.getResignEnd()) ? null : employeeQueryQO.getResignEnd().plusDays(1);
//        queryWrapper.between(Objects.nonNull(employeeQueryQO.getIncumbencyStart()) || Objects.nonNull(employeeQueryQO.getIncumbencyEnd()), "onboarding_date", employeeQueryQO.getIncumbencyStart(), employeeQueryQO.getIncumbencyEnd());
        queryWrapper.between(Objects.nonNull(employeeQueryQO.getResignStart()) || Objects.nonNull(resignEnd), "create_time", employeeQueryQO.getResignStart(), resignEnd);
        queryWrapper.orderByDesc("create_time");
        Page<Employee> page1 = new Page<>(employeeQueryQO.getPage(), employeeQueryQO.getPageSize());
        Page<Employee> page = this.page(page1, queryWrapper);

        return page;
    }

    @Override
    public Map<String, Long> selectLastDayCount() {
        return employeeMapper.selectLastDayCount();
    }

    @Override
    public List<Employee> selectBig10() {
        QueryWrapper<Employee> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.orderByDesc("points");

        return this.page(new Page<>(1, 10), objectQueryWrapper).getRecords();
    }

    @Override
    public Integer countWaitAudit() {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("audit_status", AuditStatus.WAIT_AUDIT);
        return (int) this.count(queryWrapper);
    }

    @Override
    public  List<Map<String, Object>> queryLevelCount() {
        return employeeMapper.queryLevelCount();
    }

    @Override
    public List<EmployeeMonthTotalTO> queryMonth(MonthExportDTO monthExportDTO) {
        return employeeMapper.queryMonth(monthExportDTO);
    }
}

package com.hzjy.hzjycheckIn.controller.backend;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.config.ExcelUtil;
import com.hzjy.hzjycheckIn.config.FormatUtils;
import com.hzjy.hzjycheckIn.dto.AuditRejectDTO;
import com.hzjy.hzjycheckIn.dto.backend.EmployeeInfoVO;
import com.hzjy.hzjycheckIn.dto.query.EmployeePunchMonthQueryQO;
import com.hzjy.hzjycheckIn.dto.query.EmployeeQueryQO;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.entity.EmployeeDetails;
import com.hzjy.hzjycheckIn.entity.EmployeePunchMonth;
import com.hzjy.hzjycheckIn.enums.AuditStatus;
import com.hzjy.hzjycheckIn.service.EmployeeDetailsService;
import com.hzjy.hzjycheckIn.service.EmployeePunchMonthService;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import com.hzjy.hzjycheckIn.util.ExportSetHeaderUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("backend/employee")
public class BackendEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeDetailsService employeeDetailsService;

    @Autowired
    private EmployeePunchMonthService punchMonthService;

    @Autowired
    private OSS ossClient;

    @ApiOperation("查看在职员工列表")
    @PostMapping("/listIncumbency")
    public Result<Page<EmployeeInfoVO>> listEmployee(@RequestBody EmployeeQueryQO employeeQueryQO) {
        employeeQueryQO.setAuditStatus(AuditStatus.PASS);
        employeeQueryQO.setIsEmployeeList(Boolean.TRUE);
        Page<Employee> employeePage = employeeService.listEmployee(employeeQueryQO);
        Page<EmployeeInfoVO> page = new Page<>();
        List<Employee> records = employeePage.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            List<EmployeeInfoVO> employeeInfoVOS = new ArrayList<>();
            for (Employee record : records) {
                EmployeeInfoVO employeeInfoVO = new EmployeeInfoVO();
                BeanUtils.copyProperties(record, employeeInfoVO);
                employeeInfoVOS.add(employeeInfoVO);
            }
            page.setRecords(employeeInfoVOS);
        }
        page.setSize(employeePage.getSize());
        page.setTotal(employeePage.getTotal());
        page.setCurrent(employeePage.getCurrent());
        page.setPages(employeePage.getPages());
        return Result.success(page);
    }

    @ApiOperation("查看审核员工列表")
    @PostMapping("/listAudit")
    public Result<Page<Employee>> listAudit(@RequestBody EmployeeQueryQO employeeQueryQO) {
        employeeQueryQO.setIsAuditList(Boolean.TRUE);
        return Result.success(employeeService.listEmployeeAuditList(employeeQueryQO));
    }

    @ApiOperation("审核通过")
    @PostMapping("/auditPass/{id}")
    public Result<Boolean> auditPass(@PathVariable int id) {
        Employee employee = employeeService.getById(id);
        if (Objects.isNull(employee)) {
            return Result.fail("员工不存在");
        }
        return Result.success(employeeService.auditPass(id));
    }

    @ApiOperation("审核拒绝")
    @PostMapping("/auditReject")
    public Result<Boolean> auditReject(@RequestBody AuditRejectDTO auditRejectDTO) {
        Employee employee = employeeService.getById(auditRejectDTO.getId());
        if (Objects.isNull(employee)) {
            return Result.fail("员工不存在");
        }
        return Result.success(employeeService.auditReject(auditRejectDTO));
    }

    @ApiOperation("启用")
    @PostMapping("/enable/{id}")
    public Result<Boolean> enable(@PathVariable int id) {
        Employee employee = employeeService.getById(id);
        if (Objects.isNull(employee)) {
            return Result.fail("员工不存在");
        }
        return Result.success(employeeService.enable(id));
    }


    @ApiOperation("查看在职员工详情")
    @PostMapping("/detail/{id}")
    public Result<EmployeeInfoVO> detail(@PathVariable int id) {
        EmployeeInfoVO employeeInfoVO = new EmployeeInfoVO();
        Employee employee = employeeService.getById(id);
        if (Objects.isNull(employee)) {
            return Result.fail("员工不存在");
        }
        BeanUtils.copyProperties(employee, employeeInfoVO);
        QueryWrapper<EmployeeDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", employee.getId());
        EmployeeDetails one = employeeDetailsService.getOne(queryWrapper);
        if (Objects.nonNull(one)) {
            if (StringUtils.isNotEmpty(one.getIdCardFrontUrl())) {
                one.setIdCardFrontUrl("https://hzjysb.oss-cn-hangzhou.aliyuncs.com/" + one.getIdCardFrontUrl());
            }
            if (StringUtils.isNotEmpty(one.getIdCardBackendUrl())) {
                one.setIdCardBackendUrl("https://hzjysb.oss-cn-hangzhou.aliyuncs.com/" + one.getIdCardBackendUrl());
            }
            employeeInfoVO.setEmployeeDetails(one);
        }

        return Result.success(employeeInfoVO);
    }

    @ApiOperation("停用")
    @PostMapping("/disable/{id}")
    public Result<Boolean> disable(@PathVariable int id) {

        Employee employee = employeeService.getById(id);
        if (Objects.isNull(employee)) {
            return Result.fail("员工不存在");
        }
        return Result.success(employeeService.disable(id));
    }

    @ApiOperation("离职")
    @PostMapping("/resign/{id}")
    public Result<Boolean> resign(@PathVariable int id) {

        Employee employee = employeeService.getById(id);
        if (Objects.isNull(employee)) {
            return Result.fail("员工不存在");
        }
        return Result.success(employeeService.resign(id));
    }

    @ApiOperation("入职")
    @PostMapping("/incumbency/{id}")
    public Result<Boolean> incumbency(@PathVariable int id) {

        Employee employee = employeeService.getById(id);
        if (Objects.isNull(employee)) {
            return Result.fail("员工不存在");
        }
        return Result.success(employeeService.incumbency(id));
    }


    @ApiOperation("积分清零")
    @PostMapping("/clearPoints/{id}")
    public Result<Boolean> clearPoints(@PathVariable int id) {

        Employee employee = employeeService.getById(id);
        if (Objects.isNull(employee)) {
            return Result.fail("员工不存在");
        }
        return Result.success(employeeService.clearPoints(id));
    }


    @ApiOperation("批量启用")
    @PostMapping("/batchEnable")
    public Result<Boolean> batchEnable(@RequestBody List<Integer> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Result.fail("传入的ID为空");
        }

        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", ids);
        updateWrapper.set("status", Boolean.TRUE);
        updateWrapper.set("enable_date", LocalDateTime.now());
        updateWrapper.set("disable_date", null);
        employeeService.update(updateWrapper);

        return Result.success(Boolean.TRUE);
    }


    @ApiOperation("批量停用")
    @PostMapping("/batchDisable")
    public Result<Boolean> batchDisable(@RequestBody List<Integer> ids) {

        if (CollectionUtil.isEmpty(ids)) {
            return Result.fail("传入的ID为空");
        }
        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", ids);
        updateWrapper.set("status", Boolean.FALSE);
        updateWrapper.set("enable_date", null);
        updateWrapper.set("disable_date", LocalDateTime.now());
        employeeService.update(updateWrapper);

        return Result.success(Boolean.TRUE);
    }


    @ApiOperation("批量积分清零")
    @PostMapping("/batchClearPoint")
    public Result<Boolean> batchClearPoint(@RequestBody List<Integer> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Result.fail("传入的ID为空");
        }

        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", ids);
        updateWrapper.set("points", 0);
        updateWrapper.set("level", 0);
        employeeService.update(updateWrapper);

        return Result.success(Boolean.TRUE);
    }


    @ApiOperation("批量审核通过")
    @PostMapping("/batchAuditPass")
    public Result<Boolean> batchAuditPass(@RequestBody List<Integer> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Result.fail("传入的ID为空");
        }

        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", ids);
        updateWrapper.in("audit_status", Collections.singleton(AuditStatus.WAIT_AUDIT));
        updateWrapper.set("audit_status", AuditStatus.PASS);
        updateWrapper.set("on_board", Boolean.TRUE);
        updateWrapper.set("status", Boolean.TRUE);
        updateWrapper.set("onboarding_date", LocalDateTime.now());
        updateWrapper.set("enable_date", LocalDateTime.now());
        updateWrapper.set("disable_date", null);
        employeeService.update(updateWrapper);

        return Result.success(Boolean.TRUE);
    }


    @ApiOperation("批量审核拒绝")
    @PostMapping("/batchAuditReject")
    public Result<Boolean> batchAuditReject(@RequestBody List<Integer> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Result.fail("传入的ID为空");
        }

        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", ids);
        updateWrapper.in("audit_status", Collections.singleton(AuditStatus.WAIT_AUDIT));
        updateWrapper.set("audit_status", AuditStatus.REJECT);
        employeeService.update(updateWrapper);

        return Result.success(Boolean.TRUE);
    }

    @ApiOperation("员工每月打卡统计")
    @PostMapping("employeePunchMonth")
    public Result<Page<EmployeePunchMonth>> employeePunchMonth(@RequestBody EmployeePunchMonthQueryQO monthQueryQO) {
        QueryWrapper<EmployeePunchMonth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", monthQueryQO.getEmployeeId());
        Page<EmployeePunchMonth> page1 = new Page<>();
        page1.setPages(monthQueryQO.getPage());
        page1.setSize(monthQueryQO.getPageSize());
        Page<EmployeePunchMonth> page = punchMonthService.page(page1, queryWrapper);
        return Result.success(page);
    }


    @PostMapping("export")
    @ApiOperation("员工导出")
    public void export(@RequestBody EmployeeQueryQO employeeQueryQO,HttpServletResponse response) {
        //excel标题
        String[] title = {"姓名", "手机号", "注册时间", "等级", "积分", "当月累计工时", "启用状态",  "启用时间", "停用时间"};
        //excel文件名
        String fileName = "员工列表" + System.currentTimeMillis() + ".xls";
        //sheet名
        String sheetName = "员工列表";
        employeeQueryQO.setPage(1);
        employeeQueryQO.setPageSize(1000);
        Page<Employee> employeePage = employeeService.listEmployee(employeeQueryQO);

        List<Employee> list = employeePage.getRecords();
        String[][] content = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[10];
            Employee resultInfo = list.get(i);
            content[i][0] = resultInfo.getName();
            content[i][1] = resultInfo.getPhone();
            content[i][2] = FormatUtils.time(resultInfo.getCreateTime());
            content[i][3] = resultInfo.getLevel() + "";
            content[i][4] = resultInfo.getPoints() + "";
            content[i][5] = resultInfo.getCurrentMonthTime() + "";
            content[i][6] = resultInfo.getStatus() ? "启用" : "停用";
            content[i][7] = FormatUtils.time(resultInfo.getEnableDate());
            content[i][8] = FormatUtils.time(resultInfo.getDisableDate());
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            ExportSetHeaderUtil.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}

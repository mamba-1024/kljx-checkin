package com.hzjy.hzjycheckIn.controller.backend;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.config.ExcelUtil;
import com.hzjy.hzjycheckIn.config.FormatUtils;
import com.hzjy.hzjycheckIn.dto.EmployeeMonthTotalTO;
import com.hzjy.hzjycheckIn.dto.MonthExportDTO;
import com.hzjy.hzjycheckIn.dto.backend.BackendAttendanceRecordVO;
import com.hzjy.hzjycheckIn.dto.backend.EmployeeAttendanceVO;
import com.hzjy.hzjycheckIn.dto.query.EmployeeAttendanceQO;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.entity.EmployeeAttendance;
import com.hzjy.hzjycheckIn.service.EmployeeAttendanceService;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import com.hzjy.hzjycheckIn.util.ExportSetHeaderUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backend/attendance")
@ApiOperation("考勤日报")
@Slf4j
public class BackendAttendanceController {

    @Autowired
    private EmployeeAttendanceService attendanceService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("list")
    @ApiOperation("查看考勤日报列表")
    public Result<Page<EmployeeAttendanceVO>> list(@RequestBody EmployeeAttendanceQO employeeAttendanceQO) {
        new QueryWrapper<>();
        Page<EmployeeAttendanceVO> page = new Page<>();
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(employeeAttendanceQO.getName()), "name", employeeAttendanceQO.getName());
        List<Employee> employees = employeeService.list(queryWrapper);
        if (CollectionUtil.isEmpty(employees)) {
            return Result.success(page);
        }
        List<Integer> employeeIds = employees.stream().map(Employee::getId).collect(Collectors.toList());

        Map<Integer, Employee> employeeMap = employees.stream().collect(Collectors.toMap(o -> o.getId(), Function.identity()));

        QueryWrapper<EmployeeAttendance> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("employee_id", employeeIds);
        queryWrapper1.orderByDesc("create_at");
        queryWrapper1.between(Objects.nonNull(employeeAttendanceQO.getStartTime()) || Objects.nonNull(employeeAttendanceQO.getEndTime()), "punch_date", employeeAttendanceQO.getStartTime(), employeeAttendanceQO.getEndTime());

        Page<EmployeeAttendance> attendancePage = attendanceService.page(new Page<>(employeeAttendanceQO.getPage(), employeeAttendanceQO.getPageSize()), queryWrapper1);
        List<EmployeeAttendance> attendanceList = attendancePage.getRecords();
        List<EmployeeAttendanceVO> records = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(attendanceList)) {
            for (EmployeeAttendance employeeAttendance : attendanceList) {
                EmployeeAttendanceVO employeeAttendanceVO = new EmployeeAttendanceVO();
                employeeAttendanceVO.setPunchDate(employeeAttendance.getPunchDate().toString());
                Employee employee = employeeMap.get(employeeAttendance.getEmployeeId());
                employeeAttendanceVO.setName(employee.getName());
                employeeAttendanceVO.setStatus(employee.getStatus());
                employeeAttendanceVO.setCommonRecord(new BackendAttendanceRecordVO(employeeAttendance.getCommonPunchStart(), employeeAttendance.getCommonPunchEnd(), employeeAttendance.getCommonPunchDuration(), employeeAttendance.getCommonWorkShiftId()));
                employeeAttendanceVO.setOverRecord(new BackendAttendanceRecordVO(employeeAttendance.getOverPunchStart(), employeeAttendance.getOverPunchEnd(), employeeAttendance.getOverPunchDuration(), employeeAttendance.getOverWorkShiftId()));
                records.add(employeeAttendanceVO);
            }
        }
        page.setRecords(records);
        page.setPages(attendancePage.getPages());
        page.setTotal(attendancePage.getTotal());
        page.setCurrent(attendancePage.getCurrent());
        return Result.success(page);
    }


    @PostMapping("export/daily")
    @ApiOperation("导出考勤日报列表")
    public void exportDaily(@RequestBody EmployeeAttendanceQO employeeAttendanceQO, HttpServletResponse response) {

        //excel标题
        String[] title = {"时间", "姓名", "员工状态", "普通打卡上班打卡时间", "普通打卡下班打卡时间", "加班打卡上班打卡时间", "加班打卡下班打卡时间", "普通打卡上班时长", "加班打卡上班时长"};

        //excel文件名 - 优化文件名称格式
        String currentTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "员工考勤明细_" + currentTime + ".xls";

        //sheet名
        String sheetName = "员工考勤明细";
        employeeAttendanceQO.setPage(1);
        employeeAttendanceQO.setPageSize(1000);
        List<EmployeeAttendanceVO> list = list(employeeAttendanceQO).getData().getRecords();
        String[][] content = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[10];
            EmployeeAttendanceVO resultInfo = list.get(i);
            content[i][0] = resultInfo.getPunchDate();
            content[i][1] = resultInfo.getName();
            content[i][2] = resultInfo.getStatus() ? "启用" : "停用";
            content[i][3] = FormatUtils.time(resultInfo.getCommonRecord().getPunchUpTime());
            content[i][4] = FormatUtils.time(resultInfo.getCommonRecord().getPunchDownTime());
            content[i][5] = FormatUtils.time(resultInfo.getOverRecord().getPunchUpTime());
            content[i][6] = FormatUtils.time(resultInfo.getOverRecord().getPunchDownTime());
            BigDecimal hours1 = resultInfo.getCommonRecord().getHours();
            content[i][7] = Objects.isNull(hours1) ? "0.0" : hours1.toString();
            BigDecimal hours = resultInfo.getOverRecord().getHours();
            content[i][8] = Objects.isNull(hours) ? "0.0" : hours.toString();
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


    @PostMapping("export/month")
    @ApiOperation("导出考勤月报")
    public void exportMonth(@RequestBody MonthExportDTO monthExportDTO, HttpServletResponse response) {
        String[] title = {"时间", "姓名", "员工状态", "普通班次打卡总时长(小时)", "加班班次打卡总时长(小时)", "打卡总时长(小时)"};
        String currentTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "员工考勤月报_" + currentTime + ".xls";
        //sheet名
        String sheetName = "员工考勤明细";
        List<EmployeeMonthTotalTO> list = employeeService.queryMonth(monthExportDTO);
        if (CollectionUtil.isEmpty(list)) {
            //创建HSSFWorkbook
            HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, null, null);

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
            return;
        }

        List<Long> employeeIds = list.stream().map(EmployeeMonthTotalTO::getEmployeeId).collect(Collectors.toList());
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", employeeIds);
        List<Employee> employees = employeeService.list(queryWrapper);
        Map<Integer, Employee> employeeMap = employees.stream().collect(Collectors.toMap(Employee::getId, Function.identity()));

        List<EmployeeMonthTotalTO> result = list.stream().filter(o -> employeeMap.keySet().contains(o.getEmployeeId().intValue())).collect(Collectors.toList());

        String[][] content = new String[result.size()][];

        for (int i = 0; i < result.size(); i++) {
            content[i] = new String[10];
            EmployeeMonthTotalTO resultInfo = result.get(i);
            content[i][0] = monthExportDTO.getMonth();
            Employee employee = employeeMap.get(resultInfo.getEmployeeId().intValue());
            if (Objects.isNull(employee)) {
                continue;
            }
            content[i][1] = employee.getName();
            content[i][2] = employee.getStatus() ? "启用" : "停用";
            content[i][3] = resultInfo.getTotalCommonDuration().toString();
            content[i][4] = resultInfo.getTotalOverDuration().toString();
            content[i][5] = resultInfo.getTotalDuration().toString();
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

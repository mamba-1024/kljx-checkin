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
import com.hzjy.hzjycheckIn.entity.WorkShift;
import com.hzjy.hzjycheckIn.service.WorkShiftService;
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

    @Autowired
    private WorkShiftService workShiftService;

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
            // 预加载所有班次，避免循环内多次查询
            List<WorkShift> allShifts = workShiftService.list();
            Map<Integer, WorkShift> shiftMap = allShifts.stream().collect(Collectors.toMap(WorkShift::getId, Function.identity(), (a, b) -> a));
            for (EmployeeAttendance employeeAttendance : attendanceList) {
                EmployeeAttendanceVO employeeAttendanceVO = new EmployeeAttendanceVO();
                employeeAttendanceVO.setPunchDate(employeeAttendance.getPunchDate().toString());
                Employee employee = employeeMap.get(employeeAttendance.getEmployeeId());
                employeeAttendanceVO.setName(employee.getName());
                employeeAttendanceVO.setStatus(employee.getStatus());
                BackendAttendanceRecordVO common = new BackendAttendanceRecordVO(employeeAttendance.getCommonPunchStart(), employeeAttendance.getCommonPunchEnd(), employeeAttendance.getCommonPunchDuration(), employeeAttendance.getCommonWorkShiftId());
                WorkShift commonShift = shiftMap.get(employeeAttendance.getCommonWorkShiftId());
                if (commonShift != null) {
                    common.setShiftName(commonShift.getShiftName());
                }
                // 使用 employee_attendance 实际四次打卡时间
                common.setAmStartTime(employeeAttendance.getAmStartTime());
                common.setAmEndTime(employeeAttendance.getAmEndTime());
                common.setPmStartTime(employeeAttendance.getPmStartTime());
                common.setPmEndTime(employeeAttendance.getPmEndTime());
                employeeAttendanceVO.setCommonRecord(common);

                BackendAttendanceRecordVO over = new BackendAttendanceRecordVO(employeeAttendance.getOverPunchStart(), employeeAttendance.getOverPunchEnd(), employeeAttendance.getOverPunchDuration(), employeeAttendance.getOverWorkShiftId());
                WorkShift overShift = shiftMap.get(employeeAttendance.getOverWorkShiftId());
                if (overShift != null) {
                    over.setShiftName(overShift.getShiftName());
                }
                // 加班记录不区分 am/pm，导出时保持空值
                employeeAttendanceVO.setOverRecord(over);
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
        String[] title = {"时间", "姓名", "员工状态",
                "普通打卡上班打卡时间", "普通打卡下班打卡时间",
                "加班打卡上班打卡时间", "加班打卡下班打卡时间",
                "普通打卡上班时长", "加班打卡上班时长",
                "普通-上午上班", "普通-上午下班", "普通-下午上班", "普通-下午下班",
                "加班-上午上班", "加班-上午下班", "加班-下午上班", "加班-下午下班"};

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
            content[i] = new String[17];
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

            // 班次时间段展示（普通/加班，各上午/下午）
            content[i][9] = FormatUtils.time(resultInfo.getCommonRecord().getAmStartTime());
            content[i][10] = FormatUtils.time(resultInfo.getCommonRecord().getAmEndTime());
            content[i][11] = FormatUtils.time(resultInfo.getCommonRecord().getPmStartTime());
            content[i][12] = FormatUtils.time(resultInfo.getCommonRecord().getPmEndTime());

            content[i][13] = FormatUtils.time(resultInfo.getOverRecord().getAmStartTime());
            content[i][14] = FormatUtils.time(resultInfo.getOverRecord().getAmEndTime());
            content[i][15] = FormatUtils.time(resultInfo.getOverRecord().getPmStartTime());
            content[i][16] = FormatUtils.time(resultInfo.getOverRecord().getPmEndTime());
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
        String[] title = {"时间", "姓名", "员工状态", "普通班次打卡总时长(小时)", "加班班次打卡总时长(小时)", "打卡总时长(小时)",
                "普通-上午上班", "普通-上午下班", "普通-下午上班", "普通-下午下班",
                "加班-上午上班", "加班-上午下班", "加班-下午上班", "加班-下午下班"};
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
            content[i] = new String[14];
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

            // 取该月份对应的夏/冬令时代表班次，展示时间段
            java.time.LocalDate anyDayOfMonth = java.time.LocalDate.parse(monthExportDTO.getMonth() + "-01");
            boolean isSummer = anyDayOfMonth.isAfter(java.time.LocalDate.of(anyDayOfMonth.getYear(), java.time.Month.MAY, 31)) &&
                    anyDayOfMonth.isBefore(java.time.LocalDate.of(anyDayOfMonth.getYear(), java.time.Month.OCTOBER, 1));
            List<WorkShift> allShifts = workShiftService.list();
            List<WorkShift> seasonalShifts = allShifts.stream().filter(s -> s.isSummer() == isSummer).collect(java.util.stream.Collectors.toList());
            WorkShift commonShift = seasonalShifts.stream().filter(s -> s.getShiftName() != null && s.getShiftName().contains("白班")).findFirst().orElse(null);
            WorkShift overShift = seasonalShifts.stream().filter(s -> s.getShiftName() != null && s.getShiftName().contains("加班")).findFirst().orElse(null);

            // 使用当月第一天合并日期，导出与 punchUpTime 一致格式
            java.time.LocalDate monthDate = java.time.LocalDate.parse(monthExportDTO.getMonth() + "-01");
            content[i][6] = commonShift == null ? "" : FormatUtils.time(java.time.LocalDateTime.of(monthDate, commonShift.getStartTime()));
            content[i][7] = commonShift == null ? "" : FormatUtils.time(java.time.LocalDateTime.of(monthDate, commonShift.getEndTime()));
            content[i][8] = commonShift == null ? "" : FormatUtils.time(java.time.LocalDateTime.of(monthDate, commonShift.getStartTime()));
            content[i][9] = commonShift == null ? "" : FormatUtils.time(java.time.LocalDateTime.of(monthDate, commonShift.getEndTime()));

            content[i][10] = overShift == null ? "" : FormatUtils.time(java.time.LocalDateTime.of(monthDate, overShift.getStartTime()));
            content[i][11] = overShift == null ? "" : FormatUtils.time(java.time.LocalDateTime.of(monthDate, overShift.getEndTime()));
            content[i][12] = overShift == null ? "" : FormatUtils.time(java.time.LocalDateTime.of(monthDate, overShift.getStartTime()));
            content[i][13] = overShift == null ? "" : FormatUtils.time(java.time.LocalDateTime.of(monthDate, overShift.getEndTime()));
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


    private java.time.LocalDateTime mergeDateTime(java.time.LocalDateTime dayStart, java.time.LocalTime time) {
        if (time == null) {
            return null;
        }
        return java.time.LocalDateTime.of(dayStart.toLocalDate(), time);
    }

}

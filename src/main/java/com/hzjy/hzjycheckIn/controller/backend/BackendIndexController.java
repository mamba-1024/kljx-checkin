package com.hzjy.hzjycheckIn.controller.backend;

import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.dto.backend.IndexStaticVO;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.entity.HzjyConfig;
import com.hzjy.hzjycheckIn.service.EmployeeAttendanceService;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import com.hzjy.hzjycheckIn.service.HzjyConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("backend/index")
@ApiOperation(value = "首页", tags = "backend")
public class BackendIndexController {

    @Autowired
    private HzjyConfigService hzjyConfigService;

    @Autowired
    private EmployeeAttendanceService employeeAttendanceService;

    @Autowired
    private EmployeeService employeeService;

    @ApiOperation("更改主图")
    @PostMapping("/setUrl")
    public Result<Boolean> mainUrlConfig(@RequestBody List<String> mainUrlList) {
        String urlListStr = mainUrlList.toString();
        HzjyConfig entity = new HzjyConfig();
        entity.setId(1);
        entity.setMainUrl(urlListStr);
        hzjyConfigService.updateById(entity);
        return Result.success(Boolean.TRUE);
    }

    @ApiOperation("更改主图")
    @GetMapping("/getUrl")
    public Result<List<String>> getUrl() {
        List<HzjyConfig> list = hzjyConfigService.list();
        List<String> result = new ArrayList<>();
        HzjyConfig hzjyConfig = list.get(0);
        String mainUrl = hzjyConfig.getMainUrl();
        String filenames = mainUrl.substring(1, mainUrl.length() - 1);
// 使用逗号分割字符串
        String[] filenameArray = filenames.split(", ");
        for (String urls : filenameArray) {
            result.add("https://hzjysb.oss-cn-hangzhou.aliyuncs.com/" + urls);
        }
        return Result.success(result);
    }


    @ApiOperation("首页数据")
    @GetMapping("/static")
    public Result<IndexStaticVO> indexStaticVOResult() {
        IndexStaticVO data = new IndexStaticVO();

        Map<String, Long> employeeCount = new HashMap<>();
        Map<String, Long> employeeCounts = employeeService.selectLastDayCount();
        employeeCount.put("enableCount", employeeCounts.get("yesterday_enabled_count"));
        employeeCount.put("disableCount", employeeCounts.get("yesterday_disabled_count"));
        employeeCount.put("lastDayAdd", employeeCounts.get("yesterday_new_count"));

        data.setEmployeeCount(employeeCount);

        Map<String, BigDecimal> employeeWorkCount = new HashMap<>();
        Map<String, BigDecimal> employeeWorkCountFromDB = employeeAttendanceService.selectLastDayWorkCount();
        employeeWorkCount.put("昨日正常上班", employeeWorkCountFromDB.get("totalCommon"));
        employeeWorkCount.put("昨日加班人数", employeeWorkCountFromDB.get("totalOver"));
        data.setEmployeeWorkCount(employeeWorkCount);


//        @ApiModelProperty("等级分布")
        Map<String, Long> level = new LinkedHashMap<>();
        level.put("LV0", 0L);
        level.put("LV1", 0L);
        level.put("LV2", 0L);
        level.put("LV3", 0L);
        List<Map<String, Object>> levelDistribution = employeeService.queryLevelCount();

        for (Map<String, Object> map : levelDistribution) {
            level.put((String) map.get("level_display"), (Long) map.get("count"));
        }

        data.setLevelDistribution(level);

//        @ApiModelProperty("员工积分排名前十")
        List<Employee> employees = employeeService.selectBig10();

        data.setEmployees(employees);

        data.setWaitAuditCount(employeeService.countWaitAudit());

        return Result.success(data);
    }


}

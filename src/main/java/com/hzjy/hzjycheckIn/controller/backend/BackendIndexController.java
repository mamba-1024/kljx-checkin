package com.hzjy.hzjycheckIn.controller.backend;

import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.dto.backend.IndexStaticVO;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.entity.HzjyConfig;
import com.hzjy.hzjycheckIn.service.EmployeeAttendanceService;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import com.hzjy.hzjycheckIn.service.HzjyConfigService;
import com.hzjy.hzjycheckIn.util.FileUrlUtil;
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

    @Autowired
    private FileUrlUtil fileUrlUtil;

    @ApiOperation("更改主图")
    @PostMapping("/setUrl")
    public Result<Boolean> mainUrlConfig(@RequestBody List<String> mainUrlList) {
        String urlListStr = mainUrlList.toString();
        
        // 先尝试获取现有配置
        HzjyConfig existingConfig = hzjyConfigService.getById(1);
        
        if (existingConfig != null) {
            // 如果存在，则更新
            existingConfig.setMainUrl(urlListStr);
            boolean updateResult = hzjyConfigService.updateById(existingConfig);
            if (!updateResult) {
                return Result.fail("更新主图配置失败");
            }
        } else {
            // 如果不存在，则创建新记录
            HzjyConfig newConfig = new HzjyConfig();
            newConfig.setId(1);
            newConfig.setMainUrl(urlListStr);
            boolean saveResult = hzjyConfigService.save(newConfig);
            if (!saveResult) {
                return Result.fail("保存主图配置失败");
            }
        }
        
        return Result.success(Boolean.TRUE);
    }

    /**
     * 获取主图URL列表
     * 
     * <p>从系统配置表中获取主图配置信息，解析存储的URL列表并拼接完整的访问地址</p>
     * 
     * <p>数据流程：</p>
     * <ul>
     *   <li>从hzjy_config表获取主图配置</li>
     *   <li>解析存储的JSON数组字符串</li>
     *   <li>去除文件名中的双引号</li>
     *   <li>拼接本地文件访问地址</li>
     * </ul>
     * 
     * @return 包含完整访问地址的主图URL列表
     */
    @ApiOperation("主图列表")
    @GetMapping("/getUrl")
    public Result<List<String>> getUrl() {
        // 获取所有系统配置记录
        List<HzjyConfig> list = hzjyConfigService.list();
        List<String> result = new ArrayList<>();
        
        // 检查配置列表是否为空
        if (list == null || list.isEmpty()) {
            // 如果没有配置数据，返回空列表
            return Result.success(result);
        }
        
        // 获取第一条配置记录（通常只有一条配置）
        HzjyConfig hzjyConfig = list.get(0);
        String mainUrl = hzjyConfig.getMainUrl();
        
        // 检查主图URL配置是否为空
        if (mainUrl == null || mainUrl.trim().isEmpty() || mainUrl.equals("[]")) {
            return Result.success(result);
        }
        
        // 去除JSON数组字符串的首尾方括号
        String filenames = mainUrl.substring(1, mainUrl.length() - 1);
        
        // 检查处理后的字符串是否为空
        if (filenames.trim().isEmpty()) {
            return Result.success(result);
        }
        
        // 使用逗号分割字符串，得到文件名数组
        String[] filenameArray = filenames.split(", ");
        
        // 遍历文件名数组，拼接完整的访问地址
        for (String urls : filenameArray) {
            // 去除文件名中的双引号（因为List.toString()会为元素添加引号）
            String cleanUrl = urls.replace("\"", "").trim();
            if (!cleanUrl.isEmpty()) {
                // 使用文件URL工具类获取完整访问地址
                result.add(fileUrlUtil.getFileUrl(cleanUrl));
            }
        }
        
        return Result.success(result);
    }


    @ApiOperation("首页数据")
    @GetMapping("/static")
    public Result<IndexStaticVO> indexStaticVOResult() {
        IndexStaticVO data = new IndexStaticVO();

        Map<String, Long> employeeCount = new HashMap<>();
        Map<String, Long> employeeCounts = employeeService.selectLastDayCount();
        
        // 安全地获取并转换计数值
        employeeCount.put("enableCount", getLongValue(employeeCounts.get("yesterday_enabled_count")));
        employeeCount.put("disableCount", getLongValue(employeeCounts.get("yesterday_disabled_count")));
        employeeCount.put("lastDayAdd", getLongValue(employeeCounts.get("yesterday_new_count")));

        data.setEmployeeCount(employeeCount);

        Map<String, BigDecimal> employeeWorkCount = new HashMap<>();
        Map<String, BigDecimal> employeeWorkCountFromDB = employeeAttendanceService.selectLastDayWorkCount();
        employeeWorkCount.put("昨日正常上班", getBigDecimalValue(employeeWorkCountFromDB.get("totalCommon")));
        employeeWorkCount.put("昨日加班人数", getBigDecimalValue(employeeWorkCountFromDB.get("totalOver")));
        data.setEmployeeWorkCount(employeeWorkCount);


        //@ApiModelProperty("等级分布")
        Map<String, Long> level = new LinkedHashMap<>();
        level.put("LV0", 0L);
        level.put("LV1", 0L);
        level.put("LV2", 0L);
        level.put("LV3", 0L);
        List<Map<String, Object>> levelDistribution = employeeService.queryLevelCount();

        for (Map<String, Object> map : levelDistribution) {
            Long count = getLongValue(map.get("count"));
            level.put((String) map.get("level_display"), count);
        }

        data.setLevelDistribution(level);

//        @ApiModelProperty("员工积分排名前十")
        List<Employee> employees = employeeService.selectBig10();

        data.setEmployees(employees);

        data.setWaitAuditCount(employeeService.countWaitAudit());

        return Result.success(data);
    }

    /**
     * 安全地将对象转换为Long类型
     * 
     * @param value 要转换的值
     * @return 转换后的Long值，如果转换失败返回0L
     */
    private Long getLongValue(Object value) {
        if (value == null) {
            return 0L;
        }
        try {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            } else if (value instanceof String) {
                return Long.valueOf((String) value);
            } else {
                return Long.valueOf(value.toString());
            }
        } catch (Exception e) {
            // 如果转换失败，返回0
            return 0L;
        }
    }

    /**
     * 安全地将对象转换为BigDecimal类型
     * 
     * @param value 要转换的值
     * @return 转换后的BigDecimal值，如果转换失败返回BigDecimal.ZERO
     */
    private BigDecimal getBigDecimalValue(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        try {
            if (value instanceof BigDecimal) {
                return (BigDecimal) value;
            } else if (value instanceof Number) {
                return BigDecimal.valueOf(((Number) value).doubleValue());
            } else if (value instanceof String) {
                return new BigDecimal((String) value);
            } else {
                return new BigDecimal(value.toString());
            }
        } catch (Exception e) {
            // 如果转换失败，返回0
            return BigDecimal.ZERO;
        }
    }

}

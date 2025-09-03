package com.hzjy.hzjycheckIn.controller.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.config.BankVerifyUtil;
import com.hzjy.hzjycheckIn.config.CacheManager;
import com.hzjy.hzjycheckIn.config.EmployeeContext;
import com.hzjy.hzjycheckIn.dto.*;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.hzjy.hzjycheckIn.entity.EmployeeDetails;
import com.hzjy.hzjycheckIn.enums.AuditStatus;
import com.hzjy.hzjycheckIn.service.EmployeeDetailsService;
import com.hzjy.hzjycheckIn.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;
    @Resource
    private EmployeeDetailsService detailsService;

    @Resource
    private BankVerifyUtil bankVerifyUtil;

    @Autowired
    private CacheManager cacheManager;

    @ApiOperation("获取用户信息")
    @GetMapping("/getUserInfo")
    public Result<EmployeeInfoDTO> getUserInfo() {
        Employee employee = EmployeeContext.getEmployee();
        //查询用户信息
        Employee dbEmployee = employeeService.getById(employee.getId());
        //查询等级信息等等
        EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO();
        employeeInfoDTO.setUserName(dbEmployee.getName());
        employeeInfoDTO.setNickname(dbEmployee.getNickname());
        // phone 兼容 null 或非 11 位
        String phone = dbEmployee.getPhone();
        String phoneMasked = phone;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(phone) && phone.length() == 11) {
            phoneMasked = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        employeeInfoDTO.setPhoneNumber(phoneMasked);
        employeeInfoDTO.setVerified(dbEmployee.getIsAuthenticated());
        employeeInfoDTO.setLevel("" + employee.getLevel());
        employeeInfoDTO.setCheckInTime(employee.getCurrentMonthTime() + "");
        employeeInfoDTO.setAccumulatedPoints(employee.getPoints());
        employeeInfoDTO.setAvatarUrl(dbEmployee.getAvatar());
        employeeInfoDTO.setAuditStatus(dbEmployee.getAuditStatus());
        return Result.success(employeeInfoDTO);
    }

    @ApiOperation("更新员工信息，头像昵称")
    @PostMapping("update")
    public Result<Boolean> updateEmployeeInfo(@RequestBody EmployeeUpdateDTO employee) {
        Employee employee1 = EmployeeContext.getEmployee();
        Employee updateEmployee = new Employee();
        updateEmployee.setId(employee1.getId());
        updateEmployee.setNickname(employee.getNickname());
        updateEmployee.setAvatar(employee.getAvatarUrl());
        employeeService.updateById(updateEmployee);

        return Result.success(Boolean.TRUE);
    }

    @ApiOperation("实名认证")
    @PostMapping("verifyUser")
    public Result<Boolean> verifyUser(@RequestBody VerifiedInfo verifiedInfo) {
        Employee employee = EmployeeContext.getEmployee();
        String key = "VALIDATE_" + employee.getId();
        Integer o = cacheManager.get(key);
        if (Objects.isNull(o)) {
            cacheManager.put(key, 3);
            o = 3;
        }
        if (o < 1) {
            return Result.fail("当日实名认证次数已用完，请明日再试");
        }
        // 暂时跳过银行四要素验证接口 TODO 银行验证接口暂时不可用，后续恢复
        // String verify = bankVerifyUtil.verify(verifiedInfo.getBankCard(), verifiedInfo.getIdCard(), verifiedInfo.getBankReservePhone(), verifiedInfo.getUserName());
        // JSONObject jsonObject = JSONObject.parseObject(verify);
        // Integer code = jsonObject.getInteger("code");
        // if (code != 200) {
        //     return Result.fail(jsonObject.getString("msg"));
        // }
        // JSONObject data = jsonObject.getJSONObject("data");
        // String result = data.getString("result");
        // if ("0".equals(result)) {
        
        // 暂时直接通过验证，跳过银行验证步骤
        QueryWrapper<EmployeeDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", employee.getId());
        EmployeeDetails dbEmployeeDetails = detailsService.getOne(queryWrapper);
        //新增detail数据
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setBankReservePhone(verifiedInfo.getBankReservePhone());
        employeeDetails.setEmployeeId(employee.getId());
        employeeDetails.setBankCard(verifiedInfo.getBankCard());
        employeeDetails.setIdCard(verifiedInfo.getIdCard());
        employeeDetails.setName(verifiedInfo.getUserName());
        employeeDetails.setIdCardFrontUrl(verifiedInfo.getFrontIdCardUrl());
        employeeDetails.setIdCardBackendUrl(verifiedInfo.getBackendIdCardUrl());
        employeeDetails.setBankBranch("招商银行宜兴分行"); // 统一设置银行名称
        if (Objects.nonNull(dbEmployeeDetails)) {
            employeeDetails.setId(dbEmployeeDetails.getId());
        }
        detailsService.saveOrUpdate(employeeDetails);

        employee.setIsAuthenticated(Boolean.TRUE);
        employee.setAuditStatus(AuditStatus.WAIT_AUDIT);
        employee.setName(verifiedInfo.getUserName());
        employee.setNickname(verifiedInfo.getUserName());
        employee.setRejectReason(null);

        employeeService.updateById(employee);
        return Result.success(Boolean.TRUE);

    }


    @ApiOperation("实名认证用户详情")
    @GetMapping("userDetail")
    public Result<EmployeeDetailDTO> userDetail() {
        Employee employee = EmployeeContext.getEmployee();

        EmployeeDetailDTO employeeDetailDTO = new EmployeeDetailDTO();
        employeeDetailDTO.setIsAuthenticated(employee.getIsAuthenticated());
        employeeDetailDTO.setAuditStatus(employee.getAuditStatus());
        employeeDetailDTO.setRejectReason(employee.getRejectReason());

        QueryWrapper<EmployeeDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", employee.getId());
        EmployeeDetails details = detailsService.getOne(queryWrapper);
        if (Objects.nonNull(details)) {
            employeeDetailDTO.setName(details.getName());
            String bankCard = details.getBankCard();
            if (StringUtils.isNotEmpty(bankCard) & bankCard.length() > 4) {
                String hiddenBankCard = "**** **** **** " + bankCard.substring(bankCard.length() - 4);
                employeeDetailDTO.setBankCard(hiddenBankCard);
            }
            employeeDetailDTO.setBankName(details.getBankBranch());
            String idNumber = details.getIdCard();
            String hiddenIdNumber = idNumber.replaceAll("(?<=\\d{4})\\d(?=\\d{4})", "*");
            employeeDetailDTO.setIdCard(hiddenIdNumber);
        }

        //更新详情
        return Result.success(employeeDetailDTO);

    }


    @ApiOperation("刷新token")
    @GetMapping("refreshToken")
    public Result<String> refreshToken() {
        return Result.success("test");
    }

    @ApiOperation("获取等级")
    @GetMapping("levelInfo")
    public Result<LevelInfoDTO> levelInfo() {
        Employee employee = EmployeeContext.getEmployee();
        LevelInfoDTO levelInfoDTO = new LevelInfoDTO();
        levelInfoDTO.setRemark("到达3星可以获得年终奖，积分每年会清零，次年重新计算");
        List<LevelInfoDTO.CheckInLevel> levelList = new ArrayList<>();
        LevelInfoDTO.CheckInLevel checkInLevel0 = new LevelInfoDTO.CheckInLevel();
        checkInLevel0.setName("0星");
        checkInLevel0.setDesc("测试000");
        levelList.add(checkInLevel0);

        LevelInfoDTO.CheckInLevel checkInLevel1 = new LevelInfoDTO.CheckInLevel();
        checkInLevel1.setName("1星");
        checkInLevel1.setDesc("测试111");
        levelList.add(checkInLevel1);

        LevelInfoDTO.CheckInLevel checkInLevel2 = new LevelInfoDTO.CheckInLevel();
        checkInLevel2.setName("2星");
        checkInLevel2.setDesc("测试222");
        levelList.add(checkInLevel2);

        LevelInfoDTO.CheckInLevel checkInLevel3 = new LevelInfoDTO.CheckInLevel();
        checkInLevel3.setName("3星");
        checkInLevel3.setDesc("测试333");
        levelList.add(checkInLevel3);

        levelInfoDTO.setLevelList(levelList);
        //更新详情
        return Result.success(levelInfoDTO);

    }
}

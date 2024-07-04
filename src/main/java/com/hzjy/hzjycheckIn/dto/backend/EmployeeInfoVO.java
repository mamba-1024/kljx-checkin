package com.hzjy.hzjycheckIn.dto.backend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hzjy.hzjycheckIn.entity.EmployeeDetails;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("员工信息")
public class EmployeeInfoVO {

    /**
     * 员工ID，主键，自增
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 员工姓名，字符串类型，不能为空
     */
    @ApiModelProperty("员工姓名")
    private String name;

    /**
     * 员工头像，字符串类型
     */
    @ApiModelProperty("员工头像")
    private String avatar;

    /**
     * 员工启停用状态，布尔类型
     */
    @ApiModelProperty("员工启停用状态")
    private Boolean status;

    /**
     * 员工在职状态，布尔类型
     */
    @ApiModelProperty("员工在职状态")
    private Boolean onBoard;

    /**
     * 员工是否实名认证，布尔类型
     */
    @ApiModelProperty("员工是否实名认证")
    private Boolean isAuthenticated;

    /**
     * 员工当前等级，整型
     */
    @ApiModelProperty("员工当前等级")
    private Integer level;

    /**
     * 员工手机号码
     */
    @ApiModelProperty("员工手机号码")
    private String phone;


    /**
     * 员工昵称
     */
    @ApiModelProperty("员工昵称")
    private String nickname;

    /**
     * 创建时间，默认为当前时间
     */
    @ApiModelProperty("")
    private LocalDateTime createTime;


    /**
     * 积分
     */
    @ApiModelProperty("积分")
    private Integer points;

    /**
     * 当前安全上工总时长
     */
    @ApiModelProperty("当前安全上工总时长")
    private BigDecimal totalWorkTime;

    /**
     * 当月累计工时
     */
    @ApiModelProperty("当月累计工时")
    private BigDecimal currentMonthTime;


    /**
     * 入职
     */
    @ApiModelProperty("入职时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onboardingDate;


    @ApiModelProperty("启用时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enableDate;


    @ApiModelProperty("停用时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime disableDate;

    /**
     * 离职
     */
    private LocalDateTime resignDate;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态  waitAudit-待审核，PASS-审核通过，reject-审核拒绝")
    private String auditStatus;

    private EmployeeDetails employeeDetails;


}

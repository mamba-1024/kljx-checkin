package com.hzjy.hzjycheckIn.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hzjy.hzjycheckIn.entity.query.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ApiModel("企业员工查询")
public class EmployeeQueryQO extends Page {

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("员工启停用状态")
    private Boolean status;

    @ApiModelProperty("在职状态")
    private Boolean onBoard;

    @ApiModelProperty("审核状态")
    private String auditStatus;

    @ApiModelProperty("入职开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate incumbencyStart;

    @ApiModelProperty("入职结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate incumbencyEnd;

    @ApiModelProperty("入职开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate resignStart;

    @ApiModelProperty("入职结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate resignEnd;

    @ApiModelProperty("启用开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate enableDateStart;

    @ApiModelProperty("启用结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate enableDateEnd;


    @ApiModelProperty("停用开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate disableDateStart;

    @ApiModelProperty("停用结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate disableDateEnd;

    @ApiModelProperty("是否实名")
    private Boolean isAuthenticated;

    private Boolean isEmployeeList;

    private Boolean isAuditList;
}

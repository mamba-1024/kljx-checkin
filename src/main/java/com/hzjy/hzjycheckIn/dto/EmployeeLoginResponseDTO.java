package com.hzjy.hzjycheckIn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工登录响应DTO
 */
@Data
@ApiModel("员工登录响应")
public class EmployeeLoginResponseDTO {

    @ApiModelProperty(value = "登录令牌", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @ApiModelProperty(value = "员工ID", example = "1")
    private Integer employeeId;

    @ApiModelProperty(value = "员工姓名", example = "张三")
    private String name;

    @ApiModelProperty(value = "员工昵称", example = "小张")
    private String nickname;

    @ApiModelProperty(value = "员工头像", example = "https://example.com/avatar.jpg")
    private String avatar;

    @ApiModelProperty(value = "员工手机号", example = "138****8000")
    private String phone;

    @ApiModelProperty(value = "员工等级", example = "2")
    private Integer level;

    @ApiModelProperty(value = "是否实名认证", example = "true")
    private Boolean isAuthenticated;

    @ApiModelProperty(value = "审核状态", example = "PASS")
    private String auditStatus;

    @ApiModelProperty(value = "员工状态", example = "true")
    private Boolean status;

    @ApiModelProperty(value = "在职状态", example = "true")
    private Boolean onBoard;
} 
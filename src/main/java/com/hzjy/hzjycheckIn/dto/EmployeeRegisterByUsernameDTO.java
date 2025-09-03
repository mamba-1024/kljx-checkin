package com.hzjy.hzjycheckIn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 员工注册DTO
 */
@Data
@ApiModel("员工注册(通过用户名和密码)")
public class EmployeeRegisterByUsernameDTO {

    @ApiModelProperty(value = "用户名", required = true, example = "张三")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty(value = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "密码长度必须在6-20位之间")
    private String password;

    @ApiModelProperty(value = "确认密码", required = true, example = "123456")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
} 
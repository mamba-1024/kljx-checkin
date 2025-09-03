package com.hzjy.hzjycheckIn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 员工登录DTO
 */
@Data
@ApiModel("员工登录")
public class EmployeeLoginByUsernameDTO {

    @ApiModelProperty(value = "用户名", required = true, example = "张三")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty(value = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;
} 
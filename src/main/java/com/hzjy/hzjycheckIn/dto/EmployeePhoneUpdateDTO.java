package com.hzjy.hzjycheckIn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("后台更新员工姓名手机号")
public class EmployeePhoneUpdateDTO {

    @ApiModelProperty(value = "员工ID", required = true, example = "123")
    @NotNull(message = "员工ID不能为空")
    private Integer id;

    @ApiModelProperty(value = "员工姓名", required = true, example = "张三")
    @NotBlank(message = "员工姓名不能为空")
    private String name;


    @ApiModelProperty(value = "新手机号", required = true, example = "13800138000")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}

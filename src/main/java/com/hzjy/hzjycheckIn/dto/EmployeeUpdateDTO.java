package com.hzjy.hzjycheckIn.dto;

/**
 * 用户信息
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "用户信息")
@Data
public class EmployeeUpdateDTO {

    @ApiModelProperty(value = "头像地址", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @ApiModelProperty(value = "昵称", example = "张三")
    private String nickname;

}

package com.hzjy.hzjycheckIn.dto;

/**
 * 用户信息
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "用户信息")
@Data
public class EmployeeDetailDTO {

    @ApiModelProperty(value = "姓名", example = "张三")
    private String name;

    @ApiModelProperty(value = "身份证号码", example = "36*************1")
    private String idCard;

    @ApiModelProperty(value = "认证状态", example = "true")
    private Boolean isAuthenticated;

    @ApiModelProperty(value = "银行卡号", example = "6565656666666")
    private String  bankCard;

    @ApiModelProperty(value = "银行名称", example = "江苏银行")
    private String bankName;

    @ApiModelProperty(value = "身份证照片", example = "true")
    private Boolean idCardIsUpload;


    private String auditStatus;

    private String rejectReason;
}


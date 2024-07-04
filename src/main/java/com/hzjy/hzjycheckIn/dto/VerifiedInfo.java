package com.hzjy.hzjycheckIn.dto;


import com.hzjy.hzjycheckIn.common.IdCard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ApiModel("实名认证")
@Data
public class  VerifiedInfo {


    /**
     * 员工身份证号码
     */
    @ApiModelProperty(value = "员工身份证号码", example = "身份证号码")
    @IdCard
    private String idCard;

    /**
     * 员工银行卡号码
     */
    @ApiModelProperty(value = "员工银行卡号码", example = "员工银行卡号码")
    @Size(min = 6, max = 20, message = "银行卡号输入有误")
    private String bankCard;

    /**
     * 员工银行卡所属支行信息
     */
    @ApiModelProperty(value = "员工银行卡所属支行信息", example = "员工银行卡所属支行信息")
    private String bankBranch;

    /**
     * 员工银行卡预留手机号码
     */
    @ApiModelProperty(value = "员工银行卡预留手机号码", example = "员工银行卡预留手机号码")
    @NotEmpty
    private String bankReservePhone;

    @ApiModelProperty(value = "姓名", example = "姓名")
    @NotEmpty
    private String userName;
    @NotEmpty
    @ApiModelProperty(value = "身份证正面照片", example = "身份证正面照片")
    private String frontIdCardUrl;

    @NotEmpty
    @ApiModelProperty(value = "身份证背面照片", example = "身份证背面照片")
    private String backendIdCardUrl;

}

package com.hzjy.hzjycheckIn.dto;

/**
 * 用户信息
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel(description = "用户信息")
@Data
public class EmployeeInfoDTO {

    @ApiModelProperty(value = "用户名", example = "张三")
    private String userName;

    @ApiModelProperty(value = "头像地址", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @ApiModelProperty(value = "昵称", example = "小张")
    private String nickname;

    @ApiModelProperty(value = "电话号码", example = "13888888888")
    private String phoneNumber;

    @ApiModelProperty(value = "等级", example = "VIP会员")
    private String level;

    @ApiModelProperty(value = "本月打卡时长", example = "20小时")
    private String checkInTime;

    @ApiModelProperty(value = "累计积分", example = "1000分")
    private int accumulatedPoints;

    @ApiModelProperty(value = "是否实名认证", example = "true")
    private boolean isVerified;



    /**
     * 入职
     */
    private LocalDateTime onboardingDate;

    /**
     * 离职
     */
    private LocalDateTime resignDate;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核结果",example = "PASS,REJECT")
    private String auditStatus;

    @ApiModelProperty(value = "审核不通过原因")
    private String rejectReason;
}


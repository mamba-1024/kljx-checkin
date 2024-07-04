package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@Getter
@Setter
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工姓名，字符串类型，不能为空
     */
    private String name;

    /**
     * 员工头像，字符串类型
     */
    private String avatar;

    /**
     * 员工启停用状态，布尔类型
     */
    private Boolean status;

    /**
     * 员工在职状态，布尔类型
     */
    private Boolean onBoard;

    /**
     * 员工是否实名认证，布尔类型
     */
    private Boolean isAuthenticated;

    /**
     * 员工当前等级，整型
     */
    private Integer level;

    /**
     * 员工手机号码
     */
    private String phone;

    /**
     * 员工密码
     */
    private String password;

    /**
     * 密码加密盐
     */
    private String salt;

    /**
     * 员工昵称
     */
    private String nickname;

    /**
     * 创建时间，默认为当前时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间，默认为当前时间，且每次更新时自动更新
     */
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 积分
     */
    private Integer points;

    /**
     * 当前安全上工总时长
     */
    private BigDecimal totalWorkTime;

    /**
     * 当月累计工时
     */
    private BigDecimal currentMonthTime;

    private BigDecimal leftWorkTime;

    private String openId;

    /**
     * 入职
     */
    private LocalDateTime onboardingDate;
    /**
     * 启用时间
     */
    private LocalDateTime enableDate;


    /**
     * 停用时间
     */
    private LocalDateTime disableDate;

    /**
     * 离职
     */
    private LocalDateTime resignDate;

    /**
     * 审核状态
     */
    private String auditStatus;

    private String rejectReason;
}

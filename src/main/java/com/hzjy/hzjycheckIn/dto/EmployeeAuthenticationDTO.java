package com.hzjy.hzjycheckIn.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeAuthenticationDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 员工详情ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工ID
     */
    private Integer employeeId;

    /**
     * 员工身份证号码
     */
    private String idCard;

    /**
     * 员工银行卡号码
     */
    private String bankCard;

    /**
     * 员工银行卡所属支行信息
     */
    private String bankBranch;

    /**
     * 员工银行卡预留手机号码
     */
    private String bankReservePhone;

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
}

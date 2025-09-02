package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 账户实体类
 * </p>
 * <p>
 * 用于管理系统后台登录账户信息，包括账户名称、密码、登录时间等
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@Getter
@Setter
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账户名称
     * 登录账户的用户名，用于系统登录识别
     */
    private String name;

    /**
     * 账户密码
     * 登录账户的密码，用于身份验证
     */
    private String password;

    /**
     * 创建时间
     * 账户记录的创建时间
     */
    private LocalDateTime createAt;

    /**
     * 创建人
     * 创建该账户的用户或管理员
     */
    private String createBy;

    /**
     * 最后登录时间
     * 账户最后一次登录的时间
     */
    private LocalDateTime loginTime;
}

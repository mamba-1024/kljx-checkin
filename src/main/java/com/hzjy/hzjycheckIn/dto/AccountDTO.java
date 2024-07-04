package com.hzjy.hzjycheckIn.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工姓名，字符串类型，不能为空
     */
    private String name;

    /**
     * 员工头像，字符串类型
     */
    private String password;

}

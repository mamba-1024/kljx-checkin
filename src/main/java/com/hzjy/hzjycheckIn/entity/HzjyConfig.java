package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
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
public class HzjyConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工姓名，字符串类型，不能为空
     */
    private String mainUrl;

}
package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EmployeePunchMonth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer employeeId;

    private String punchMonth;

    private double commonPunch;

    private double overPunch;

}

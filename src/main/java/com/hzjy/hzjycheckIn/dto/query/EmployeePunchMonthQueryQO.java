package com.hzjy.hzjycheckIn.dto.query;

import com.hzjy.hzjycheckIn.entity.query.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("企业员工查询")
public class EmployeePunchMonthQueryQO extends Page {

    @ApiModelProperty("姓名")
    private Integer employeeId;

}

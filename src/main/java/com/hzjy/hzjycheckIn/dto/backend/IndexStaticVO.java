package com.hzjy.hzjycheckIn.dto.backend;

import com.hzjy.hzjycheckIn.entity.Employee;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ApiModel("大盘数据")
public class IndexStaticVO {


    @ApiModelProperty("员工统计")
    private Map<String, Long> employeeCount;

    @ApiModelProperty("员工上班情况")
    private Map<String, BigDecimal> employeeWorkCount;

    @ApiModelProperty("等级分布")
    private Map<String, Long> levelDistribution;

    @ApiModelProperty("员工积分排名前十")
    private List<Employee> employees;

    @ApiModelProperty("待审核员工数量")
    private Integer waitAuditCount;

}

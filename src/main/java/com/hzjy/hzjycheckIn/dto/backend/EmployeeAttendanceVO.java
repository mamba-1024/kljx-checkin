package com.hzjy.hzjycheckIn.dto.backend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApiModel("员工打卡明细")
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAttendanceVO {

    @ApiModelProperty("打卡日期")
    private String punchDate;

    @ApiModelProperty("员工名称")
    private String name;

    /**
     * 员工在职状态，布尔类型
     */
    @ApiModelProperty("员工在职状态")
    private Boolean onBoard;


    @ApiModelProperty("状态")
    private Boolean status;

    @ApiModelProperty("普通打卡时间")
    private BackendAttendanceRecordVO commonRecord;

    @ApiModelProperty("加班打卡时间")
    private BackendAttendanceRecordVO overRecord;


}

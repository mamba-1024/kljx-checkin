package com.hzjy.hzjycheckIn.dto;

import com.hzjy.hzjycheckIn.entity.AttendanceRecord;
import com.hzjy.hzjycheckIn.entity.WorkShift;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("打卡初始化信息")
@Data
public class AttendanceInfoDTO {

    /**
     * 班次
     */
    @ApiModelProperty("班次")
    private List<WorkShift> workShifts;

    @ApiModelProperty("打卡记录")
    private List<AttendanceRecord> attendanceRecords;

    @ApiModelProperty("默认班次")
    private WorkShift defaultWorkShift;

    @ApiModelProperty("时令，夏令时，冬令时")
    private String seasonalName;

    @ApiModelProperty("普通打卡说明")
    private String commonAttendanceDesc;


    @ApiModelProperty("加班打卡说明")
    private String attendanceDesc;

}

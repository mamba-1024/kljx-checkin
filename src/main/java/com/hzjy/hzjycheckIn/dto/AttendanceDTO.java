package com.hzjy.hzjycheckIn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@ApiModel("班次")
public class AttendanceDTO {

    @ApiModelProperty("班次ID")
    private Integer workShiftId;

    @ApiModelProperty("打卡时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime attendanceTime;

    @ApiModelProperty("打卡类型")
    private Integer punchType;


}

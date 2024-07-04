package com.hzjy.hzjycheckIn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@ApiModel("考勤记录查询")
@Data
public class AttendanceQueryDTO {

    @ApiModelProperty("查询月份")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate queryMonth;

}

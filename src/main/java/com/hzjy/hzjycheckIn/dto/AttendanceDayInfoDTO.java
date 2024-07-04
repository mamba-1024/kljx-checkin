package com.hzjy.hzjycheckIn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("每日打卡信息")
public class AttendanceDayInfoDTO {

    @ApiModelProperty("当前时间")
    @DateTimeFormat(pattern = "MM-dd")
    @JsonFormat(pattern = "MM-dd")
    private LocalDate localDate;

    @ApiModelProperty("当日打卡时长")
    private BigDecimal duration;


    @ApiModelProperty("当日打卡详情")
    private List<AttendanceDayDetailInfo> attendanceDayDetailInfos;


    @Data
    public static class AttendanceDayDetailInfo {

        /**
         * 班次名称
         */
        @ApiModelProperty("班次名称")
        private String shiftName;

        @ApiModelProperty("时长,单位h")
        private BigDecimal duration;

        @ApiModelProperty("打卡时间")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startTime;

        @ApiModelProperty("打卡结束时间")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime endTime;

        @ApiModelProperty("时间展示")
        private String showTime;

    }
}

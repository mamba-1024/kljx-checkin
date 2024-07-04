package com.hzjy.hzjycheckIn.dto.backend;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("具体班次打卡时间")
@AllArgsConstructor
@NoArgsConstructor
public class BackendAttendanceRecordVO {

    @ApiModelProperty("上班时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime punchUpTime;

    @ApiModelProperty("下班时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime punchDownTime;

    @ApiModelProperty("打卡时长")
    private BigDecimal hours;

    private Integer workShiftId;

}

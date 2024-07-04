package com.hzjy.hzjycheckIn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.License;
import lombok.Data;

import java.util.List;

@ApiModel("等级说明")
@Data
public class LevelInfoDTO {

    @ApiModelProperty("备注")
    private String remark;

    private List<CheckInLevel> levelList;

    @Data
    public static class CheckInLevel {

        private String name;

        private String desc;



    }
}

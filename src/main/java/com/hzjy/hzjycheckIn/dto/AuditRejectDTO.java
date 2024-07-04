package com.hzjy.hzjycheckIn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("审核拒绝")
public class AuditRejectDTO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("拒绝原因")
    private String rejectReason;

}

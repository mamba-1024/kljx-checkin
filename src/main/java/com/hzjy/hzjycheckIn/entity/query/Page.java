package com.hzjy.hzjycheckIn.entity.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {

    @ApiModelProperty("分页参数，页码")
    private Integer page;


    @ApiModelProperty("每页长度")
    private Integer pageSize;


}

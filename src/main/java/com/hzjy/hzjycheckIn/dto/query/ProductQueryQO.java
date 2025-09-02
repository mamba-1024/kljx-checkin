package com.hzjy.hzjycheckIn.dto.query;

import com.hzjy.hzjycheckIn.entity.query.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductQueryQO extends Page {

    /**
     * 产品标题，用于模糊查询
     */
    @ApiModelProperty("产品标题")
    private String title;

}

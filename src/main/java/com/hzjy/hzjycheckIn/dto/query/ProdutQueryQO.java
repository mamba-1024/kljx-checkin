package com.hzjy.hzjycheckIn.dto.query;

import com.hzjy.hzjycheckIn.entity.query.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutQueryQO extends Page {

    @ApiModelProperty("姓名")
    private String title;


}

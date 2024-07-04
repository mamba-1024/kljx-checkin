package com.hzjy.hzjycheckIn.dto;

import com.hzjy.hzjycheckIn.entity.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("首页")
@Data
public class IndexInfoDTO {

    @ApiModelProperty("首图")
    private List<String> mainUrls;

    private String mainUrl;

    /**
     * 最新产品
     */
    @ApiModelProperty("最新产品")
    private List<Product> products;




}

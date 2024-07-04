package com.hzjy.hzjycheckIn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@TableName("product")
public class Product implements Serializable {

    private Integer id;

    private String title;

    private String productMainUrl;

    private String htmlContent;

    private Date createdAt;

    private String shortDesc;

    private Boolean showIndex;


}

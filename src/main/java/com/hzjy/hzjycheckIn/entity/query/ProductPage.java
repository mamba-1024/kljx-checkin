package com.hzjy.hzjycheckIn.entity.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzjy.hzjycheckIn.entity.Product;

public class ProductPage extends Page<Product> {

    private String name;

    private Boolean isShow;
}

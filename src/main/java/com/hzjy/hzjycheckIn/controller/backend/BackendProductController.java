package com.hzjy.hzjycheckIn.controller.backend;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.dto.query.ProdutQueryQO;
import com.hzjy.hzjycheckIn.entity.Product;
import com.hzjy.hzjycheckIn.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("backend/product")
public class BackendProductController extends BackendBaseController<Product> {

    @Autowired
    private ProductService productService;


    @Override
    protected Result<Boolean> addEntity(@RequestBody Product product) {
        return Result.success(productService.save(product));
    }

    @Override
    protected Result<Boolean> deleteEntity(Long id) {
        return Result.success(productService.removeById(id));
    }

    @Override
    protected Result<Product> getEntity(Long id) {
        Product byId = productService.getById(id);
        if (Objects.nonNull(byId)) {
            byId.setProductMainUrl("https://hzjysb.oss-cn-hangzhou.aliyuncs.com/" + byId.getProductMainUrl());
        }
        return Result.success(byId);
    }

    @Override
    protected Result<Boolean> updateEntity(@RequestBody Product product) {
        return Result.success(productService.updateById(product));
    }

    @ApiOperation("查看所有产品")
    @PostMapping("/list")
    public Result<Page<Product>> listEmployee(@RequestBody ProdutQueryQO produtQueryQO) {
        Page<Product> page = new Page<>();
        page.setPages(produtQueryQO.getPage());
        page.setSize(produtQueryQO.getPageSize());
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        Page<Product> page1 = productService.page(page, productQueryWrapper);
        List<Product> records = page1.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            for (Product record : records) {
                record.setProductMainUrl("https://hzjysb.oss-cn-hangzhou.aliyuncs.com/" + record.getProductMainUrl());
            }
        }
        return Result.success(page1);
    }


    @ApiOperation("是否首页展示")
    @PostMapping("/showIndex")
    public Result<Boolean> showIndex(@RequestBody Product product) {
        UpdateWrapper<Product> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", product);
        updateWrapper.set("show_index", product.getShowIndex());
        boolean update = productService.update(updateWrapper);
        return Result.success(update);
    }
}

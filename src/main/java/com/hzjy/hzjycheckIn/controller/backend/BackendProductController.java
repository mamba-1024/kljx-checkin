package com.hzjy.hzjycheckIn.controller.backend;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.dto.query.ProductQueryQO;
import com.hzjy.hzjycheckIn.entity.Product;
import com.hzjy.hzjycheckIn.service.ProductService;
import com.hzjy.hzjycheckIn.util.FileUrlUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("backend/product")
public class BackendProductController extends BackendBaseController<Product> {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileUrlUtil fileUrlUtil;

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
            byId.setProductMainUrl(fileUrlUtil.getFileUrl(byId.getProductMainUrl()));
        }
        return Result.success(byId);
    }

    @Override
    protected Result<Boolean> updateEntity(@RequestBody Product product) {
        return Result.success(productService.updateById(product));
    }

    @ApiOperation("查看所有产品，支持根据标题模糊查询")
    @PostMapping("/list")
    public Result<Page<Product>> listEmployee(@RequestBody ProductQueryQO productQueryQO) {
        Page<Product> page = new Page<>();
        page.setPages(productQueryQO.getPage());
        page.setSize(productQueryQO.getPageSize());
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        
        // 添加根据title模糊查询的条件
        if (StringUtils.isNotBlank(productQueryQO.getTitle())) {
            productQueryWrapper.like("title", productQueryQO.getTitle());
        }
        
        Page<Product> page1 = productService.page(page, productQueryWrapper);
        List<Product> records = page1.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            for (Product record : records) {
                record.setProductMainUrl(fileUrlUtil.getFileUrl(record.getProductMainUrl()));
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

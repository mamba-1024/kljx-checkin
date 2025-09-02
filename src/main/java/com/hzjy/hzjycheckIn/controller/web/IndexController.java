package com.hzjy.hzjycheckIn.controller.web;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.dto.IndexInfoDTO;
import com.hzjy.hzjycheckIn.entity.EntAction;
import com.hzjy.hzjycheckIn.entity.HzjyConfig;
import com.hzjy.hzjycheckIn.entity.Product;
import com.hzjy.hzjycheckIn.service.EntActionService;
import com.hzjy.hzjycheckIn.service.HzjyConfigService;
import com.hzjy.hzjycheckIn.service.ProductService;
import com.hzjy.hzjycheckIn.util.FileUrlUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiOperation("首页")
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private ProductService productService;

    @Autowired
    private EntActionService entActionService;

    @Autowired
    private HzjyConfigService hzjyConfigService;

    @Autowired
    private FileUrlUtil fileUrlUtil;

    @ApiOperation("首页信息")
    @GetMapping("/info")
    public Result<IndexInfoDTO> info() {
        IndexInfoDTO indexInfoDTO = new IndexInfoDTO();
        List<String> mailUrls = new ArrayList<>();
        HzjyConfig byId = hzjyConfigService.getById(1);
        if (Objects.nonNull(byId) && StrUtil.isNotEmpty(byId.getMainUrl())) {
            String mainUrl = byId.getMainUrl();
            String filenames = mainUrl.substring(1, mainUrl.length() - 1);
// 使用逗号分割字符串
            String[] filenameArray = filenames.split(", ");
            for (String urls : filenameArray) {
                mailUrls.add(fileUrlUtil.getFileUrl(urls));
            }
        }
        indexInfoDTO.setMainUrls(mailUrls);
        indexInfoDTO.setMainUrl(fileUrlUtil.getFileUrl("index/mainurl1.jpg"));
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("show_index", Boolean.TRUE);
        List<Product> products = productService.list(productQueryWrapper);
        for (Product product : products) {
            product.setProductMainUrl(fileUrlUtil.getFileUrl(product.getProductMainUrl()));
        }
        indexInfoDTO.setProducts(products);
        return Result.success(indexInfoDTO);
    }


    @ApiOperation("产品详情")
    @GetMapping("/product")
    public Result<List<Product>> product() {
        List<Product> products = productService.list();
        for (Product product : products) {
            product.setProductMainUrl(fileUrlUtil.getFileUrl(product.getProductMainUrl()));
        }
        return Result.success(products);
    }

    @ApiOperation("查看产品详情")
    @GetMapping("/product/{id}")
    public Result<Product> getProductDetail(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (Objects.nonNull(product)) {
            product.setProductMainUrl(fileUrlUtil.getFileUrl(product.getProductMainUrl()));
        }
        return Result.success(product);
    }

    @ApiOperation("企业动态")
    @GetMapping("/action")
    public Result<List<EntAction>> action() {
        List<EntAction> products = entActionService.list();
        for (EntAction product : products) {
            product.setActionMainUrl(fileUrlUtil.getFileUrl(product.getActionMainUrl()));
        }
        return Result.success(products);
    }


    @ApiOperation("查看企业动态详情")
    @GetMapping("/action/{id}")
    public Result<EntAction> getEntActionDetail(@PathVariable Long id) {
        EntAction product = entActionService.getById(id);
        if (Objects.nonNull(product)) {
            product.setActionMainUrl(fileUrlUtil.getFileUrl(product.getActionMainUrl()));
        }
        return Result.success(product);
    }


}

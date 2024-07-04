package com.hzjy.hzjycheckIn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzjy.hzjycheckIn.entity.Product;
import com.hzjy.hzjycheckIn.mapper.ProductMapper;
import com.hzjy.hzjycheckIn.service.ProductService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
@Service
public class ProducatServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}

package com.hzjy.hzjycheckIn.controller.web;

import com.hzjy.hzjycheckIn.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试接口")
@Slf4j
public class TestController {

    @ApiOperation("中文编码测试")
    @GetMapping("/encoding")
    public Result<String> testEncoding() {
        log.info("测试中文编码");
        return Result.success("中文编码测试成功");
    }

    @ApiOperation("简单测试")
    @GetMapping("/simple")
    public Result<String> testSimple() {
        return Result.success("请求成功");
    }
} 
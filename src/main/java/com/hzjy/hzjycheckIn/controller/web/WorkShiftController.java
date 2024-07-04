package com.hzjy.hzjycheckIn.controller.web;

import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.entity.WorkShift;
import com.hzjy.hzjycheckIn.service.WorkShiftService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:56
 */
@ApiOperation("班次")
@RestController
@RequestMapping("/workShift")
public class WorkShiftController {

    @Autowired
    private WorkShiftService workShiftService;

    @ApiOperation("获取班次")
    @GetMapping("workShift")
    public Result<List<WorkShift>> getWorkShift() {
        List<WorkShift> list = workShiftService.list();
        return Result.success(list);
    }

}

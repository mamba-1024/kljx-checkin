package com.hzjy.hzjycheckIn.controller.backend;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzjy.hzjycheckIn.common.Result;
import com.hzjy.hzjycheckIn.dto.query.EntActionQO;
import com.hzjy.hzjycheckIn.entity.EntAction;
import com.hzjy.hzjycheckIn.service.EntActionService;
import com.hzjy.hzjycheckIn.util.FileUrlUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("backend/entAction")
public class BackendEntActionController extends BackendBaseController<EntAction> {

    @Autowired
    private EntActionService entActionService;

    @Autowired
    private FileUrlUtil fileUrlUtil;

    @Override
    protected Result<Boolean> addEntity(@RequestBody EntAction entAction) {
        return Result.success(entActionService.save(entAction));
    }

    @Override
    protected Result<Boolean> deleteEntity(@PathVariable Long id) {
        return Result.success(entActionService.removeById(id));
    }

    @Override
    protected Result<EntAction> getEntity(Long id) {
        EntAction byId = entActionService.getById(id);
        if (Objects.nonNull(byId)) {
            byId.setActionMainUrl(fileUrlUtil.getFileUrl(byId.getActionMainUrl()));
        }
        return Result.success(byId);
    }

    @Override
    protected Result<Boolean> updateEntity(@RequestBody EntAction entAction) {
        return Result.success(entActionService.updateById(entAction));
    }

    @ApiOperation("查看所有企业活动，支持根据标题模糊查询")
    @PostMapping("/list")
    public Result<Page<EntAction>> listEmployee(@RequestBody EntActionQO entActionQO) {
        Page<EntAction> page = new Page<>();
        page.setPages(entActionQO.getPage());
        page.setSize(entActionQO.getPageSize());
        QueryWrapper<EntAction> productQueryWrapper = new QueryWrapper<>();
        
        // 添加根据title模糊查询的条件
        if (StringUtils.isNotBlank(entActionQO.getTitle())) {
            productQueryWrapper.like("title", entActionQO.getTitle());
        }
        
        Page<EntAction> page1 = entActionService.page(page, productQueryWrapper);
        List<EntAction> records = page1.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            for (EntAction record : records) {
                record.setActionMainUrl(fileUrlUtil.getFileUrl(record.getActionMainUrl()));
            }
        }
        return Result.success(page1);
    }

}

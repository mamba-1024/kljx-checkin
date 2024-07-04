package com.hzjy.hzjycheckIn.controller.backend;

import com.hzjy.hzjycheckIn.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class BackendBaseController<T> {

    @PostMapping("add")
    public Result<Boolean> add(@RequestBody T t) {
        return this.addEntity(t);
    }

    protected abstract Result<Boolean> addEntity(T t);

    @PostMapping("delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return this.deleteEntity(id);
    }

    protected abstract Result<Boolean> deleteEntity(Long id);

    @PostMapping("update")
    public Result<Boolean> update(@RequestBody T t) {
        return this.updateEntity(t);
    }

    @GetMapping("get/{id}")
    public Result<T> get(@PathVariable Long id){
        return this.getEntity(id);
    }

    protected abstract Result<T> getEntity(Long id);

    protected abstract Result<Boolean> updateEntity(T t);


}

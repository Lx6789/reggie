package com.lx.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lx.reggie.common.R;
import com.lx.reggie.dto.SetmealDto;
import com.lx.reggie.entity.Setmeal;
import com.lx.reggie.service.SetmealDishService;
import com.lx.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        return setmealService.saveSetmeal(setmealDto);
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        return setmealService.PagingQuery(page, pageSize, name);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        return setmealService.DeleteSetmeal(ids);
    }

    /**
     * 根据条件查询菜单数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        return setmealService.QuerySetmeal(setmeal);
    }
}

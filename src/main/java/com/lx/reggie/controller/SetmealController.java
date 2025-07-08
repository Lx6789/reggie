package com.lx.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lx.reggie.common.R;
import com.lx.reggie.dto.SetmealDto;
import com.lx.reggie.entity.Dish;
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

    /**
     * 修改状态，修改dish表
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> getDishStatus(@PathVariable int status, @RequestParam List<Long> ids) {
        //判断status 0 停售 1 起售
        if (status == 0) {
            for (int i = 0; i < ids.size(); i++) {
                //判断是否需要修改状态
                Setmeal dish = setmealService.getById(ids.get(i));
                if (dish.getStatus() == 1) {
                    dish.setStatus(0);
                    setmealService.updateById(dish);
                }
            }
        } else {
            for (int i = 0; i < ids.size(); i++) {
                //判断是否需要修改状态
                Setmeal dish = setmealService.getById(ids.get(i));
                if (dish.getStatus() == 0) {
                    dish.setStatus(1);
                    setmealService.updateById(dish);
                }
            }
        }
        return R.success("修改成功");
    }


}

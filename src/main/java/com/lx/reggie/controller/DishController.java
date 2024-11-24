package com.lx.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lx.reggie.common.R;
import com.lx.reggie.dto.DishDto;
import com.lx.reggie.entity.Dish;
import com.lx.reggie.service.DishFlavorService;
import com.lx.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        return dishService.AddDishes(dishDto);
    }

    /**
     * 菜品信息的分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        return dishService.DishPagingQuery(page, pageSize, name);
    }

    /**
     * 根据id查询菜品信息和口味信息
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        return dishService.SelectDishWithFlavor(id);
    }


    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        return dishService.updateDishes(dishDto);
    }

    /**
     * 根据条件查询菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        return dishService.SelectDish(dish);
    }
}

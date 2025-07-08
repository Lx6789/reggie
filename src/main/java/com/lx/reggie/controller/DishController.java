package com.lx.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lx.reggie.common.CustomException;
import com.lx.reggie.common.R;
import com.lx.reggie.dto.DishDto;
import com.lx.reggie.entity.Dish;
import com.lx.reggie.service.CategoryService;
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

    @Autowired
    private CategoryService categoryService;

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
     * 根据条件查询对应的菜品数据
     *
     * @param dish
     * @return
     */
    /*@GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }*/

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        return dishService.SelectDish(dish);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delet(@RequestParam List<Long> ids) {
        log.info("dish :" + ids.toString());
        dishService.Remove(ids);
        return R.success("删除成功");
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
                Dish dish = dishService.getById(ids.get(i));
                if (dish.getStatus() == 1) {
                    dish.setStatus(0);
                    dishService.updateById(dish);
                }
            }
        } else {
            for (int i = 0; i < ids.size(); i++) {
                //判断是否需要修改状态
                Dish dish = dishService.getById(ids.get(i));
                if (dish.getStatus() == 0) {
                    dish.setStatus(1);
                    dishService.updateById(dish);
                }
            }
        }
        return R.success("修改成功");
    }
}

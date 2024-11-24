package com.lx.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.reggie.common.R;
import com.lx.reggie.dto.DishDto;
import com.lx.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    R<Page> DishPagingQuery(int page, int pageSize, String name);

    R<String> AddDishes(DishDto dishDto);

    R<DishDto> SelectDishWithFlavor(Long id);

    R<String> updateDishes(DishDto dishDto);

    R<List<Dish>> SelectDish(Dish dish);
}

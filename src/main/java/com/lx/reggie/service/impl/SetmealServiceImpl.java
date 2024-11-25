package com.lx.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.reggie.common.CustomException;
import com.lx.reggie.common.R;
import com.lx.reggie.dto.SetmealDto;
import com.lx.reggie.entity.Category;
import com.lx.reggie.entity.Setmeal;
import com.lx.reggie.entity.SetmealDish;
import com.lx.reggie.mapper.SetmealMapper;
import com.lx.reggie.service.CategoryService;
import com.lx.reggie.service.SetmealDishService;
import com.lx.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public R<String> DeleteSetmeal(List<Long> ids) {
        RemoveWithDish(ids);
        return R.success("套餐数据删除成功");
    }

    @Override
    public R<String> saveSetmeal(SetmealDto setmealDto) {
        saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @Override
    public R<Page> PagingQuery(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>();
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 新增套餐同时保存套餐和菜品间的关系
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //报存套餐和菜品的关联信息
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除与其相关联的数据
     * @param ids
     */
    @Transactional
    public void RemoveWithDish(List<Long> ids) {
        //查询套餐状态，确认是否能删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        //若不能删除，抛出异常
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //若能删除，先删除套餐表数据
        this.removeByIds(ids);
        //删除关系表数据
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper1);
    }
}

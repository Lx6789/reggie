package com.lx.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.reggie.common.CustomException;
import com.lx.reggie.common.R;
import com.lx.reggie.entity.Category;
import com.lx.reggie.entity.Dish;
import com.lx.reggie.entity.Setmeal;
import com.lx.reggie.mapper.CategoryMapper;
import com.lx.reggie.service.CategoryService;
import com.lx.reggie.service.DishService;
import com.lx.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public R<String> AddCategory(Category category) {
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @Override
    public R<Page> PagingQuery(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件,根据sort排序
        queryWrapper.orderByAsc(Category::getSort);
        //进行分页查询
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @Override
    public R<String> DeleteCategory(Long id) {
        remove(id);
        return R.success("分类信息删除成功");
    }

    @Override
    public R<String> UpdateCategory(Category category) {
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    @Override
    public R<List<Category>> QueryClassification(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    private void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询当前分类是否关联菜品，如意关联抛出一个业务异常
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if(count1 > 0) {
            throw new CustomException("当前分类下关联菜品不能删除");
        }
        //查询当前分类是否关联套餐，如意关联抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0) {
            throw new CustomException("当前分类下关联套餐不能删除");
        }
        //删除分类
        super.removeById(id);
    }
}

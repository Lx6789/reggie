package com.lx.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.reggie.common.R;
import com.lx.reggie.dto.SetmealDto;
import com.lx.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    R<List<Setmeal>> QuerySetmeal(Setmeal setmeal);

    R<String> DeleteSetmeal(List<Long> ids);

    R<String> saveSetmeal(SetmealDto setmealDto);

    R<Page> PagingQuery(int page, int pageSize, String name);
}

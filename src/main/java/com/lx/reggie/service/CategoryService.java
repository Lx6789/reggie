package com.lx.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.reggie.common.R;
import com.lx.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    R<String> AddCategory(Category category);

    R<Page> PagingQuery(int page, int pageSize);

    R<String> DeleteCategory(Long id);

    R<String> UpdateCategory(Category category);
}

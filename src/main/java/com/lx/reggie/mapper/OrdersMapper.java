package com.lx.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}

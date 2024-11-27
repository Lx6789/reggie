package com.lx.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}

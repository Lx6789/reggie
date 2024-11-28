package com.lx.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lx.reggie.common.R;
import com.lx.reggie.entity.Employee;
import com.lx.reggie.entity.Orders;
import com.lx.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("下单成功");
    }


    @GetMapping("/page")
    public R<Page<Orders>> page(int page, int pageSize, Long number, String beginTime, String endTime) {
        //分页查询，根据订单号 时间查询
        //构造分页构造器
        Page<Orders> pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //判断时间
        LocalDateTime bt = null;
        LocalDateTime et = null;
        if (beginTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 进行转换
            bt = LocalDateTime.parse(beginTime, formatter);
            et = LocalDateTime.parse(endTime, formatter);
        }
        queryWrapper.eq(number != null, Orders::getNumber, number).between(bt != null ,Orders::getOrderTime, bt, et);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }
}

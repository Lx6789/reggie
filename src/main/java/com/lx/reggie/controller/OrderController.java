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
        queryWrapper.eq(number != null, Orders::getNumber, number);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM -dd HH:mm:ss");
        // 进行转换
        LocalDateTime bt = LocalDateTime.parse(beginTime, formatter);
        LocalDateTime et = LocalDateTime.parse(endTime, formatter);

        return null;
    }
}

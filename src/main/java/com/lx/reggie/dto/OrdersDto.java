package com.lx.reggie.dto;

import lombok.Data;
import java.util.List;
import com.lx.reggie.entity.Orders;
import com.lx.reggie.entity.OrderDetail;


@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}

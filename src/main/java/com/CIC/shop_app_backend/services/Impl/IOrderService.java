package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.dtos.OrderDTO;
import com.CIC.shop_app_backend.entity.Order;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO);
}

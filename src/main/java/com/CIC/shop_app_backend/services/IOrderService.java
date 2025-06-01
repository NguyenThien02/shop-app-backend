package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.dtos.OrderDTO;
import com.CIC.shop_app_backend.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO);

    Page<Order> getOrderByUserId(Long userId, PageRequest pageRequest);

    Order getOrderById(Long orderId);
}

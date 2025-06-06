package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.dtos.OrderDTO;
import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO);

    Page<Order> getOrderByUserId(Long userId, PageRequest pageRequest);

    Page<Order> getOrderBySellerId(Long sellerId, PageRequest pageRequest);

    Order getOrderById(Long orderId);

    Order updateOrderStatus(Long orderId, OrderStatus orderStatus);
}

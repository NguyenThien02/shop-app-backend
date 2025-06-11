package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.dtos.OrderDetailDTO;
import com.CIC.shop_app_backend.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO, Long orderId);

    Page<OrderDetail> getOrderDetailByOrderId(Long orderId, PageRequest pageRequest);
}

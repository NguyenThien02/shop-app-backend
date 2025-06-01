package com.CIC.shop_app_backend.repository;

import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Page<OrderDetail> findByOrder_OrderId(Long orderId, Pageable pageable);
}

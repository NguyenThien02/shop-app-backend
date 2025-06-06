package com.CIC.shop_app_backend.repository;

import com.CIC.shop_app_backend.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser_UserId(Long userId, Pageable pageable);

    Page<Order> findBySeller_UserId(Long sellerId, Pageable pageable);
}

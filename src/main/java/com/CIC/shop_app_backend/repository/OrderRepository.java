package com.CIC.shop_app_backend.repository;

import com.CIC.shop_app_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

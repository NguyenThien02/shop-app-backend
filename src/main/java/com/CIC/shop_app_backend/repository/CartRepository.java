package com.CIC.shop_app_backend.repository;

import com.CIC.shop_app_backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser_UserId(Long userId);
}

package com.CIC.shop_app_backend.repository;

import com.CIC.shop_app_backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
//    Optional<Cart> findByUser_UserId(Long userId);
    Cart findByUser_UserId(Long userId);
}

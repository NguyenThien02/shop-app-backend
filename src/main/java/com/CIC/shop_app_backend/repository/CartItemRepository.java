package com.CIC.shop_app_backend.repository;

import com.CIC.shop_app_backend.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Page<CartItem> findByCart_CartId(Long cartId, Pageable pageable);
    List<CartItem> findAllByCartItemIdIn(List<Long> ids);
}

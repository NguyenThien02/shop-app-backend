package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.dtos.CartDTO;
import com.CIC.shop_app_backend.entity.Cart;

public interface ICartService {
    Cart createCart(CartDTO cartDTO);

    Cart getCartByUserId(Long userId);
}

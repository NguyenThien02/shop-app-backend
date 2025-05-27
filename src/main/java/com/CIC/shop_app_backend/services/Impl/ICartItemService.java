package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.dtos.CartItemsDTO;
import com.CIC.shop_app_backend.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ICartItemService {
    CartItem createCartItem (CartItemsDTO cartItemsDTO);

    Page<CartItem> getAllCartItemByCartId(PageRequest pageRequest, Long cartId);

    void deleteAllCartItem();
}

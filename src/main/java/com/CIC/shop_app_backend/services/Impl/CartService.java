package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.dtos.CartDTO;
import com.CIC.shop_app_backend.entity.Cart;
import com.CIC.shop_app_backend.entity.User;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.CartRepository;
import com.CIC.shop_app_backend.repository.UserRepository;
import com.CIC.shop_app_backend.services.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Override
    public Cart createCart(CartDTO cartDTO) {
        User user = userRepository.findById(cartDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Not found user with id: " + cartDTO.getUserId()));

        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Not found user with ID: " + userId));
        Cart cart = cartRepository.findByUser_UserId(userId);
        if(cart == null){
            CartDTO cartDTO = new CartDTO(userId);
            return createCart(cartDTO);
        }
        return cart;
    }
}

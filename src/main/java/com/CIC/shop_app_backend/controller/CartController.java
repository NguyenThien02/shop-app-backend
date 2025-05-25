package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.dtos.CartDTO;
import com.CIC.shop_app_backend.entity.Cart;
import com.CIC.shop_app_backend.responses.CartResponse;
import com.CIC.shop_app_backend.responses.UserResponse;
import com.CIC.shop_app_backend.services.Impl.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/carts")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @PostMapping("create")
    public ResponseEntity<?> creatCart(
            @Valid @RequestBody CartDTO cartDTO
    ){
        try {
            Cart cart = cartService.createCart(cartDTO);
            CartResponse cartResponse = new CartResponse();
            cartResponse.setUserResponse(UserResponse.fromUser(cart.getUser()));
            cartResponse.setId(cart.getCartId());

            return ResponseEntity.ok(cartResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{user-id}")
    public ResponseEntity<?> getCartByUserId(
            @PathVariable("user-id") Long userId
    ){
        try {
            Cart cart = cartService.getCartByUserId(userId);
            CartResponse cartResponse = new CartResponse();
            cartResponse.setId(cart.getCartId());
            cartResponse.setUserResponse(UserResponse.fromUser(cart.getUser()));

            return ResponseEntity.ok(cartResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

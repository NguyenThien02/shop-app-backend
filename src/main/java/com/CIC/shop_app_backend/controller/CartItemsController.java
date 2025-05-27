package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.dtos.CartItemsDTO;
import com.CIC.shop_app_backend.entity.CartItem;
import com.CIC.shop_app_backend.responses.CartItemResponse;
import com.CIC.shop_app_backend.responses.ListCartItemResponse;
import com.CIC.shop_app_backend.services.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/cart-items")
@RequiredArgsConstructor

public class CartItemsController {
    private final CartItemService cartItemService;

    @PostMapping("create")
    public ResponseEntity<?> createCartItem(
            @Valid @RequestBody CartItemsDTO cartItemsDTO
    ) {
        try {
            CartItem cartItem = cartItemService.createCartItem(cartItemsDTO);
            CartItemResponse cartItemResponse = CartItemResponse.fromCartItem(cartItem);
            return ResponseEntity.ok(cartItemResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{cart_id}")
    public ResponseEntity<?> getAllCartItemByCartId(
            @PathVariable("cart_id") Long cartId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(
                    page,
                    limit,
                    Sort.by("cartItemId")
            );
            Page<CartItem> cartItemPage = cartItemService.getAllCartItemByCartId(pageRequest, cartId);
            Page<CartItemResponse> cartItemResponsePage = cartItemPage.map(cartItem -> CartItemResponse.fromCartItem(cartItem));
            List<CartItemResponse> cartItemResponseList = cartItemResponsePage.getContent();

            return ResponseEntity.ok(ListCartItemResponse.builder()
                            .cartItemResponseList(cartItemResponseList)
                            .totalPages(cartItemResponsePage.getTotalPages())
                    .build()) ;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

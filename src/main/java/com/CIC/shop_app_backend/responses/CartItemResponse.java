package com.CIC.shop_app_backend.responses;

import com.CIC.shop_app_backend.entity.CartItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    @JsonProperty("cart_item_id")
    private Long cartItemId;

    @JsonProperty("cart")
    private CartResponse cartResponse;

    @JsonProperty("product")
    private ProductResponse productResponse;

    private Long quantity;

    @JsonProperty("added_at")
    private LocalDateTime addedAt;

    public static CartItemResponse fromCartItem(CartItem cartItem){
        return CartItemResponse.builder()
                .cartItemId(cartItem.getCartItemId())
                .cartResponse(CartResponse.fromCart(cartItem.getCart()))
                .productResponse(ProductResponse.fromProduct(cartItem.getProduct()))
                .quantity(cartItem.getQuantity())
                .addedAt(cartItem.getAddedAt())
                .build();
    }
}

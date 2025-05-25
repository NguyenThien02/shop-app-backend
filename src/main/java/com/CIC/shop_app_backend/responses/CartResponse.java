package com.CIC.shop_app_backend.responses;

import com.CIC.shop_app_backend.entity.Cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_response")
    private UserResponse userResponse;

    public static CartResponse fromCart(Cart cart){
        return CartResponse.builder()
                .id(cart.getCartId())
                .userResponse(UserResponse.fromUser(cart.getUser()))
                .build();
    }
}

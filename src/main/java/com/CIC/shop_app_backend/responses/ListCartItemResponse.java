package com.CIC.shop_app_backend.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListCartItemResponse {
    private List<CartItemResponse> cartItemResponseList;
    private int totalPages;
}

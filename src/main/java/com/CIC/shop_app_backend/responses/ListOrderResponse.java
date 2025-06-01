package com.CIC.shop_app_backend.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListOrderResponse {
    private List<OrderResponse> orderResponses;
    private int totalPages;
}

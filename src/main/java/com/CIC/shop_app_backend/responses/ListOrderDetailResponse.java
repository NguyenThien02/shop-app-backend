package com.CIC.shop_app_backend.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListOrderDetailResponse {
    private List<OrderDetailResponse> orderDetailList;
    private int totalPages;
}

package com.CIC.shop_app_backend.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListProductResponse {
    private List<ProductResponse> productResponse;
    private int totalPages;
}

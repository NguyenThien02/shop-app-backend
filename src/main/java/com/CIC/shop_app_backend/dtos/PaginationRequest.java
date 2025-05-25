package com.CIC.shop_app_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PaginationRequest {
    private int page = 0;
    private int limit = 10;
    private Long category_id = 0L;
}

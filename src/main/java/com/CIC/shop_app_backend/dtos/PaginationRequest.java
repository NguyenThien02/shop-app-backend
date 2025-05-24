package com.CIC.shop_app_backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequest {
    private int page = 0;
    private int limit = 10;
    private Long category_id = 0L;
}

package com.CIC.shop_app_backend.dtos;

import com.CIC.shop_app_backend.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Long productId;
    private Long numberOfProducts;
    private Double totalMoney;
}

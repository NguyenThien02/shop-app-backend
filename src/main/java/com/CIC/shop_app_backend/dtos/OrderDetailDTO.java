package com.CIC.shop_app_backend.dtos;

import com.CIC.shop_app_backend.entity.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Number Of Products is required")
    @Min(value = 1, message = "Number Of Products must be at least 1")
    private Long numberOfProducts;
}

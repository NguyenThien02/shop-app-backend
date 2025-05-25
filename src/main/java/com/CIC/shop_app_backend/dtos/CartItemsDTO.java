package com.CIC.shop_app_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CartItemsDTO {
    @JsonProperty("cart_id")
    @NotNull(message = "Cart ID is required")
    private Long cartId;

    @JsonProperty("product_id")
    @NotNull(message = "Product ID is required")
    private Long productId;

    @JsonProperty("quantity")
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long quantity;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @PrePersist
    protected void onCreate(){
        addedAt = LocalDateTime.now();
    }
}

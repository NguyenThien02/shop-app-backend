package com.CIC.shop_app_backend.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ProductDTO {
    @JsonProperty("product_name")
    private String productName;

    private String description;

    private double price;

    @JsonProperty("stock_quantity")
    private Long stockQuantity;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("seller_id")
    private Long sellerId;
}

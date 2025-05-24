package com.CIC.shop_app_backend.responses;

import com.CIC.shop_app_backend.entity.Category;
import com.CIC.shop_app_backend.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    @JsonProperty("product_response_id")
    private Long productResponseId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("description")
    private String description;

    private double price;

    @JsonProperty("stock_quantity")
    private Long stockQuantity;

    @JsonProperty("image_url")
    private String imageUrl;

    private Category category;

    @JsonProperty("seller_respone")
    private UserResponse sellerRespone;

    public static ProductResponse fromProduct(Product product){
        return ProductResponse.builder()
                .productResponseId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory())
                .sellerRespone(UserResponse.fromUser(product.getSeller()))
                .build();
    }

}

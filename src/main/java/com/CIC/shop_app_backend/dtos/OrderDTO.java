package com.CIC.shop_app_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @JsonProperty("user_id")
    @NotNull(message = "User ID is required")
    private Long userId;

    @JsonProperty("seller_id")
    @NotNull(message = "Seller ID is required")
    private Long sellerId;

    @JsonProperty("shipping_addres")
    @NotBlank(message = "Shipping address is required")
    @Size(max = 500, message = "Shipping address cannot exceed 500 characters")
    private String shippingAddress;

    @JsonProperty("notes")
    @Size(max = 4000, message = "Notes cannot exceed 4000 characters")
    private String notes;

    @JsonProperty("voucher_id")
    @NotNull(message = "Voucher ID is required")
    private Long voucherId;

    @JsonProperty("order_details")
    @NotNull(message = "OrderDetailDTO is required")
    @Valid
    private List<OrderDetailDTO> orderDetails;
}

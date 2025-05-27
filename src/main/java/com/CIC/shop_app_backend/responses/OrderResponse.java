package com.CIC.shop_app_backend.responses;

import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.entity.enums.OrderStatus;
import com.CIC.shop_app_backend.entity.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("user_response")
    private UserResponse userResponse;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("total_amount")
    private Double totalAmount;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("order_status")
    private OrderStatus orderStatus;

    @JsonProperty("payment_status")
    private PaymentStatus paymentStatus;

    private String notes;

    public static OrderResponse fromOrder(Order order){
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userResponse(UserResponse.fromUser(order.getUser()))
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .notes(order.getNotes())
                .build();
    }
}

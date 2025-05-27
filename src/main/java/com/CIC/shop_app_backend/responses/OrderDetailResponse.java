package com.CIC.shop_app_backend.responses;

import com.CIC.shop_app_backend.entity.OrderDetail;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderDetailResponse {
    private Long orderDetailId;

    private OrderResponse orderResponse;

    private ProductResponse productResponse;

    private Long numberOfProducts;

    private Double totalMoney;

    public static OrderDetailResponse fromOderDetail(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .orderDetailId(orderDetail.getOrderDetailId())
                .orderResponse(OrderResponse.fromOrder(orderDetail.getOrder()))
                .productResponse(ProductResponse.fromProduct(orderDetail.getProduct()))
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .totalMoney(orderDetail.getTotalMoney())
                .build();
    }
}

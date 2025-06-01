package com.CIC.shop_app_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "number_of_products")
    private Long numberOfProducts;

    @Column(name = "total_money")
    private double totalMoney;

}

package com.CIC.shop_app_backend.entity;

import com.CIC.shop_app_backend.entity.enums.OrderStatus;
import com.CIC.shop_app_backend.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @PrePersist
    protected void orderDate(){
        orderDate = LocalDate.now();
    }

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 50)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 50)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    private String notes;
}

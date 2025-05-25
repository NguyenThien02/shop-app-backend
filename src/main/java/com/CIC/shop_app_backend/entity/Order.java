package com.CIC.shop_app_backend.entity;

import com.CIC.shop_app_backend.entity.enums.OrderStatus;
import com.CIC.shop_app_backend.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Orders")
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

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Enumerated(EnumType.STRING) // Lưu giá trị dạng chuỗi trong DB
    @Column(name = "order_status", nullable = false, length = 50)
    @ColumnDefault("'PENDING'") // Giá trị mặc định
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 50)
    @ColumnDefault("'UNPAID'")
    private PaymentStatus paymentStatus;

    private String notes;

}

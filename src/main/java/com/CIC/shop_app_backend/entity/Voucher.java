package com.CIC.shop_app_backend.entity;

import com.CIC.shop_app_backend.entity.enums.VoucherType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vouchers")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long voucherId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @Enumerated(EnumType.STRING)
    @Column(name = "voucher_type")
    private VoucherType type; // PERCENTAGE or FIXED

    @Column(nullable = false)
    private Double amount;

    private String description;

    @Column(name = "min_order_cost")
    private Double minOrderCost;

    @Column(name = "expiry_datetime")
    private LocalDate expiryDatetime;

    @Column(name = "limit_usage")
    private Integer limitUsage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }
}

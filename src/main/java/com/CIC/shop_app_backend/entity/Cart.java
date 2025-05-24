package com.CIC.shop_app_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Carts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Cart extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "user_id")
    private Long userId;
}

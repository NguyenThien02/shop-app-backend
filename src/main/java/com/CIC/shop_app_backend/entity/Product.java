package com.CIC.shop_app_backend.entity;

import com.CIC.shop_app_backend.responses.UserResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Products")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "stock_quantity")
    private Long stockQuantity;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User Seller;

}

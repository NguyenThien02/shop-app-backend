package com.CIC.shop_app_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Categories")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "description")
    private String description;
}

package com.CIC.shop_app_backend.repository;

import com.CIC.shop_app_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

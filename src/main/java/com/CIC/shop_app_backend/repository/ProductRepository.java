package com.CIC.shop_app_backend.repository;

import com.CIC.shop_app_backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p " +
            "WHERE (:categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:keyWord IS NULL OR " +
            "LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyWord, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyWord, '%')))")
    Page<Product> findProductsByCategory(
            @Param("categoryId") Long categoryId,
            @Param("keyWord") String keyWord,
            Pageable pageable
    );
    Page<Product> findBySellerUserId(Long sellerId, Pageable pageable);
}

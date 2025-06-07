package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.dtos.ProductDTO;
import com.CIC.shop_app_backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface IProductService {
    Page<Product> getByProductCategory(PageRequest pageRequest, Long categoryId) ;

    Product createProduct(ProductDTO productDTO);

    Product updateStockQuantityProduct(Long productId, Long quantity);

    Product getProductDetail(Long productId);

}

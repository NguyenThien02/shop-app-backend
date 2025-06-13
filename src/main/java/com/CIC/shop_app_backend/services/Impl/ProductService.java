package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.dtos.ProductDTO;
import com.CIC.shop_app_backend.entity.Category;
import com.CIC.shop_app_backend.entity.Product;
import com.CIC.shop_app_backend.entity.User;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.CategoryRepository;
import com.CIC.shop_app_backend.repository.ProductRepository;
import com.CIC.shop_app_backend.repository.UserRepository;
import com.CIC.shop_app_backend.responses.UserResponse;
import com.CIC.shop_app_backend.services.IInventoryService;
import com.CIC.shop_app_backend.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final IInventoryService inventoryService;

    public Page<Product> getByProductCategory(PageRequest pageRequest, Long categoryId, String keyWord) {
        return productRepository.findProductsByCategory(categoryId, keyWord, pageRequest);
    }

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        if(!categoryRepository.existsById(productDTO.getCategoryId())){
            throw new DataNotFoundException("Category does not exist");
        }
        if(!userRepository.existsById(productDTO.getSellerId())){
            throw new DataNotFoundException("Seller does not exist");
        }
        Category newCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Not found category"));

        User newSeller = userRepository.findById(productDTO.getSellerId())
                .orElseThrow(() -> new DataNotFoundException("Not found Seller"));
        UserResponse newUserResponse = UserResponse.fromUser(newSeller);

        Product newProduct = new Product();
        newProduct.setProductName(productDTO.getProductName());
        newProduct.setDescription(productDTO.getDescription());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setStockQuantity(productDTO.getStockQuantity());
        newProduct.setImageUrl(productDTO.getImageUrl());
        newProduct.setCategory(newCategory);
        newProduct.setSeller(newSeller);
        return productRepository.save(newProduct);
    }

    @Override
    public Product updateStockQuantityProduct(Long productId, Long quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có ID: " + productId));
        Long newStockQuantity = product.getStockQuantity() - quantity;
        product.setStockQuantity(newStockQuantity);

        inventoryService.setInventory(productId, newStockQuantity);
        return productRepository.save(product);
    }

    @Override
    public Product getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có ID: " + productId));
        return product;
    }

    @Override
    public Page<Product> getProductBySellerId(PageRequest pageRequest, Long sellerId) {
        return productRepository.findBySellerUserId(sellerId, pageRequest);
    }
}

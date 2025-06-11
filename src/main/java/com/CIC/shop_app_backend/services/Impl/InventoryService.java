package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.entity.Product;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.ProductRepository;
import com.CIC.shop_app_backend.services.IInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String INVENTORY_KEY = "inventory:";

    @Override
    public boolean checkInventory(Long productId, Long quantity) {
        String key = INVENTORY_KEY + productId;
        Long stock = 0L;
        Object redisStockObj = redisTemplate.opsForValue().get(key);
        if (redisStockObj != null) {
            stock = Long.parseLong(redisStockObj.toString());
        }else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có ID: " + productId));
            stock = product.getStockQuantity();
            redisTemplate.opsForValue().set(key, stock);
        }
        return stock >= quantity;
    }

    @Override
    public void setInventory(Long productId, Long stock) {
        redisTemplate.opsForValue().set(INVENTORY_KEY + productId, stock);
    }
}

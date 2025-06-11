package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.services.IInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String INVENTORY_KEY = "inventory:";

    @Override
    public boolean checkInventory(Long productId, Long quantity) {
        String key = INVENTORY_KEY + productId;
        Long stock = redisTemplate.opsForValue().get(key) != null ?
                Long.parseLong(redisTemplate.opsForValue().get(key).toString()) : 0;
        return stock >= quantity;
    }

    @Override
    public boolean deductInventory(Long productId, Long quantity) {
        String key = INVENTORY_KEY + productId;
        synchronized (this) {
            Long stock = redisTemplate.opsForValue().get(key) != null ?
                    Long.parseLong(redisTemplate.opsForValue().get(key).toString()) : 0;
            if (stock >= quantity) {
                redisTemplate.opsForValue().set(key, stock - quantity);
                return true;
            }
            return false;
        }
    }

    @Override
    public void setInventory(Long productId, Long quantity) {
        redisTemplate.opsForValue().set(INVENTORY_KEY + productId, quantity);
    }
}

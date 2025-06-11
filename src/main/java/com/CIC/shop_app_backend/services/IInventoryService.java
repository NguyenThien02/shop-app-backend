package com.CIC.shop_app_backend.services;

public interface IInventoryService {
    boolean checkInventory(Long productId, Long quantity);

    boolean deductInventory(Long productId, Long quantity);

    void setInventory(Long productId, Long quantity);
}

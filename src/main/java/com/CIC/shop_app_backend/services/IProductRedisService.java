package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.entity.Product;
import com.CIC.shop_app_backend.responses.ListProductResponse;
import com.CIC.shop_app_backend.responses.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductRedisService {

    ListProductResponse getProductByCategory(
            Long categoryId, PageRequest pageRequest, String keyWord) throws JsonProcessingException;

    void saveAllProducts(ListProductResponse listProductResponse,
                         Long categoryId,
                         PageRequest pageRequest,
                         String keyWord) throws JsonProcessingException;

    void clear();
}

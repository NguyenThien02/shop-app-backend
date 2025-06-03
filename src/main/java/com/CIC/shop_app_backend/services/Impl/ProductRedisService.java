package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.responses.ListProductResponse;
import com.CIC.shop_app_backend.responses.ProductResponse;
import com.CIC.shop_app_backend.services.IProductRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRedisService implements IProductRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    private String getKeyFrom(Long categoryId,
                              PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        return  String.format("all_products:category:%d, page:%d, size:%d", categoryId, pageNumber, pageSize);
    }

    @Override
    public ListProductResponse getProductByCategory(Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(categoryId, pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        ListProductResponse productResponses =
                json != null ?
                        redisObjectMapper.readValue(json, new TypeReference<ListProductResponse>() {})
                        : null;
        return productResponses;
    }

    @Override
    public void saveAllProducts(ListProductResponse listProductResponse,Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(categoryId, pageRequest);
        String json = redisObjectMapper.writeValueAsString(listProductResponse);
        redisTemplate.opsForValue().set(key, json);
    }
}

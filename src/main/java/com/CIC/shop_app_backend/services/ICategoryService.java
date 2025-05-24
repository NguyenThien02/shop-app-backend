package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.entity.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
}

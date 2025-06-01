package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.entity.Category;
import com.CIC.shop_app_backend.repository.CategoryRepository;
import com.CIC.shop_app_backend.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}

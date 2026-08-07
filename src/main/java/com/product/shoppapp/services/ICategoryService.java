package com.product.shoppapp.services;

import com.product.shoppapp.dtos.CategoryDTO;
import com.product.shoppapp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();

    Category updateCategory(Long categoryId, CategoryDTO categoryDTO);

    void deleteCategory(Long id);
}

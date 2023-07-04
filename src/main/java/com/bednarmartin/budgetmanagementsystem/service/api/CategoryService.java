package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {

    void addCategory(CategoryRequest request);
    @Transactional
    void updateCategory(long id, CategoryRequest request);

    void deleteCategoryById(long id);
    @Transactional
    void deleteCategoryByName(String name);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(long id);

    Category getCategoryByName(String name);
}

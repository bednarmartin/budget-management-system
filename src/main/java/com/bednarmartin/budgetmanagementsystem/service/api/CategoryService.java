package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {

    CategoryResponse addCategory(CategoryRequest request);

    @Transactional
    CategoryResponse updateCategory(long id, CategoryRequest request);

    void deleteCategoryById(long id);

    @Transactional
    void deleteCategoryByName(String name);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(long id);

    Category getCategoryByName(String name);

    List<AmountSumByCategoryResponse> getAmountSumByCategory();

    AmountSumByCategoryResponse getAmountSumByCategoryByCategoryName(String categoryName);
}

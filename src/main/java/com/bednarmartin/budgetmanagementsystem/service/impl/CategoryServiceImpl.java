package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.annotations.LogMethod;
import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.repository.CategoryRepository;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.mapper.ResponseObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    private final ResponseObjectMapper responseObjectMapper;
    private final String errorMessage = "Such Category not in database";

    @LogMethod
    @Override
    public CategoryResponse addCategory(CategoryRequest request) {
        LocalDateTime actualTime = LocalDateTime.now();
        Category category = Category.builder()
                .name(request.getName())
                .transactionType(request.getTransactionType())
                .dateCreated(actualTime)
                .dateUpdated(actualTime)
                .build();

        repository.save(category);
        return responseObjectMapper.mapToCategoryResponse(category);
    }

    @LogMethod
    @Override
    public CategoryResponse updateCategory(long id, CategoryRequest request) {
        LocalDateTime actualTime = LocalDateTime.now();
        repository.updateCategoryById(id, request.getName(), request.getTransactionType(), actualTime);

        Category category = repository.findById(id).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        return responseObjectMapper.mapToCategoryResponse(category);
    }

    @LogMethod
    @Override
    public void deleteCategoryById(long id) {
        repository.deleteById(id);
    }

    @LogMethod
    @Override
    public void deleteCategoryByName(String name) {
        repository.deleteByName(name);
    }

    @LogMethod
    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = repository.findAll();
        return categories.stream().map(responseObjectMapper::mapToCategoryResponse).toList();
    }

    @LogMethod
    @Override
    public CategoryResponse getCategoryById(long id) {
        String errorMessage = "Such Category not in database";
        Category category = repository.findById(id).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        return responseObjectMapper.mapToCategoryResponse(category);
    }

    @LogMethod
    @Override
    public Category getCategoryByName(String name) {
        String errorMessage = "Such Category is not in the Database";
        return repository.findByName(name)
                .orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
    }

    @LogMethod
    @Override
    public List<AmountSumByCategoryResponse> getAmountSumByCategory() {
        List<AmountSumByCategoryResponse> responses = repository.getAllAmountSumsByCategory();

        for (AmountSumByCategoryResponse response : responses) {
            if (response.getSum() == null) {
                response.setSum(BigDecimal.ZERO);
            }
        }

        return responses;
    }

    @LogMethod
    @Override
    public AmountSumByCategoryResponse getAmountSumByCategoryByCategoryName(String categoryName) {
        AmountSumByCategoryResponse response = repository.getAllAmountSumsByCategoryByCategoryName(categoryName)
                .orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        if (response.getSum() == null) {
            response.setSum(BigDecimal.ZERO);
        }
        return response;
    }


}

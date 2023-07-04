package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.repository.CategoryRepository;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;


    @Override
    public void addCategory(CategoryRequest request) {
        log.debug("addCategory with parameter: {} called", request);

        LocalDateTime actualTime = LocalDateTime.now();
        Category category = Category.builder()
                .name(request.getName())
                .transactionType(request.getTransactionType())
                .dateCreated(actualTime)
                .dateUpdated(actualTime)
                .build();

        repository.save(category);

        log.info("Category with id: {} saved", category.getId());
        log.debug("Category: {} saved", category);
    }

    @Override
    public void updateCategory(long id, CategoryRequest request) {
        log.debug("updateCategory with parameters: {}, {} called", id, request);

        LocalDateTime actualTime = LocalDateTime.now();
        repository.updateCategoryById(id, request.getName(), request.getTransactionType(), actualTime);

        log.info("Category with id: {} updated", id);
        log.debug("Category: {} updated", request);
    }

    @Override
    public void deleteCategoryById(long id) {
        log.debug("deleteCategoryById with parameter: {} called", id);
        repository.deleteById(id);
        log.info("Category with id: {} deleted", id);
    }

    @Override
    public void deleteCategoryByName(String name) {
        log.debug("deleteCategoryByName with parameter: {} called", name);
        repository.deleteByName(name);
        log.info("Category with name: {} deleted", name);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        log.debug("getAllCategories called");
        List<Category> categories = repository.findAll();

        log.info("all CategoryResponse returned");
        return categories.stream().map(this::mapToCategoryResponse).toList();
    }

    @Override
    public CategoryResponse getCategoryById(long id) {
        log.debug("getCategoryById with parameter: {} called", id);

        String errorMessage = "Such ExpenseCategory not in database";
        Category category = repository.findById(id).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        CategoryResponse categoryResponse = mapToCategoryResponse(category);

        log.debug("CategoryResponse: {} returned", categoryResponse);
        log.info("CategoryResponse with id: {} returned", id);

        return categoryResponse;
    }

    @Override
    public Category getCategoryByName(String name) {
        log.debug("getCategoryByName with parameter: {} called", name);

        String errorMessage = "Such ExpenseCategory is not in the Database";
        Category category = repository.findByName(name)
                .orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        log.debug("Category: {} returned", category);
        log.info("Category with id: {} returned", category.getId());

        return category;
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .dateCreated(category.getDateCreated())
                .transactionType(category.getTransactionType())
                .build();
    }

}

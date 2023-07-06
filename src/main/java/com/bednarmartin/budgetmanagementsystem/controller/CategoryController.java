package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getCategoryById(@PathVariable long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addCategory(@RequestBody CategoryRequest request) {
        categoryService.addCategory(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable long id) {
        categoryService.deleteCategoryById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCategory(@PathVariable long id, @RequestBody CategoryRequest request) {
        categoryService.updateCategory(id, request);
    }

    @GetMapping("/balances")
    @ResponseStatus(HttpStatus.OK)
    public List<AmountSumByCategoryResponse> updateCategory() {
        return categoryService.getAmountSumByCategory();
    }

    @GetMapping("/balances/{category_name}")
    @ResponseStatus(HttpStatus.OK)
    public AmountSumByCategoryResponse updateCategory(@PathVariable("category_name") String categoryName) {
        return categoryService.getAmountSumByCategoryByCategoryName(categoryName);
    }
}


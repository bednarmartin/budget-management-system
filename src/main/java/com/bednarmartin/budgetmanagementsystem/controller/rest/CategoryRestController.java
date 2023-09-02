package com.bednarmartin.budgetmanagementsystem.controller.rest;

import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable long id) {
        return new ResponseEntity<>(categoryService.getCategoryById(id), OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addCategory(@Valid @RequestBody CategoryRequest request) {
        String message = "Category created successfully";
        categoryService.addCategory(request);
        return new ResponseEntity<>(Map.of("message", message), CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable long id) {
        String message = "Category deleted successfully";
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(Map.of("message", message), OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateCategory(@PathVariable long id,
                                                              @Valid @RequestBody CategoryRequest request) {
        String message = "Category updated successfully";
        categoryService.updateCategory(id, request);
        return new ResponseEntity<>(Map.of("message", message), OK);
    }

    @GetMapping("/balances")
    public ResponseEntity<List<AmountSumByCategoryResponse>> getAllBalances() {
        return new ResponseEntity<>(categoryService.getAmountSumByCategory(), OK);
    }

    @GetMapping("/balances/{category_name}")
    public ResponseEntity<AmountSumByCategoryResponse> getBalancesByName(
            @PathVariable("category_name") String categoryName) {
        return new ResponseEntity<>(categoryService.getAmountSumByCategoryByCategoryName(categoryName), OK);
    }
}


package com.bednarmartin.budgetmanagementsystem.controller.rest;

import com.bednarmartin.budgetmanagementsystem.exception.DatabaseDuplicateException;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable long id) {
        CategoryResponse categoryResponse = null;
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            categoryResponse = categoryService.getCategoryById(id);
        } catch (SuchElementNotInDatabaseException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(categoryResponse, httpStatus);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            categoryResponses = categoryService.getAllCategories();
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(categoryResponses, httpStatus);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addCategory(@Valid @RequestBody CategoryRequest request) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        String message = "Category created successfully";

        try {
            categoryService.addCategory(request);
        } catch (DatabaseDuplicateException e) {
            message = e.getMessage();
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            message = "Something went wrong";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(Map.of("message", message), httpStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable long id) {
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Category deleted successfully";

        try {
            categoryService.deleteCategoryById(id);
        } catch (SuchElementNotInDatabaseException e) {
            message = e.getMessage();
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            message = "Something went wrong";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(Map.of("message", message), httpStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateCategory(@PathVariable long id, @Valid @RequestBody CategoryRequest request) {
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Category updated successfully";
        try {
            categoryService.updateCategory(id, request);
        } catch (SuchElementNotInDatabaseException e) {
            message = e.getMessage();
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            message = "Something went wrong";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(Map.of("message", message), httpStatus);
    }

    @GetMapping("/balances")
    public ResponseEntity<List<AmountSumByCategoryResponse>> getAllBalances() {
        List<AmountSumByCategoryResponse> responses = new ArrayList<>();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            responses = categoryService.getAmountSumByCategory();
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(responses, httpStatus);
    }

    @GetMapping("/balances/{category_name}")
    public ResponseEntity<AmountSumByCategoryResponse> getBalancesByName(@PathVariable("category_name") String categoryName) {
        AmountSumByCategoryResponse response = null;
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            response = categoryService.getAmountSumByCategoryByCategoryName(categoryName);
        } catch (SuchElementNotInDatabaseException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, httpStatus);
    }
}


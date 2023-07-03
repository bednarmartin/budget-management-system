package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.service.api.ExpenseCategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseCategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseCategoryResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense/category")
@RequiredArgsConstructor
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ExpenseCategoryResponse getExpenseCategoryById(@PathVariable long id) {
        return expenseCategoryService.getExpenseCategoryById(id);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExpenseCategoryResponse> getAllExpenseCategories() {
        return expenseCategoryService.getAllExpenseCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addExpenseCategory(@RequestBody ExpenseCategoryRequest expenseCategoryRequest) {
        expenseCategoryService.addExpenseCategory(expenseCategoryRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteExpenseCategory(@PathVariable long id) {
        expenseCategoryService.deleteExpenseCategoryById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateExpenseCategory(@PathVariable long id, @RequestBody ExpenseCategoryRequest expenseCategoryRequest) {
        expenseCategoryService.updateExpenseCategory(id, expenseCategoryRequest);
    }
}


package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.db.model.ExpenseCategory;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateExpenseCategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateExpenseCategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseCategoryResponse;

import java.util.List;

public interface ExpenseCategoryService {

    void addExpenseCategory(CreateExpenseCategoryRequest expenseCategoryRequest);

    void updateExpenseCategory(long id, UpdateExpenseCategoryRequest expenseCategoryRequest);

    void deleteExpenseCategoryById(long id);

    List<ExpenseCategoryResponse> getAllExpenseCategories();

    ExpenseCategoryResponse getExpenseCategoryById(long id);

    ExpenseCategory getExpenseCategoryByName(String name);
}

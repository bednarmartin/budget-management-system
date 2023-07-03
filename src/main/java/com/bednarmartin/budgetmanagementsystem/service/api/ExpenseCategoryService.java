package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.db.model.ExpenseCategory;
import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseCategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseCategoryResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExpenseCategoryService {

    void addExpenseCategory(ExpenseCategoryRequest expenseCategoryRequest);
    @Transactional
    void updateExpenseCategory(long id, ExpenseCategoryRequest expenseCategoryRequest);

    void deleteExpenseCategoryById(long id);
    @Transactional
    void deleteExpenseCategoryByName(String name);

    List<ExpenseCategoryResponse> getAllExpenseCategories();

    ExpenseCategoryResponse getExpenseCategoryById(long id);

    ExpenseCategory getExpenseCategoryByName(String name);
}

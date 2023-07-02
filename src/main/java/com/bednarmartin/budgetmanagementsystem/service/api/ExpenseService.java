package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateExpenseRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateExpenseRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseResponse;

import java.util.List;

public interface ExpenseService {

    void addExpense(CreateExpenseRequest createExpenseRequest);

    void updateExpense(long id, UpdateExpenseRequest expenseRequest);

    void deleteExpenseById(long id);

    List<ExpenseResponse> getAllExpenses();

    ExpenseResponse getExpenseById(long id);
}

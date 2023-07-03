package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExpenseService {

    void addExpense(ExpenseRequest expenseRequest);
    @Transactional
    void updateExpense(long id, ExpenseRequest expenseRequest);

    void deleteExpenseById(long id);

    List<ExpenseResponse> getAllExpenses();

    ExpenseResponse getExpenseById(long id);
}

package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.db.model.Expense;
import com.bednarmartin.budgetmanagementsystem.db.model.ExpenseCategory;
import com.bednarmartin.budgetmanagementsystem.db.repository.ExpenseRepository;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.ExpenseCategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.ExpenseService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository repository;

    private final ExpenseCategoryService expenseCategoryService;

    @Override
    public void addExpense(ExpenseRequest expenseRequest) {
        log.debug("addExpense with parameter: {} called", expenseRequest);

        ExpenseCategory expenseCategory = expenseCategoryService.getExpenseCategoryByName(expenseRequest.getCategoryName());

        LocalDateTime actualTime = LocalDateTime.now();
        Expense expense = Expense.builder()
                .amount(expenseRequest.getAmount())
                .category(expenseCategory)
                .description(expenseRequest.getDescription())
                .dateCreated(actualTime)
                .dateUpdated(actualTime)
                .build();
        repository.save(expense);

        log.info("Expense with id: {} saved", expense.getId());
        log.debug("Expense: {} saved", expense);

    }

    @Override
    public void updateExpense(long id, ExpenseRequest expenseRequest) {
        log.debug("updateExpense with parameters: {}, {} called", id, expenseRequest);

        ExpenseCategory expenseCategory = expenseCategoryService.getExpenseCategoryByName(
                expenseRequest.getCategoryName());

        LocalDateTime actualTime = LocalDateTime.now();
        repository.updateExpenseCategoryById(id,
                expenseRequest.getAmount(),
                expenseRequest.getDescription(),
                expenseCategory,
                actualTime);

        log.info("Expense with id: {} updated", id);
        log.debug("Expense: {} updated", expenseRequest);
    }

    @Override
    public void deleteExpenseById(long id) {
        log.debug("deleteExpenseById with parameter: {} called", id);
        repository.deleteById(id);
        log.info("Expense with id: {} deleted", id);
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        log.debug("getAllExpenses called");
        List<Expense> expenses = repository.findAll();

        log.info("all ExpenseResponses returned");
        return expenses.stream().map(this::mapToExpenseResponse).toList();
    }

    @Override
    public ExpenseResponse getExpenseById(long id) {
        log.debug("getExpenseById with parameter: {} called", id);

        String errorMessage = "Such Expense not in database";
        Expense expense = repository.findById(id).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        ExpenseResponse expenseResponse = mapToExpenseResponse(expense);

        log.debug("ExpenseResponse: {} returned", expenseResponse);
        log.info("ExpenseResponse with id: {} returned", id);

        return expenseResponse;
    }

    private ExpenseResponse mapToExpenseResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .category(expense.getCategory())
                .dateCreated(expense.getDateCreated())
                .build();
    }

}

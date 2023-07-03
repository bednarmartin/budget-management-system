package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.db.model.ExpenseCategory;
import com.bednarmartin.budgetmanagementsystem.db.repository.ExpenseCategoryRepository;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.ExpenseCategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseCategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseCategoryResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository repository;


    @Override
    public void addExpenseCategory(ExpenseCategoryRequest expenseCategoryRequest) {
        log.debug("addExpenseCategory with parameter: {} called", expenseCategoryRequest);

        LocalDateTime actualTime = LocalDateTime.now();
        ExpenseCategory expenseCategory = ExpenseCategory.builder()
                .name(expenseCategoryRequest.getName())
                .dateCreated(actualTime)
                .dateUpdated(actualTime)
                .build();

        repository.save(expenseCategory);

        log.info("ExpenseCategory with id: {} saved", expenseCategory.getId());
        log.debug("ExpenseCategory: {} saved", expenseCategory);
    }

    @Override
    public void updateExpenseCategory(long id, ExpenseCategoryRequest expenseCategoryRequest) {
        log.debug("updateExpenseCategory with parameters: {}, {} called", id, expenseCategoryRequest);

        LocalDateTime actualTime = LocalDateTime.now();
        repository.updateExpenseCategoryById(id, expenseCategoryRequest.getName(), actualTime);

        log.info("ExpenseCategory with id: {} updated", id);
        log.debug("ExpenseCategory: {} updated", expenseCategoryRequest);
    }

    @Override
    public void deleteExpenseCategoryById(long id) {
        log.debug("deleteExpenseCategoryById with parameter: {} called", id);
        repository.deleteById(id);
        log.info("ExpenseCategory with id: {} deleted", id);
    }

    @Override
    public void deleteExpenseCategoryByName(String name) {
        log.debug("deleteExpenseCategoryByName with parameter: {} called", name);
        repository.deleteByName(name);
        log.info("ExpenseCategory with name: {} deleted", name);
    }

    @Override
    public List<ExpenseCategoryResponse> getAllExpenseCategories() {
        log.debug("getAllExpenseCategories called");
        List<ExpenseCategory> expenses = repository.findAll();

        log.info("all ExpenseCategoryResponse returned");
        return expenses.stream().map(this::mapToExpenseCategoryResponse).toList();
    }

    @Override
    public ExpenseCategoryResponse getExpenseCategoryById(long id) {
        log.debug("getExpenseCategoryById with parameter: {} called", id);

        String errorMessage = "Such ExpenseCategory not in database";
        ExpenseCategory expenseCategory = repository.findById(id).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        ExpenseCategoryResponse expenseCategoryResponse = mapToExpenseCategoryResponse(expenseCategory);

        log.debug("ExpenseCategoryResponse: {} returned", expenseCategoryResponse);
        log.info("ExpenseCategoryResponse with id: {} returned", id);

        return expenseCategoryResponse;
    }

    @Override
    public ExpenseCategory getExpenseCategoryByName(String name) {
        log.debug("getExpenseCategoryByName with parameter: {} called", name);

        String errorMessage = "Such ExpenseCategory is not in the Database";
        ExpenseCategory expenseCategory = repository.findByName(name)
                .orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        log.debug("ExpenseCategory: {} returned", expenseCategory);
        log.info("ExpenseCategory with id: {} returned", expenseCategory.getId());

        return expenseCategory;
    }

    private ExpenseCategoryResponse mapToExpenseCategoryResponse(ExpenseCategory expenseCategory) {
        return ExpenseCategoryResponse.builder()
                .id(expenseCategory.getId())
                .name(expenseCategory.getName())
                .dateCreated(expenseCategory.getDateCreated())
                .build();
    }

}

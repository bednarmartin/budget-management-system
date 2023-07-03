package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.service.api.ExpenseService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ExpenseResponse getExpenseById(@PathVariable long id) {
        return expenseService.getExpenseById(id);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExpenseResponse> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addExpense(@RequestBody ExpenseRequest expenseRequest) {
        expenseService.addExpense(expenseRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteExpense(@PathVariable long id) {
        expenseService.deleteExpenseById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateExpense(@PathVariable long id, @RequestBody ExpenseRequest expenseRequest) {
        expenseService.updateExpense(id, expenseRequest);
    }


}

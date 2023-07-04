package com.bednarmartin.budgetmanagementsystem.service;

import com.bednarmartin.budgetmanagementsystem.service.api.ExpenseCategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.ExpenseService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseCategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ExpenseServiceTests {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    ExpenseCategoryService expenseCategoryService;

    @BeforeEach
    public void init() {
        ExpenseCategoryRequest request = ExpenseCategoryRequest.builder()
                .name("Utilities")
                .build();
        expenseCategoryService.addExpenseCategory(request);
    }

    @Test
    public void testAddOneExpense() throws InterruptedException {
        ExpenseRequest request = ExpenseRequest.builder()
                .amount(BigDecimal.valueOf(51.99))
                .description("Electricity bill")
                .categoryName("Utilities")
                .build();

        LocalDateTime start = LocalDateTime.now();
        Thread.sleep(100);
        expenseService.addExpense(request);
        Thread.sleep(100);
        LocalDateTime end = LocalDateTime.now();

        List<ExpenseResponse> responses = expenseService.getAllExpenses();
        LocalDateTime dateCreated = responses.get(0).getDateCreated();
        boolean dateCreatedIsGood = dateCreated.isAfter(start) && dateCreated.isBefore(end);

        Assertions.assertEquals(1, responses.size());
        Assertions.assertEquals(1L, responses.get(0).getId());
        Assertions.assertEquals(responses.get(0).getAmount(), BigDecimal.valueOf(51.99));
        Assertions.assertEquals(responses.get(0).getDescription(), "Electricity bill");
        Assertions.assertEquals(responses.get(0).getCategory().getName(), "Utilities");
        Assertions.assertTrue(dateCreatedIsGood);

    }

    @Test
    public void testAddMoreExpenseCategories() throws InterruptedException {
        String[] descriptions = {"Electricity bill", "Gas bill", "TV bill"};
        BigDecimal[] amounts = {BigDecimal.valueOf(51.99), BigDecimal.valueOf(23.59), BigDecimal.valueOf(12.53)};

        LocalDateTime start = LocalDateTime.now();
        Thread.sleep(100);
        for (int i = 0; i < descriptions.length; i++) {
            ExpenseRequest request = ExpenseRequest.builder()
                    .amount(amounts[i])
                    .description(descriptions[i])
                    .categoryName("Utilities")
                    .build();
            expenseService.addExpense(request);
        }
        Thread.sleep(100);
        LocalDateTime end = LocalDateTime.now();

        List<ExpenseResponse> responses = expenseService.getAllExpenses();

        for (ExpenseResponse response : responses) {
            LocalDateTime dateCreated = response.getDateCreated();
            boolean dateCreatedIsGood = dateCreated.isAfter(start) && dateCreated.isBefore(end);
            Assertions.assertTrue(dateCreatedIsGood);
        }

        Assertions.assertEquals(3, responses.size());
        for (int i = 0; i < responses.size(); i++) {
            Assertions.assertEquals((long) i + 1, responses.get(i).getId());
            Assertions.assertEquals("Utilities", responses.get(i).getCategory().getName());
            Assertions.assertEquals(descriptions[i], responses.get(i).getDescription());
            Assertions.assertEquals(amounts[i], responses.get(i).getAmount());
        }
    }

    @Test
    public void testUpdateExpenseCategory() {
        ExpenseRequest request = ExpenseRequest.builder()
                .amount(BigDecimal.valueOf(51.99))
                .description("Electricity bill")
                .categoryName("Utilities")
                .build();

        expenseService.addExpense(request);
        ExpenseResponse response = expenseService.getAllExpenses().get(0);

        ExpenseRequest updateRequest = ExpenseRequest.builder()
                .categoryName("Utilities")
                .description("Candy")
                .amount(BigDecimal.valueOf(23.19))
                .build();

        expenseService.updateExpense(response.getId(), updateRequest);

        ExpenseResponse updatedResponse = expenseService.getExpenseById(response.getId());
        Assertions.assertEquals("Utilities", updatedResponse.getCategory().getName());
        Assertions.assertEquals(response.getDateCreated(), updatedResponse.getDateCreated());
        Assertions.assertEquals("Candy", updatedResponse.getDescription());
        Assertions.assertEquals(BigDecimal.valueOf(23.19), updatedResponse.getAmount());

    }

    @Test
    public void testDeleteExpenseCategoryById() {
        ExpenseRequest request = ExpenseRequest.builder()
                .amount(BigDecimal.valueOf(51.99))
                .description("Electricity bill")
                .categoryName("Utilities")
                .build();

        expenseService.addExpense(request);
        ExpenseResponse response = expenseService.getAllExpenses().get(0);

        expenseService.deleteExpenseById(response.getId());
        List<ExpenseResponse> responses = expenseService.getAllExpenses();

        Assertions.assertTrue(responses.isEmpty());

    }

}

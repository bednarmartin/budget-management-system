package com.bednarmartin.budgetmanagementsystem.service;

import com.bednarmartin.budgetmanagementsystem.db.model.ExpenseCategory;
import com.bednarmartin.budgetmanagementsystem.service.api.ExpenseCategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseCategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseCategoryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ExpenseCategoryServiceTests {

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @Test
    public void testAddOneExpenseCategory() throws InterruptedException {
        ExpenseCategoryRequest request = ExpenseCategoryRequest.builder()
                .name("Utilities")
                .build();

        LocalDateTime start = LocalDateTime.now();
        Thread.sleep(100);
        expenseCategoryService.addExpenseCategory(request);
        Thread.sleep(100);
        LocalDateTime end = LocalDateTime.now();

        List<ExpenseCategoryResponse> responses = expenseCategoryService.getAllExpenseCategories();
        LocalDateTime dateCreated = responses.get(0).getDateCreated();
        boolean dateCreatedIsGood = dateCreated.isAfter(start) && dateCreated.isBefore(end);

        Assertions.assertEquals(1, responses.size());
        Assertions.assertEquals(1L, responses.get(0).getId());
        Assertions.assertEquals("Utilities", responses.get(0).getName());
        Assertions.assertTrue(dateCreatedIsGood);

    }

    @Test
    public void testAddMoreExpenseCategories() throws InterruptedException {
        String[] names = {"Utilities", "Groceries", "Health"};
        ExpenseCategoryRequest request1 = ExpenseCategoryRequest.builder()
                .name(names[0])
                .build();

        ExpenseCategoryRequest request2 = ExpenseCategoryRequest.builder()
                .name(names[1])
                .build();

        ExpenseCategoryRequest request3 = ExpenseCategoryRequest.builder()
                .name(names[2])
                .build();

        LocalDateTime start = LocalDateTime.now();
        Thread.sleep(100);
        expenseCategoryService.addExpenseCategory(request1);
        expenseCategoryService.addExpenseCategory(request2);
        expenseCategoryService.addExpenseCategory(request3);
        Thread.sleep(100);
        LocalDateTime end = LocalDateTime.now();

        List<ExpenseCategoryResponse> responses = expenseCategoryService.getAllExpenseCategories();

        for (ExpenseCategoryResponse response : responses) {
            LocalDateTime dateCreated = response.getDateCreated();
            boolean dateCreatedIsGood = dateCreated.isAfter(start) && dateCreated.isBefore(end);
            Assertions.assertTrue(dateCreatedIsGood);
        }

        Assertions.assertEquals(3, responses.size());
        for (int i = 0; i < responses.size(); i++) {
            Assertions.assertEquals((long) i + 1, responses.get(i).getId());
            Assertions.assertEquals(names[i], responses.get(i).getName());
        }
    }

    @Test
    public void testUpdateExpenseCategory() {
        ExpenseCategoryRequest createRequest = ExpenseCategoryRequest.builder()
                .name("Utilities")
                .build();

        expenseCategoryService.addExpenseCategory(createRequest);
        ExpenseCategoryResponse response = expenseCategoryService.getAllExpenseCategories().get(0);

        ExpenseCategoryRequest updateRequest = ExpenseCategoryRequest.builder().name("Groceries").build();
        expenseCategoryService.updateExpenseCategory(response.getId(), updateRequest);

        ExpenseCategoryResponse updatedResponse = expenseCategoryService.getExpenseCategoryById(response.getId());
        Assertions.assertEquals("Groceries", updatedResponse.getName());
        Assertions.assertEquals(response.getDateCreated(), updatedResponse.getDateCreated());

    }

    @Test
    public void testDeleteExpenseCategoryById() {
        ExpenseCategoryRequest createRequest = ExpenseCategoryRequest.builder()
                .name("Utilities")
                .build();

        expenseCategoryService.addExpenseCategory(createRequest);
        ExpenseCategoryResponse response = expenseCategoryService.getAllExpenseCategories().get(0);

        expenseCategoryService.deleteExpenseCategoryById(response.getId());
        List<ExpenseCategoryResponse> responses = expenseCategoryService.getAllExpenseCategories();

        Assertions.assertTrue(responses.isEmpty());

    }

    @Test
    public void testDeleteExpenseCategoryByName() {
        String name = "Utilities";
        ExpenseCategoryRequest createRequest = ExpenseCategoryRequest.builder()
                .name(name)
                .build();

        expenseCategoryService.addExpenseCategory(createRequest);

        expenseCategoryService.deleteExpenseCategoryByName(name);
        List<ExpenseCategoryResponse> responses = expenseCategoryService.getAllExpenseCategories();

        Assertions.assertTrue(responses.isEmpty());
    }

    @Test
    public void testGetExpenseCategoryByName() {
        String name = "Utilities";
        ExpenseCategoryRequest createRequest = ExpenseCategoryRequest.builder()
                .name(name)
                .build();

        expenseCategoryService.addExpenseCategory(createRequest);
        ExpenseCategory expenseCategory = expenseCategoryService.getExpenseCategoryByName(name);

        Assertions.assertNotNull(expenseCategory);
        Assertions.assertEquals(name, expenseCategory.getName());

    }
}

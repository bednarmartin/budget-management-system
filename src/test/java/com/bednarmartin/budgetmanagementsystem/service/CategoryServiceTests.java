package com.bednarmartin.budgetmanagementsystem.service;

import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.TransactionService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.TransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class CategoryServiceTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private AccountService accountService;

    @Test
    public void testAddOneCategory() throws InterruptedException {
        CategoryRequest request = CategoryRequest.builder()
                .name("Utilities")
                .transactionType(TransactionType.EXPENSE)
                .build();

        LocalDateTime start = LocalDateTime.now();
        Thread.sleep(100);
        categoryService.addCategory(request);
        Thread.sleep(100);
        LocalDateTime end = LocalDateTime.now();

        List<CategoryResponse> responses = categoryService.getAllCategories();
        LocalDateTime dateCreated = responses.get(0).getDateCreated();
        boolean dateCreatedIsGood = dateCreated.isAfter(start) && dateCreated.isBefore(end);

        Assertions.assertEquals(1, responses.size());
        Assertions.assertEquals(1L, responses.get(0).getId());
        Assertions.assertEquals("Utilities", responses.get(0).getName());
        Assertions.assertEquals(TransactionType.EXPENSE, responses.get(0).getTransactionType());
        Assertions.assertTrue(dateCreatedIsGood);

    }

    @Test
    public void testAddMoreCategories() throws InterruptedException {
        String[] names = {"Utilities", "Groceries", "Health"};
        TransactionType[] types = {TransactionType.EXPENSE, TransactionType.INCOME, TransactionType.TRANSFER};

        LocalDateTime start = LocalDateTime.now();
        Thread.sleep(100);
        for (int i = 0; i < names.length; i++) {
            CategoryRequest request = CategoryRequest.builder()
                    .name(names[i])
                    .transactionType(types[i])
                    .build();
            categoryService.addCategory(request);
        }
        Thread.sleep(100);
        LocalDateTime end = LocalDateTime.now();

        List<CategoryResponse> responses = categoryService.getAllCategories();

        for (CategoryResponse response : responses) {
            LocalDateTime dateCreated = response.getDateCreated();
            boolean dateCreatedIsGood = dateCreated.isAfter(start) && dateCreated.isBefore(end);
            Assertions.assertTrue(dateCreatedIsGood);
        }

        Assertions.assertEquals(3, responses.size());
        for (int i = 0; i < responses.size(); i++) {
            Assertions.assertEquals((long) i + 1, responses.get(i).getId());
            Assertions.assertEquals(names[i], responses.get(i).getName());
            Assertions.assertEquals(types[i], responses.get(i).getTransactionType());
        }
    }

    @Test
    public void testUpdateCategory() {
        CategoryRequest createRequest = CategoryRequest.builder()
                .name("Utilities")
                .transactionType(TransactionType.EXPENSE)
                .build();

        categoryService.addCategory(createRequest);
        CategoryResponse response = categoryService.getAllCategories().get(0);

        CategoryRequest updateRequest = CategoryRequest.builder()
                .name("Groceries")
                .transactionType(TransactionType.EXPENSE).build();
        categoryService.updateCategory(response.getId(), updateRequest);

        CategoryResponse updatedResponse = categoryService.getCategoryById(response.getId());
        Assertions.assertEquals("Groceries", updatedResponse.getName());
        Assertions.assertEquals(createRequest.getTransactionType(), updatedResponse.getTransactionType());
        Assertions.assertEquals(response.getDateCreated(), updatedResponse.getDateCreated());

    }

    @Test
    public void testDeleteCategoryById() {
        CategoryRequest createRequest = CategoryRequest.builder()
                .name("Utilities")
                .transactionType(TransactionType.EXPENSE)
                .build();

        categoryService.addCategory(createRequest);
        CategoryResponse response = categoryService.getAllCategories().get(0);

        categoryService.deleteCategoryById(response.getId());
        List<CategoryResponse> responses = categoryService.getAllCategories();

        Assertions.assertTrue(responses.isEmpty());

    }

    @Test
    public void testDeleteCategoryByName() {
        String name = "Utilities";
        CategoryRequest createRequest = CategoryRequest.builder()
                .name(name)
                .transactionType(TransactionType.EXPENSE)
                .build();

        categoryService.addCategory(createRequest);

        categoryService.deleteCategoryByName(name);
        List<CategoryResponse> responses = categoryService.getAllCategories();

        Assertions.assertTrue(responses.isEmpty());
    }

    @Test
    public void testGetCategoryByName() {
        String name = "Utilities";
        CategoryRequest createRequest = CategoryRequest.builder()
                .name(name)
                .transactionType(TransactionType.EXPENSE)
                .build();

        categoryService.addCategory(createRequest);
        Category category = categoryService.getCategoryByName(name);

        Assertions.assertNotNull(category);
        Assertions.assertEquals(name, category.getName());

    }

    @Test
    public void getAmountSumByCategory() {

        AccountTypeRequest accountTypeRequest = AccountTypeRequest.builder()
                .name("Cash")
                .build();

        CreateAccountRequest accountRequest = CreateAccountRequest.builder()
                .name("Cash account")
                .accountTypeName("Cash")
                .initialBalance(BigDecimal.valueOf(9.99))
                .build();


        accountTypeService.addAccountType(accountTypeRequest);
        accountService.addAccount(accountRequest);


        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("Utilities")
                .transactionType(TransactionType.EXPENSE)
                .build();


        CategoryRequest categoryRequest2 = CategoryRequest.builder()
                .name("Salary")
                .transactionType(TransactionType.INCOME)
                .build();

        CategoryRequest categoryRequest3 = CategoryRequest.builder()
                .name("Healthcare")
                .transactionType(TransactionType.EXPENSE)
                .build();


        CategoryRequest categoryRequest4 = CategoryRequest.builder()
                .name("Investing")
                .transactionType(TransactionType.INCOME)
                .build();

        categoryService.addCategory(categoryRequest);
        categoryService.addCategory(categoryRequest2);
        categoryService.addCategory(categoryRequest3);
        categoryService.addCategory(categoryRequest4);


        TransactionRequest request1 = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(5.00))
                .description("Bill")
                .categoryName("Utilities")
                .accountName("Cash account")
                .type(TransactionType.EXPENSE)
                .build();

        TransactionRequest request2 = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(100.00))
                .description("Salary bonus 1")
                .categoryName("Salary")
                .accountName("Cash account")
                .type(TransactionType.INCOME)
                .build();

        TransactionRequest request3 = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(49.98))
                .description("Bill 2")
                .categoryName("Utilities")
                .accountName("Cash account")
                .type(TransactionType.EXPENSE)
                .build();

        TransactionRequest request4 = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(0.50))
                .description("Interest")
                .categoryName("Investing")
                .accountName("Cash account")
                .type(TransactionType.INCOME)
                .build();

        TransactionRequest request5 = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(0.50))
                .description("Interest")
                .categoryName("Utilities")
                .accountName("Cash account")
                .type(TransactionType.EXPENSE)
                .build();

        transactionService.addTransaction(request1);
        transactionService.addTransaction(request2);
        transactionService.addTransaction(request3);
        transactionService.addTransaction(request4);
        transactionService.addTransaction(request5);

        List<AmountSumByCategoryResponse> responses = categoryService.getAmountSumByCategory();
        Assertions.assertEquals(4, responses.size());

        AmountSumByCategoryResponse response1 = categoryService.getAmountSumByCategoryByCategoryName("Utilities");
        Assertions.assertEquals("Utilities", response1.getCategory());
        Assertions.assertEquals(BigDecimal.valueOf(0.50 + 49.98 + 5.00), response1.getSum());

        AmountSumByCategoryResponse response2 = categoryService.getAmountSumByCategoryByCategoryName("Investing");
        Assertions.assertEquals("Investing", response2.getCategory());
        Assertions.assertEquals(BigDecimal.valueOf(0.50).stripTrailingZeros(), response2.getSum().stripTrailingZeros());

        AmountSumByCategoryResponse response3 = categoryService.getAmountSumByCategoryByCategoryName("Salary");
        Assertions.assertEquals("Salary", response3.getCategory());
        Assertions.assertEquals(BigDecimal.valueOf(100).stripTrailingZeros(), response3.getSum().stripTrailingZeros());

        AmountSumByCategoryResponse response4 = categoryService.getAmountSumByCategoryByCategoryName("Healthcare");
        Assertions.assertEquals("Healthcare", response4.getCategory());
        Assertions.assertEquals(BigDecimal.ZERO.stripTrailingZeros(), response4.getSum().stripTrailingZeros());

        Assertions.assertEquals(new HashSet<>(responses), new HashSet<>(List.of(response1, response2, response3, response4)));


    }
}

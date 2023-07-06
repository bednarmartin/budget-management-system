package com.bednarmartin.budgetmanagementsystem.service;

import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import com.bednarmartin.budgetmanagementsystem.exception.TransactionTypeMismatchException;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.TransactionService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.TransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
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
public class TransactionServiceTests {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AccountTypeService accountTypeService;

    @Autowired
    AccountService accountService;

    @BeforeEach
    public void init() {
        AccountTypeRequest accountTypeRequest = AccountTypeRequest.builder()
                .name("Cash")
                .build();

        CreateAccountRequest accountRequest = CreateAccountRequest.builder()
                .name("Cash account")
                .accountTypeName("Cash")
                .initialBalance(BigDecimal.valueOf(9.99))
                .build();

        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("Utilities")
                .transactionType(TransactionType.EXPENSE)
                .build();


        CategoryRequest categoryRequest2 = CategoryRequest.builder()
                .name("Salary")
                .transactionType(TransactionType.INCOME)
                .build();

        accountTypeService.addAccountType(accountTypeRequest);
        accountService.addAccount(accountRequest);
        categoryService.addCategory(categoryRequest);
        categoryService.addCategory(categoryRequest2);

    }

    @Test
    public void testAddOneTransaction() throws InterruptedException {
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(51.99))
                .description("Electricity bill")
                .categoryName("Utilities")
                .type(TransactionType.EXPENSE)
                .accountName("Cash account")
                .build();

        LocalDateTime start = LocalDateTime.now();
        Thread.sleep(100);
        transactionService.addTransaction(request);
        Thread.sleep(100);
        LocalDateTime end = LocalDateTime.now();

        List<TransactionResponse> responses = transactionService.getAllTransactions();
        LocalDateTime dateCreated = responses.get(0).getDateCreated();
        boolean dateCreatedIsGood = dateCreated.isAfter(start) && dateCreated.isBefore(end);

        Assertions.assertEquals(1, responses.size());
        Assertions.assertEquals(1L, responses.get(0).getId());
        Assertions.assertEquals(responses.get(0).getAmount(), BigDecimal.valueOf(51.99));
        Assertions.assertEquals(responses.get(0).getDescription(), "Electricity bill");
        Assertions.assertEquals(responses.get(0).getCategory().getName(), "Utilities");
        Assertions.assertEquals(TransactionType.EXPENSE, responses.get(0).getType());
        Assertions.assertEquals("Cash account", responses.get(0).getAccount().getName());
        Assertions.assertTrue(dateCreatedIsGood);

    }

    @Test
    public void testAddMoreTransactions() throws InterruptedException {
        String[] descriptions = {"Electricity bill", "Gas bill", "TV bill"};
        BigDecimal[] amounts = {BigDecimal.valueOf(51.99), BigDecimal.valueOf(23.59), BigDecimal.valueOf(12.53)};
        String accountName = "Cash account";

        LocalDateTime start = LocalDateTime.now();
        Thread.sleep(100);
        for (int i = 0; i < descriptions.length; i++) {
            TransactionRequest request = TransactionRequest.builder()
                    .amount(amounts[i])
                    .description(descriptions[i])
                    .categoryName("Utilities")
                    .type(TransactionType.EXPENSE)
                    .accountName(accountName)
                    .build();
            transactionService.addTransaction(request);
        }
        Thread.sleep(100);
        LocalDateTime end = LocalDateTime.now();

        List<TransactionResponse> responses = transactionService.getAllTransactions();

        for (TransactionResponse response : responses) {
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
            Assertions.assertEquals(TransactionType.EXPENSE, responses.get(i).getType());
            Assertions.assertEquals(accountName, responses.get(i).getAccount().getName());
        }
    }

    @Test
    public void testUpdateExpenseCategory() {
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(51.99))
                .description("Electricity bill")
                .categoryName("Utilities")
                .type(TransactionType.EXPENSE)
                .accountName("Cash account")
                .build();

        transactionService.addTransaction(request);
        TransactionResponse response = transactionService.getAllTransactions().get(0);

        TransactionRequest updateRequest = TransactionRequest.builder()
                .categoryName("Utilities")
                .description("Candy")
                .amount(BigDecimal.valueOf(23.19))
                .type(TransactionType.EXPENSE)
                .accountName("Cash account")
                .build();

        transactionService.updateTransaction(response.getId(), updateRequest);

        TransactionResponse updatedResponse = transactionService.getTransactionById(response.getId());
        Assertions.assertEquals("Utilities", updatedResponse.getCategory().getName());
        Assertions.assertEquals(response.getDateCreated(), updatedResponse.getDateCreated());
        Assertions.assertEquals("Candy", updatedResponse.getDescription());
        Assertions.assertEquals(BigDecimal.valueOf(23.19), updatedResponse.getAmount());
        Assertions.assertEquals(TransactionType.EXPENSE, updatedResponse.getType());
        Assertions.assertEquals("Cash account", updatedResponse.getAccount().getName());

    }

    @Test
    public void testDeleteExpenseCategoryById() {
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(51.99))
                .description("Electricity bill")
                .categoryName("Utilities")
                .accountName("Cash account")
                .type(TransactionType.EXPENSE)
                .build();

        transactionService.addTransaction(request);
        TransactionResponse response = transactionService.getAllTransactions().get(0);

        transactionService.deleteTransactionById(response.getId());
        List<TransactionResponse> responses = transactionService.getAllTransactions();

        Assertions.assertTrue(responses.isEmpty());

    }

    @Test
    public void testAddingTransactionWithTypeMismatch() {
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(51.99))
                .description("Electricity bill")
                .categoryName("Utilities")
                .accountName("Cash account")
                .type(TransactionType.INCOME)
                .build();

        Assertions.assertThrows(TransactionTypeMismatchException.class, () -> transactionService.addTransaction(request));
    }

    @Test
    public void testUpdatingTransactionWithTypeMismatch() {
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(51.99))
                .description("Electricity bill")
                .categoryName("Utilities")
                .accountName("Cash account")
                .type(TransactionType.EXPENSE)
                .build();
        transactionService.addTransaction(request);
        TransactionResponse response = transactionService.getAllTransactions().get(0);

        TransactionRequest updateRequest = TransactionRequest.builder()
                .categoryName("Utilities")
                .description("Candy")
                .accountName("Cash account")
                .amount(BigDecimal.valueOf(23.19))
                .type(TransactionType.INCOME)
                .build();
        Assertions.assertThrows(TransactionTypeMismatchException.class,
                () -> transactionService.updateTransaction(response.getId(), updateRequest));
    }

    @Test
    public void testUpdatingBalanceWithIncomeTransaction() {
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(20.00))
                .description("Bonus")
                .categoryName("Salary")
                .accountName("Cash account")
                .type(TransactionType.INCOME)
                .build();
        transactionService.addTransaction(request);
        AccountResponse accountResponse = accountService.getAccountById(1L);
        Assertions.assertEquals(BigDecimal.valueOf(29.99), accountResponse.getBalance());
    }

    @Test
    public void testUpdatingBalanceWithExpenseTransaction() {
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(5.00))
                .description("Bill")
                .categoryName("Utilities")
                .accountName("Cash account")
                .type(TransactionType.EXPENSE)
                .build();
        transactionService.addTransaction(request);
        AccountResponse accountResponse = accountService.getAccountById(1L);
        Assertions.assertEquals(BigDecimal.valueOf(4.99), accountResponse.getBalance());
    }

    @Test
    public void testUpdatingBalanceWithMoreTransactions() {
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
                .categoryName("Salary")
                .accountName("Cash account")
                .type(TransactionType.INCOME)
                .build();

        transactionService.addTransaction(request1);
        AccountResponse accountResponse = accountService.getAccountById(1L);
        Assertions.assertEquals(BigDecimal.valueOf(4.99), accountResponse.getBalance());

        transactionService.addTransaction(request2);
        accountResponse = accountService.getAccountById(1L);
        Assertions.assertEquals(BigDecimal.valueOf(104.99), accountResponse.getBalance());

        transactionService.addTransaction(request3);
        accountResponse = accountService.getAccountById(1L);
        Assertions.assertEquals(BigDecimal.valueOf(55.01), accountResponse.getBalance());

        transactionService.addTransaction(request4);
        accountResponse = accountService.getAccountById(1L);
        Assertions.assertEquals(BigDecimal.valueOf(55.51), accountResponse.getBalance());

        Assertions.assertEquals(4, transactionService.getAllTransactions().size());
    }
}

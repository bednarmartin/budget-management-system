package com.bednarmartin.budgetmanagementsystem.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.model.Transaction;
import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import com.bednarmartin.budgetmanagementsystem.db.repository.AccountRepository;
import com.bednarmartin.budgetmanagementsystem.db.repository.AccountTypeRepository;
import com.bednarmartin.budgetmanagementsystem.db.repository.CategoryRepository;
import com.bednarmartin.budgetmanagementsystem.db.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransactionRepositoryTests {

    @Autowired
    private TransactionRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Test
    @Transactional
    public void testUpdateTransactionById() throws InterruptedException {
        LocalDateTime dateTime = LocalDateTime.now();

        AccountType accountType = AccountType.builder()
                .name("Cash")
                .build();

        Account account = Account.builder()
                .accountType(accountType)
                .balance(BigDecimal.valueOf(9.99))
                .name("Cash Account")
                .build();

        Category category = Category.builder()
                .name("Health")
                .transactionType(TransactionType.EXPENSE)
                .dateUpdated(dateTime)
                .dateCreated(dateTime)
                .build();

        Transaction transaction = Transaction.builder()
                .description("Toothpaste")
                .amount(BigDecimal.valueOf(5.60))
                .category(category)
                .type(TransactionType.EXPENSE)
                .dateUpdated(dateTime)
                .dateCreated(dateTime)
                .account(account)
                .build();

        accountTypeRepository.save(accountType);
        accountRepository.save(account);
        categoryRepository.save(category);
        expenseRepository.save(transaction);
        Thread.sleep(1000);

        LocalDateTime newDateTime = LocalDateTime.now();
        BigDecimal newPrice = BigDecimal.valueOf(5.59);
        expenseRepository.updateTransactionById(
                transaction.getId(),
                newPrice,
                transaction.getDescription(),
                category,
                transaction.getType(),
                account,
                newDateTime
        );

        Optional<Transaction> updatedExpenseOptional = expenseRepository.findById(transaction.getId());
        Assertions.assertTrue(updatedExpenseOptional.isPresent());
        Transaction updatedTransaction = updatedExpenseOptional.get();
        LocalDateTime dateCreated = updatedTransaction.getDateCreated();
        LocalDateTime dateUpdated = updatedTransaction.getDateUpdated();

        Assertions.assertEquals(category.getId(), updatedTransaction.getId());
        Assertions.assertEquals(dateTime.minusNanos(dateTime.getNano()), dateCreated.minusNanos(dateCreated.getNano()));
        Assertions.assertEquals(
                newDateTime.minusNanos(newDateTime.getNano()),
                dateUpdated.minusNanos(dateUpdated.getNano()));
        Assertions.assertEquals(newPrice, updatedTransaction.getAmount());
        Assertions.assertEquals(transaction.getType(), updatedTransaction.getType());
        Assertions.assertEquals(category.getName(), updatedTransaction.getCategory().getName());
        Assertions.assertEquals(transaction.getDescription(), updatedTransaction.getDescription());
        Assertions.assertEquals(account.getName(), updatedTransaction.getAccount().getName());
        Assertions.assertEquals(account.getBalance(), updatedTransaction.getAccount().getBalance());
    }

}

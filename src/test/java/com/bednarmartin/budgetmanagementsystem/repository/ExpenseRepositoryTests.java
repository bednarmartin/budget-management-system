package com.bednarmartin.budgetmanagementsystem.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.Expense;
import com.bednarmartin.budgetmanagementsystem.db.model.ExpenseCategory;
import com.bednarmartin.budgetmanagementsystem.db.repository.ExpenseCategoryRepository;
import com.bednarmartin.budgetmanagementsystem.db.repository.ExpenseRepository;
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
public class ExpenseRepositoryTests {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Test
    @Transactional
    public void testUpdateExpenseById() throws InterruptedException {
        LocalDateTime dateTime = LocalDateTime.now();

        ExpenseCategory expenseCategory = ExpenseCategory.builder()
                .name("Health")
                .dateUpdated(dateTime)
                .dateCreated(dateTime)
                .build();

        Expense expense = Expense.builder()
                .description("Toothpaste")
                .amount(BigDecimal.valueOf(5.60))
                .category(expenseCategory)
                .dateUpdated(dateTime)
                .dateCreated(dateTime)
                .build();

        expenseCategoryRepository.save(expenseCategory);
        expenseRepository.save(expense);
        Thread.sleep(1000);

        LocalDateTime newDateTime = LocalDateTime.now();
        BigDecimal newPrice = BigDecimal.valueOf(5.59);
        expenseRepository.updateExpenseById(
                expense.getId(),
                newPrice,
                expense.getDescription(),
                expenseCategory,
                newDateTime
        );

        Optional<Expense> updatedExpenseOptional = expenseRepository.findById(expense.getId());
        Assertions.assertTrue(updatedExpenseOptional.isPresent());
        Expense updatedExpense = updatedExpenseOptional.get();
        LocalDateTime dateCreated = updatedExpense.getDateCreated();
        LocalDateTime dateUpdated = updatedExpense.getDateUpdated();

        Assertions.assertEquals(expenseCategory.getId(), updatedExpense.getId());
        Assertions.assertEquals(dateTime.minusNanos(dateTime.getNano()), dateCreated.minusNanos(dateCreated.getNano()));
        Assertions.assertEquals(
                newDateTime.minusNanos(newDateTime.getNano()),
                dateUpdated.minusNanos(dateUpdated.getNano()));
        Assertions.assertEquals(newPrice, updatedExpense.getAmount());
        Assertions.assertEquals(expenseCategory.getName(), updatedExpense.getCategory().getName());
        Assertions.assertEquals(expense.getDescription(), updatedExpense.getDescription());
    }

}

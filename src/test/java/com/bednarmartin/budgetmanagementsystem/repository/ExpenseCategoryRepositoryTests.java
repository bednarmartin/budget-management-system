package com.bednarmartin.budgetmanagementsystem.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.ExpenseCategory;
import com.bednarmartin.budgetmanagementsystem.db.repository.ExpenseCategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ExpenseCategoryRepositoryTests {

    @Autowired
    private ExpenseCategoryRepository repository;

    @Test
    @Transactional
    public void testUpdateExpenseCategoryById() throws InterruptedException {
        LocalDateTime dateTime = LocalDateTime.now();
        ExpenseCategory expenseCategory = ExpenseCategory.builder()
                .name("Utilities")
                .dateUpdated(dateTime)
                .dateCreated(dateTime)
                .build();
        repository.save(expenseCategory);

        Thread.sleep(1000);
        LocalDateTime newDateTime = LocalDateTime.now();
        String newName = "Groceries";
        repository.updateExpenseCategoryById(
                expenseCategory.getId(),
                newName,
                newDateTime
        );

        Optional<ExpenseCategory> updatedExpenseCategoryOptional = repository.findById(expenseCategory.getId());
        Assertions.assertTrue(updatedExpenseCategoryOptional.isPresent());
        ExpenseCategory updatedExpenseCategory = updatedExpenseCategoryOptional.get();
        LocalDateTime dateCreated = updatedExpenseCategory.getDateCreated();
        LocalDateTime dateUpdated = updatedExpenseCategory.getDateUpdated();

        Assertions.assertEquals(expenseCategory.getId(), updatedExpenseCategory.getId());
        Assertions.assertEquals(dateTime.minusNanos(dateTime.getNano()), dateCreated.minusNanos(dateCreated.getNano()));
        Assertions.assertEquals(newName, updatedExpenseCategory.getName());
        Assertions.assertEquals(
                newDateTime.minusNanos(newDateTime.getNano()),
                dateUpdated.minusNanos(dateUpdated.getNano()));
    }
}

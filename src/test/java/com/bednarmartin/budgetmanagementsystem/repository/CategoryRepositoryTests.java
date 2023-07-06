package com.bednarmartin.budgetmanagementsystem.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import com.bednarmartin.budgetmanagementsystem.db.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository repository;

    @Test
    @Transactional
    public void testUpdateCategoryById() throws InterruptedException {
        LocalDateTime dateTime = LocalDateTime.now();
        Category category = Category.builder()
                .name("Utilities")
                .transactionType(TransactionType.EXPENSE)
                .dateUpdated(dateTime)
                .dateCreated(dateTime)
                .build();
        repository.save(category);

        Thread.sleep(1000);
        LocalDateTime newDateTime = LocalDateTime.now();
        String newName = "Groceries";
        repository.updateCategoryById(
                category.getId(),
                newName,
                TransactionType.EXPENSE,
                newDateTime
        );

        Optional<Category> updatedExpenseCategoryOptional = repository.findById(category.getId());
        Assertions.assertTrue(updatedExpenseCategoryOptional.isPresent());
        Category updatedCategory = updatedExpenseCategoryOptional.get();
        LocalDateTime dateCreated = updatedCategory.getDateCreated();
        LocalDateTime dateUpdated = updatedCategory.getDateUpdated();

        Assertions.assertEquals(category.getId(), updatedCategory.getId());
        Assertions.assertEquals(dateTime.minusNanos(dateTime.getNano()), dateCreated.minusNanos(dateCreated.getNano()));
        Assertions.assertEquals(newName, updatedCategory.getName());
        Assertions.assertEquals(TransactionType.EXPENSE, updatedCategory.getTransactionType());
        Assertions.assertEquals(
                newDateTime.minusNanos(newDateTime.getNano()),
                dateUpdated.minusNanos(dateUpdated.getNano()));
    }
}

package com.bednarmartin.budgetmanagementsystem.service;

import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
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
public class CategoryServiceTests {

    @Autowired
    private CategoryService categoryService;

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
}

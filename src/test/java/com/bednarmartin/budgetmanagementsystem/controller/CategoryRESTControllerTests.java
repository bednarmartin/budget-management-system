package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.TransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class CategoryRESTControllerTests {

    private static ObjectMapper objectMapper;
    private final String URL = "/api/category";
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

    }

    @Test
    void testAddCategory() throws Exception {
        String name = "Groceries";

        CategoryRequest request = CategoryRequest.builder()
                .name(name)
                .transactionType(TransactionType.EXPENSE)
                .build();

        // Create a new Category
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Get Category
        String responseJson = mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CategoryResponse response = objectMapper.readValue(responseJson, CategoryResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(name, response.getName());
        Assertions.assertEquals(TransactionType.EXPENSE, response.getTransactionType());

    }

    @Test
    void testAddMoreCategories() throws Exception {
        String[] names = {"Groceries", "Utilities", "Health"};

        for (String name : names) {
            CategoryRequest request = CategoryRequest.builder()
                    .name(name)
                    .transactionType(TransactionType.EXPENSE)
                    .build();

            // Create a new Category
            mockMvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // Get all Categories

        String categoriesJson = mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<CategoryResponse> responseList = objectMapper.readValue(
                categoriesJson, new TypeReference<>() {
                });


        Assertions.assertEquals(3, responseList.size());
        for (int i = 0; i < responseList.size(); i++) {
            Assertions.assertEquals(names[i], responseList.get(i).getName());
            Assertions.assertEquals(TransactionType.EXPENSE, responseList.get(i).getTransactionType());
        }

    }

    @Test
    void testUpdateCategory() throws Exception {
        String name = "Groceries";

        CategoryRequest request = CategoryRequest.builder()
                .name(name)
                .transactionType(TransactionType.EXPENSE)
                .build();

        // Create a new Category
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        String newName = "Utilities";
        CategoryRequest updateRequest = CategoryRequest.builder()
                .name(newName)
                .transactionType(TransactionType.EXPENSE)
                .build();

        // Update the Category
        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Get updated Category
        String responseJson = mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CategoryResponse response = objectMapper.readValue(responseJson, CategoryResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(newName, response.getName());
        Assertions.assertEquals(TransactionType.EXPENSE, response.getTransactionType());
    }

    @Test
    void testDeleteCategory() throws Exception {
        String name = "Groceries";

        CategoryRequest request = CategoryRequest.builder()
                .name(name)
                .transactionType(TransactionType.EXPENSE)
                .build();

        // Create a new Category
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        // Delete the Category
        mockMvc.perform(delete(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Get all  Categories
        String categoriesJson = mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<CategoryResponse> responseList = objectMapper.readValue(
                categoriesJson, new TypeReference<>() {
                });


        Assertions.assertEquals(0, responseList.size());
    }

    @Test
    public void testGetBalances() throws Exception {
        String accountName = "Cash";
        String accountTypeName = "Cash";
        String accountTypeURL = "/api/account/type";
        String accountURL = "/api/account";
        String categoryURL = "/api/category";
        String transactionURL = "/api/transaction";
        String balancesURL = "/api/category/balances";

        AccountTypeRequest createAccountTypeRequest = AccountTypeRequest.builder()
                .name(accountTypeName)
                .build();

        // Create a new Account Type
        mockMvc.perform(post(accountTypeURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountTypeRequest)))
                .andExpect(status().isCreated());


        BigDecimal balance = BigDecimal.valueOf(10.59);

        CreateAccountRequest createAccountRequest = CreateAccountRequest.builder()
                .initialBalance(balance)
                .accountTypeName(accountTypeName)
                .name(accountName)
                .build();

        // Create a new Account
        mockMvc.perform(post(accountURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountRequest)))
                .andExpect(status().isCreated());

        String[] categoryNames = {"Groceries", "Utilities", "Health"};

        for (String name : categoryNames) {
            CategoryRequest request = CategoryRequest.builder()
                    .name(name)
                    .transactionType(TransactionType.EXPENSE)
                    .build();

            // Create a new Category
            mockMvc.perform(post(categoryURL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        String[] descriptions = {"Food", "Soda", "Candy"};
        BigDecimal[] prices = {BigDecimal.valueOf(53.69), BigDecimal.valueOf(68.96), BigDecimal.valueOf(12.12)};
        String categoryName = "Groceries";

        for (int i = 0; i < descriptions.length; i++) {
            TransactionRequest request = TransactionRequest.builder()
                    .categoryName(categoryName)
                    .description(descriptions[i])
                    .amount(prices[i])
                    .accountName("Cash")
                    .type(TransactionType.EXPENSE)
                    .build();

            // Create a new Transaction
            mockMvc.perform(post(transactionURL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        String balancesJson = mockMvc.perform(get(balancesURL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<AmountSumByCategoryResponse> responseList = objectMapper.readValue(balancesJson, new TypeReference<>() {
        });

        Assertions.assertEquals(3, responseList.size());

        String groceriesBalanceJson = mockMvc.perform(get(balancesURL + "/Groceries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AmountSumByCategoryResponse groceriesBalanceResponse = objectMapper.readValue(groceriesBalanceJson,
                AmountSumByCategoryResponse.class);

        Assertions.assertEquals(prices[0].add(prices[1]).add(prices[2]).stripTrailingZeros(),
                groceriesBalanceResponse.getSum().stripTrailingZeros());
        Assertions.assertEquals("Groceries", groceriesBalanceResponse.getCategory().getName());

        String utilitiesBalanceJson = mockMvc.perform(get(balancesURL + "/Utilities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AmountSumByCategoryResponse utilitiesBalanceResponse = objectMapper.readValue(utilitiesBalanceJson,
                AmountSumByCategoryResponse.class);

        Assertions.assertEquals(BigDecimal.ZERO.stripTrailingZeros(),
                utilitiesBalanceResponse.getSum().stripTrailingZeros());
        Assertions.assertEquals("Utilities", utilitiesBalanceResponse.getCategory().getName());

        String healthBalanceJson = mockMvc.perform(get(balancesURL + "/Health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AmountSumByCategoryResponse healthBalanceResponse = objectMapper.readValue(healthBalanceJson,
                AmountSumByCategoryResponse.class);

        Assertions.assertEquals(BigDecimal.ZERO.stripTrailingZeros(),
                healthBalanceResponse.getSum().stripTrailingZeros());
        Assertions.assertEquals("Health", healthBalanceResponse.getCategory().getName());

    }

    @Test
    void testValidation() throws Exception {
        String badName1 = "";
        String badName2 = "OK";

        // Empty request
        CategoryRequest request = CategoryRequest.builder().build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Request without name
        request = CategoryRequest.builder()
                .transactionType(TransactionType.EXPENSE)
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Request without type
        request = CategoryRequest.builder()
                .name("Groceries")
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());


        //Request with bad name
        request = CategoryRequest.builder()
                .name(badName1)
                .transactionType(TransactionType.EXPENSE)
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request = CategoryRequest.builder()
                .name(badName2)
                .transactionType(TransactionType.EXPENSE)
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }


}

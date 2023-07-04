package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.TransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TransactionRESTControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String URL = "/api/transaction";

    @BeforeEach
    public void init() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String categoryName = "Groceries";

        CategoryRequest request = CategoryRequest.builder()
                .name(categoryName)
                .transactionType(TransactionType.EXPENSE)
                .build();

        // Create a new Expense Category
        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

    }

    @Test
    void testAddTransaction() throws Exception {
        String categoryName = "Groceries";
        String description = "Food";
        BigDecimal price = BigDecimal.valueOf(10.59);

        TransactionRequest request = TransactionRequest.builder()
                .amount(price)
                .description(description)
                .categoryName(categoryName)
                .type(TransactionType.EXPENSE)
                .build();

        // Create a new Expense
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Get Expense
        String responseJson = mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TransactionResponse response = objectMapper.readValue(responseJson, TransactionResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(categoryName, response.getCategory().getName());
        Assertions.assertEquals(description, response.getDescription());
        Assertions.assertEquals(TransactionType.EXPENSE, response.getType());
        Assertions.assertEquals(price, response.getAmount());

    }

    @Test
    void testAddMoreTransactions() throws Exception {
        String[] descriptions = {"Food", "Soda", "Candy"};
        BigDecimal[] prices = {BigDecimal.valueOf(53.69), BigDecimal.valueOf(68.96), BigDecimal.valueOf(12.12)};
        String categoryName = "Groceries";

        for (int i = 0; i < descriptions.length; i++) {
            TransactionRequest request = TransactionRequest.builder()
                    .categoryName(categoryName)
                    .description(descriptions[i])
                    .amount(prices[i])
                    .type(TransactionType.EXPENSE)
                    .build();

            // Create a new Expense Category
            mockMvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // Get all Expense Categories

        String expenseJson = mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<TransactionResponse> responseList = objectMapper.readValue(expenseJson, new TypeReference<>() {
        });


        Assertions.assertEquals(3, responseList.size());
        for (int i = 0; i < responseList.size(); i++) {
            Assertions.assertEquals(descriptions[i], responseList.get(i).getDescription());
            Assertions.assertEquals(prices[i], responseList.get(i).getAmount());
            Assertions.assertEquals(categoryName, responseList.get(i).getCategory().getName());
            Assertions.assertEquals(TransactionType.EXPENSE, responseList.get(i).getType());
        }

    }

    @Test
    void testUpdateTransaction() throws Exception {
        String categoryName = "Groceries";
        String description = "Food";
        BigDecimal price = BigDecimal.valueOf(10.59);

        TransactionRequest request = TransactionRequest.builder()
                .amount(price)
                .description(description)
                .categoryName(categoryName)
                .type(TransactionType.EXPENSE)
                .build();

        // Create a new Expense
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        BigDecimal newPrice = BigDecimal.valueOf(12.59);
        TransactionRequest updateRequest = TransactionRequest.builder()
                .amount(newPrice)
                .description(description)
                .categoryName(categoryName)
                .type(TransactionType.EXPENSE)
                .build();

        // Update the Expense
        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Get updated Expense
        String responseJson = mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TransactionResponse response = objectMapper.readValue(responseJson, TransactionResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(categoryName, response.getCategory().getName());
        Assertions.assertEquals(newPrice, response.getAmount());
        Assertions.assertEquals(TransactionType.EXPENSE, response.getType());
        Assertions.assertEquals(description, response.getDescription());
    }

    @Test
    void testDeleteTransaction() throws Exception {
        String categoryName = "Groceries";
        String description = "Food";
        BigDecimal price = BigDecimal.valueOf(10.59);

        TransactionRequest request = TransactionRequest.builder()
                .amount(price)
                .description(description)
                .categoryName(categoryName)
                .type(TransactionType.EXPENSE)
                .build();

        // Create a new Expense
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        // Delete the Expense
        mockMvc.perform(delete(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Get all Expenses
        String expenseJson = mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<TransactionResponse> responseList = objectMapper.readValue(expenseJson, new TypeReference<>() {
        });

        Assertions.assertEquals(0, responseList.size());
    }

}

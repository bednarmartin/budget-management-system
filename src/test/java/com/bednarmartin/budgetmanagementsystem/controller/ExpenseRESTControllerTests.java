package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseCategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseResponse;
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
public class ExpenseRESTControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String URL = "/api/expense";

    @BeforeEach
    public void init() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String categoryName = "Groceries";

        ExpenseCategoryRequest request = ExpenseCategoryRequest.builder()
                .name(categoryName)
                .build();

        // Create a new Expense Category
        mockMvc.perform(post(URL + "/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

    }

    @Test
    void testAddExpense() throws Exception {
        String categoryName = "Groceries";
        String description = "Food";
        BigDecimal price = BigDecimal.valueOf(10.59);

        ExpenseRequest request = ExpenseRequest.builder()
                .amount(price)
                .description(description)
                .categoryName(categoryName)
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

        ExpenseResponse response = objectMapper.readValue(responseJson, ExpenseResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(categoryName, response.getCategory().getName());
        Assertions.assertEquals(description, response.getDescription());
        Assertions.assertEquals(price, response.getAmount());

    }

    @Test
    void testAddMoreExpenses() throws Exception {
        String[] descriptions = {"Food", "Soda", "Candy"};
        BigDecimal[] prices = {BigDecimal.valueOf(53.69), BigDecimal.valueOf(68.96), BigDecimal.valueOf(12.12)};
        String categoryName = "Groceries";

        for (int i = 0; i < descriptions.length; i++) {
            ExpenseRequest request = ExpenseRequest.builder()
                    .categoryName(categoryName)
                    .description(descriptions[i])
                    .amount(prices[i])
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

        List<ExpenseResponse> responseList = objectMapper.readValue(expenseJson, new TypeReference<>() {
        });


        Assertions.assertEquals(3, responseList.size());
        for (int i = 0; i < responseList.size(); i++) {
            Assertions.assertEquals(descriptions[i], responseList.get(i).getDescription());
            Assertions.assertEquals(prices[i], responseList.get(i).getAmount());
            Assertions.assertEquals(categoryName, responseList.get(i).getCategory().getName());
        }

    }

    @Test
    void testUpdateExpense() throws Exception {
        String categoryName = "Groceries";
        String description = "Food";
        BigDecimal price = BigDecimal.valueOf(10.59);

        ExpenseRequest request = ExpenseRequest.builder()
                .amount(price)
                .description(description)
                .categoryName(categoryName)
                .build();

        // Create a new Expense
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        BigDecimal newPrice = BigDecimal.valueOf(12.59);
        ExpenseRequest updateRequest = ExpenseRequest.builder()
                .amount(newPrice)
                .description(description)
                .categoryName(categoryName)
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

        ExpenseResponse response = objectMapper.readValue(responseJson, ExpenseResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(categoryName, response.getCategory().getName());
        Assertions.assertEquals(newPrice, response.getAmount());
        Assertions.assertEquals(description, response.getDescription());
    }

    @Test
    void testDeleteExpense() throws Exception {
        String categoryName = "Groceries";
        String description = "Food";
        BigDecimal price = BigDecimal.valueOf(10.59);

        ExpenseRequest request = ExpenseRequest.builder()
                .amount(price)
                .description(description)
                .categoryName(categoryName)
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

        List<ExpenseResponse> responseList = objectMapper.readValue(expenseJson, new TypeReference<>() {
        });

        Assertions.assertEquals(0, responseList.size());
    }

}

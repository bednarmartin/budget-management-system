package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.service.api.request.ExpenseCategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.ExpenseCategoryResponse;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class ExpenseCategoryRESTControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    private final String url = "/api/expense/category";

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testAddExpenseCategory() throws Exception {
        String name = "Groceries";

        ExpenseCategoryRequest request = ExpenseCategoryRequest.builder()
                .name(name)
                .build();

        // Create a new Expense Category
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Get Expense Category
        String responseJson = mockMvc.perform(get(url + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ExpenseCategoryResponse response = objectMapper.readValue(responseJson, ExpenseCategoryResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(name, response.getName());

    }

    @Test
    void testAddMoreExpenseCategories() throws Exception {
        String[] names = {"Groceries", "Utilities", "Health"};

        for (int i = 1; i <= names.length; i++) {
            ExpenseCategoryRequest request = ExpenseCategoryRequest.builder()
                    .name(names[i - 1])
                    .build();

            // Create a new Expense Category
            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // Get all Expense Categories

        String expenseCategoriesJson = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ExpenseCategoryResponse> responseList = objectMapper.readValue(
                expenseCategoriesJson, new TypeReference<>() {
                });


        Assertions.assertEquals(3, responseList.size());
        for (int i = 0; i < responseList.size(); i++) {
            Assertions.assertEquals(names[i], responseList.get(i).getName());
        }

    }

    @Test
    void testUpdateExpenseCategory() throws Exception {
        String name = "Groceries";

        ExpenseCategoryRequest request = ExpenseCategoryRequest.builder()
                .name(name)
                .build();

        // Create a new Expense Category
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        String newName = "Utilities";
        ExpenseCategoryRequest updateRequest = ExpenseCategoryRequest.builder()
                .name(newName)
                .build();

        // Update the Expense Category
        mockMvc.perform(put(url + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Get updated Expense Category
        String responseJson = mockMvc.perform(get(url + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ExpenseCategoryResponse response = objectMapper.readValue(responseJson, ExpenseCategoryResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(newName, response.getName());
    }

    @Test
    void testDeleteExpenseCategory() throws Exception {
        String name = "Groceries";

        ExpenseCategoryRequest request = ExpenseCategoryRequest.builder()
                .name(name)
                .build();

        // Create a new Expense Category
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());



        // Delete the Expense Category
        mockMvc.perform(delete(url + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Get all Expense Categories
        String expenseCategoriesJson = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ExpenseCategoryResponse> responseList = objectMapper.readValue(
                expenseCategoriesJson, new TypeReference<>() {
                });


        Assertions.assertEquals(0, responseList.size());
    }

}

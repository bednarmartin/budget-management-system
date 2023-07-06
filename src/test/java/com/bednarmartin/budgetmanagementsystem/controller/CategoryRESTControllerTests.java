package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class CategoryRESTControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    private final String URL = "/api/category";


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

}

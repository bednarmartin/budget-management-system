package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AccountTypeRESTControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    private final String URL = "/api/account/type";

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

    }

    @Test
    void testAddAccountType() throws Exception {
        String name = "Cash";

        AccountTypeRequest request = AccountTypeRequest.builder()
                .name(name)
                .build();

        // Create a new Account Type
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Get Account Type
        String responseJson = mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountTypeResponse response = objectMapper.readValue(responseJson, AccountTypeResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(name, response.getName());
    }

    @Test
    void testAddMoreCategories() throws Exception {
        String[] names = {"Cash", "Bank Account", "Investments"};

        for (String name : names) {
            AccountTypeRequest request = AccountTypeRequest.builder()
                    .name(name)
                    .build();

            // Create a new Account Type
            mockMvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // Get all Account Types
        String accountTypesJson = mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<AccountTypeResponse> responseList = objectMapper.readValue(accountTypesJson, new TypeReference<>() {
        });


        Assertions.assertEquals(3, responseList.size());
        for (int i = 0; i < responseList.size(); i++) {
            Assertions.assertEquals(names[i], responseList.get(i).getName());
        }

    }

    @Test
    void testUpdateAccountType() throws Exception {
        String name = "Cash";

        AccountTypeRequest request = AccountTypeRequest.builder()
                .name(name)
                .build();

        // Create a new Account Type
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        String newName = "Bank Account";
        AccountTypeRequest updateRequest = AccountTypeRequest.builder()
                .name(newName)
                .build();

        // Update the Account Type
        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Get updated Account Type
        String responseJson = mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountTypeResponse response = objectMapper.readValue(responseJson, AccountTypeResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(newName, response.getName());
    }

    @Test
    void testDeleteAccountType() throws Exception {
        String name = "Cash";

        AccountTypeRequest request = AccountTypeRequest.builder()
                .name(name)
                .build();

        // Create a new Account Type
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        // Delete the Account Type
        mockMvc.perform(delete(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Get all Account Types
        String categoriesJson = mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<AccountTypeResponse> responseList = objectMapper.readValue(
                categoriesJson, new TypeReference<>() {
                });


        Assertions.assertEquals(0, responseList.size());
    }

}

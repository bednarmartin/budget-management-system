package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
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
public class AccountRestControllerTests {

    private final String URL = "/api/account";
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String accountTypeName = "Cash";

        AccountTypeRequest request = AccountTypeRequest.builder()
                .name(accountTypeName)
                .build();

        // Create a new Account Type
        mockMvc.perform(post(URL + "/type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddAccount() throws Exception {
        String accountName = "Cash";
        String accountTypeName = "Cash";
        BigDecimal balance = BigDecimal.valueOf(10.59);

        CreateAccountRequest request = CreateAccountRequest.builder()
                .initialBalance(balance)
                .accountTypeName(accountTypeName)
                .name(accountName)
                .build();

        // Create a new Account
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Get the Account
        String responseJson = mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponse response = objectMapper.readValue(responseJson, AccountResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(accountName, response.getName());
        Assertions.assertEquals(accountTypeName, response.getAccountType().getName());
        Assertions.assertEquals(balance, response.getBalance());

    }

    @Test
    void testAddMoreAccounts() throws Exception {
        String[] accountNames = {"Cash", "Bank Account 1", "Bank Account 2"};
        String accountTypeName = "Cash";
        BigDecimal[] balances = {BigDecimal.valueOf(53.69), BigDecimal.valueOf(68.96), BigDecimal.valueOf(12.12)};

        for (int i = 0; i < accountNames.length; i++) {
            CreateAccountRequest request = CreateAccountRequest.builder()
                    .initialBalance(balances[i])
                    .accountTypeName(accountTypeName)
                    .name(accountNames[i])
                    .build();

            // Create a new Account
            mockMvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // Get all Transactions

        String accountJson = mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<AccountResponse> responseList = objectMapper.readValue(accountJson, new TypeReference<>() {
        });


        Assertions.assertEquals(3, responseList.size());
        for (int i = 0; i < responseList.size(); i++) {
            Assertions.assertEquals(accountNames[i], responseList.get(i).getName());
            Assertions.assertEquals(balances[i], responseList.get(i).getBalance());
            Assertions.assertEquals(accountTypeName, responseList.get(i).getAccountType().getName());
        }

    }

    @Test
    void testUpdateAccount() throws Exception {
        String accountName = "Cash";
        String accountTypeName = "Cash";
        BigDecimal balance = BigDecimal.valueOf(10.59);

        CreateAccountRequest request = CreateAccountRequest.builder()
                .initialBalance(balance)
                .accountTypeName(accountTypeName)
                .name(accountName)
                .build();

        // Create a new Account
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        String newName = "New Cash Account";
        UpdateAccountRequest updateRequest = UpdateAccountRequest.builder()
                .accountTypeName(accountTypeName)
                .name(newName)
                .build();

        // Update the Account
        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Get updated Account
        String responseJson = mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponse response = objectMapper.readValue(responseJson, AccountResponse.class);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals(accountTypeName, response.getAccountType().getName());
        Assertions.assertEquals(balance, response.getBalance());
        Assertions.assertEquals(newName, response.getName());
    }

    @Test
    void testDeleteAccount() throws Exception {
        String accountName = "Cash";
        String accountTypeName = "Cash";
        BigDecimal balance = BigDecimal.valueOf(10.59);

        CreateAccountRequest request = CreateAccountRequest.builder()
                .initialBalance(balance)
                .accountTypeName(accountTypeName)
                .name(accountName)
                .build();

        // Create a new Account
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        // Delete the Account Type
        mockMvc.perform(delete(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Get all Accounts
        String accountsJson = mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<AccountResponse> responseList = objectMapper.readValue(
                accountsJson, new TypeReference<>() {
                });


        Assertions.assertEquals(0, responseList.size());
    }

    @Test
    void testCreateValidation() throws Exception {
        String badName1 = "";
        String badName2 = "OK";
        BigDecimal badBalance = BigDecimal.valueOf(-56.58);
        String badAccountType = "";

        // Empty request
        CreateAccountRequest request = CreateAccountRequest.builder().build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Request without name
        request = CreateAccountRequest.builder()
                .accountTypeName("Cash")
                .initialBalance(BigDecimal.ZERO)
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Request without type
        request = CreateAccountRequest.builder()
                .name("Cash")
                .initialBalance(BigDecimal.ZERO)
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());


        // Request with bad balance
        request = CreateAccountRequest.builder()
                .name("Cash")
                .accountTypeName("Cash")
                .initialBalance(badBalance)
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Request with bad name
        request = CreateAccountRequest.builder()
                .name(badName1)
                .accountTypeName("Cash")
                .initialBalance(BigDecimal.ZERO)
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request = CreateAccountRequest.builder()
                .name(badName2)
                .accountTypeName("Cash")
                .initialBalance(BigDecimal.ZERO)
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Request with bad account type
        request = CreateAccountRequest.builder()
                .name("Cash")
                .accountTypeName(badAccountType)
                .initialBalance(BigDecimal.ZERO)
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testUpdateValidation() throws Exception {
        String badName1 = "";
        String badName2 = "OK";
        String badAccountType = "";

        String accountName = "Cash";
        String accountTypeName = "Cash";
        BigDecimal balance = BigDecimal.valueOf(10.59);

        CreateAccountRequest createAccountRequest = CreateAccountRequest.builder()
                .initialBalance(balance)
                .accountTypeName(accountTypeName)
                .name(accountName)
                .build();

        // Create a new Account
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountRequest)))
                .andExpect(status().isCreated());

        // Empty request
        UpdateAccountRequest request = UpdateAccountRequest.builder().build();

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Request without name
        request = UpdateAccountRequest.builder()
                .accountTypeName("Cash")
                .build();

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Request without type
        request = UpdateAccountRequest.builder()
                .name("Cash")
                .build();

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());


        //Request with bad name
        request = UpdateAccountRequest.builder()
                .name(badName1)
                .accountTypeName("Cash")
                .build();

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request = UpdateAccountRequest.builder()
                .name(badName2)
                .accountTypeName("Cash")
                .build();

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Request with bad account type
        request = UpdateAccountRequest.builder()
                .name("Cash")
                .accountTypeName(badAccountType)
                .build();

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }
}

package com.bednarmartin.budgetmanagementsystem.config;

import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.TransactionService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CategoryRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.TransactionRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AccountService accountService;

    private final AccountTypeService accountTypeService;

    private final CategoryService categoryService;

    private final TransactionService transactionService;

    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/account_types.json")) {
            List<AccountTypeRequest> requests = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            for (AccountTypeRequest request : requests) {
                accountTypeService.addAccountType(request);
            }
        }

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/accounts.json")) {
            List<CreateAccountRequest> requests = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            for (CreateAccountRequest request : requests) {
                accountService.addAccount(request);
            }
        }

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/categories.json")) {
            List<CategoryRequest> requests = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            for (CategoryRequest request : requests) {
                categoryService.addCategory(request);
            }
        }

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/transactions.json")) {
            List<TransactionRequest> requests = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            for (TransactionRequest request : requests) {
                transactionService.addTransaction(request);
            }
        }
    }
}

package com.bednarmartin.budgetmanagementsystem.service.api.response.mapper;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.Transaction;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class ResponseObjectMapperImpl implements ResponseObjectMapper {
    @Override
    public AccountResponse mapToAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build();
    }

    @Override
    public AccountTypeResponse mapToAccountTypeResponse(AccountType accountType) {
        return AccountTypeResponse.builder()
                .id(accountType.getId())
                .name(accountType.getName())
                .build();
    }

    @Override
    public CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .dateCreated(category.getDateCreated())
                .transactionType(category.getTransactionType())
                .build();
    }

    @Override
    public TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .category(transaction.getCategory())
                .type(transaction.getType())
                .dateCreated(transaction.getDateCreated())
                .account(transaction.getAccount())
                .build();
    }
}

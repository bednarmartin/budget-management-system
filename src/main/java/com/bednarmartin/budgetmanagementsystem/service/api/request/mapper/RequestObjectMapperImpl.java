package com.bednarmartin.budgetmanagementsystem.service.api.request.mapper;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.Transaction;
import com.bednarmartin.budgetmanagementsystem.service.api.request.*;
import org.springframework.stereotype.Component;

@Component
public class RequestObjectMapperImpl implements RequestObjectMapper {
    @Override
    public AccountTypeRequest mapToAccountTypeRequest(AccountType accountType) {
        return AccountTypeRequest.builder()
                .name(accountType.getName())
                .build();
    }

    @Override
    public CategoryRequest mapToCategoryRequest(Category category) {
        return CategoryRequest.builder()
                .name(category.getName())
                .transactionType(category.getTransactionType())
                .build();
    }

    @Override
    public CreateAccountRequest mapToCreateAccountRequest(Account account) {
        return CreateAccountRequest.builder()
                .initialBalance(account.getBalance())
                .accountTypeName(account.getAccountType().getName())
                .name(account.getName())
                .build();
    }

    @Override
    public CreateTransactionRequest mapToCreateTransactionRequest(Transaction transaction) {
        return CreateTransactionRequest.builder()
                .accountName(transaction.getAccount().getName())
                .amount(transaction.getAmount())
                .categoryName(transaction.getCategory().getName())
                .description(transaction.getDescription())
                .type(transaction.getType())
                .build();
    }

    @Override
    public UpdateAccountRequest mapToUpdateAccountRequest(Account account) {
        return UpdateAccountRequest.builder()
                .name(account.getName())
                .accountTypeName(account.getAccountType().getName())
                .build();
    }

    @Override
    public UpdateTransactionRequest mapToUpdateTransactionRequest(Transaction transaction) {
        return UpdateTransactionRequest.builder()
                .amount(transaction.getAmount())
                .categoryName(transaction.getCategory().getName())
                .description(transaction.getDescription())
                .build();
    }
}

package com.bednarmartin.budgetmanagementsystem.service.api.response.mapper;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.Transaction;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;

public interface ResponseObjectMapper {

    AccountResponse mapToAccountResponse(Account account);

    AccountTypeResponse mapToAccountTypeResponse(AccountType accountType);

    CategoryResponse mapToCategoryResponse(Category category);

    TransactionResponse mapToTransactionResponse(Transaction transaction);


}

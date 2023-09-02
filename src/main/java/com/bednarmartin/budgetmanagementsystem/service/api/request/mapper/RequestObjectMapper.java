package com.bednarmartin.budgetmanagementsystem.service.api.request.mapper;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.Transaction;
import com.bednarmartin.budgetmanagementsystem.service.api.request.*;

public interface RequestObjectMapper {

    AccountTypeRequest mapToAccountTypeRequest(AccountType accountType);

    CategoryRequest mapToCategoryRequest(Category category);

    CreateAccountRequest mapToCreateAccountRequest(Account account);

    CreateTransactionRequest mapToCreateTransactionRequest(Transaction transaction);

    UpdateAccountRequest mapToUpdateAccountRequest(Account account);

    UpdateTransactionRequest mapToUpdateTransactionRequest(Transaction transaction);
}

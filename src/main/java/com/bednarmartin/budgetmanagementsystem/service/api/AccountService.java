package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountResponse addAccount(CreateAccountRequest request);

    AccountResponse updateAccount(long id, UpdateAccountRequest request);

    void deleteAccountById(long id);

    AccountResponse getAccountById(long id);

    List<AccountResponse> getAllAccounts();

    Account getAccountByName(String name);

    void subtractFromBalance(Account account, BigDecimal amount);

    void addToBalance(Account account, BigDecimal amount);

}

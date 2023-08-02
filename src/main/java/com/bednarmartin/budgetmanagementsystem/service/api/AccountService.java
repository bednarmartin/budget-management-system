package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    AccountResponse addAccount(CreateAccountRequest request);

    @Transactional
    AccountResponse updateAccount(long id, UpdateAccountRequest request);

    void deleteAccountById(long id);

    AccountResponse getAccountById(long id);

    List<AccountResponse> getAllAccounts();

    Account getAccountByName(String name);

    @Transactional
    void subtractFromBalance(Account account, BigDecimal amount);

    @Transactional
    void addToBalance(Account account, BigDecimal amount);


}

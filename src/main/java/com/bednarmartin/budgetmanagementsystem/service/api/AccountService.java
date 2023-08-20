package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.annotations.LogMethod;
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

    @LogMethod
    AccountResponse getAccountById(long id);

    @LogMethod
    List<AccountResponse> getAllAccounts();

    @LogMethod
    Account getAccountByName(String name);

    @LogMethod
    @Transactional
    void subtractFromBalance(Account account, BigDecimal amount);

    @LogMethod
    @Transactional
    void addToBalance(Account account, BigDecimal amount);

}

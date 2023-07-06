package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountService {
    void addAccount(CreateAccountRequest request);

    @Transactional
    void updateAccount(long id, UpdateAccountRequest request);

    void deleteAccountById(long id);

    AccountResponse getAccountById(long id);

    List<AccountResponse> getAllAccounts();


}

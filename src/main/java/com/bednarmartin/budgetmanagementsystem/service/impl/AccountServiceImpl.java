package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.repository.AccountRepository;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    @Override
    public void addAccount(CreateAccountRequest request) {
        log.debug("addAccount with parameter: {} called", request);

        Account account = Account.builder()
                .name(request.getName())
                .accountType(request.getAccountType())
                .balance(request.getInitialBalance())
                .build();

        repository.save(account);

        log.info("Account with id: {} saved", account.getId());
        log.debug("Account: {} saved", account);
    }

    @Override
    public void updateAccount(long id, UpdateAccountRequest request) {
        log.debug("updateAccount with parameters: {}, {} called", id, request);

        repository.updateCategoryById(id, request.getName(), request.getAccountType());

        log.info("Account with id: {} updated", id);
        log.debug("Account: {} updated", request);
    }

    @Override
    public void deleteAccountById(long id) {
        log.debug("deleteAccountById with parameter: {} called", id);
        repository.deleteById(id);
        log.info("Account with id: {} deleted", id);
    }

    @Override
    public AccountResponse getAccountById(long id) {
        log.debug("getAccountById with parameter: {} called", id);

        String errorMessage = "Such Account not in database";
        Account account = repository.findById(id).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        AccountResponse accountResponse = mapToAccountResponse(account);

        log.debug("AccountResponse: {} returned", accountResponse);
        log.info("AccountResponse with id: {} returned", id);

        return accountResponse;
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        log.debug("getAllAccounts called");
        List<Account> accounts = repository.findAll();

        log.info("all AccountResponse returned");
        return accounts.stream().map(this::mapToAccountResponse).toList();
    }

    private AccountResponse mapToAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build();
    }
}

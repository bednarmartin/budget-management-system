package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.repository.AccountRepository;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    private final AccountTypeService accountTypeService;

    @Override
    public void addAccount(CreateAccountRequest request) {
        log.debug("addAccount with parameter: {} called", request);

        AccountType accountType = accountTypeService.getAccountTypeByName(request.getAccountTypeName());

        Account account = Account.builder()
                .name(request.getName())
                .accountType(accountType)
                .balance(request.getInitialBalance())
                .build();

        repository.save(account);

        log.info("Account with id: {} saved", account.getId());
        log.debug("Account: {} saved", account);
    }

    @Override
    public void updateAccount(long id, UpdateAccountRequest request) {
        log.debug("updateAccount with parameters: {}, {} called", id, request);

        AccountType accountType = accountTypeService.getAccountTypeByName(request.getAccountTypeName());

        repository.updateAccountById(id, request.getName(), accountType);

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

    @Override
    public Account getAccountByName(String name) {
        log.debug("getAccountByName with parameter: {} called", name);

        String errorMessage = "Such Account not in database";
        Account account = repository.findByName(name).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        log.debug("Account: {} returned", account);
        log.info("Account with id: {} returned", account.getId());

        return account;
    }

    @Override
    public void subtractFromBalance(Account account, BigDecimal amount) {
        log.debug("subtractFromBalance with parameters: {}, {} called", account, amount);

        Account updatedAccount = Account.builder()
                .id(account.getId())
                .accountType(account.getAccountType())
                .name(account.getName())
                .balance(account.getBalance().subtract(amount))
                .build();
        repository.save(updatedAccount);

        log.info("Account with id: {} updated", updatedAccount.getId());
        log.debug("Updated Account: {}", account);
    }

    @Override
    public void addToBalance(Account account, BigDecimal amount) {
        log.debug("addToBalance with parameters: {}, {} called", account, amount);

        Account updatedAccount = Account.builder()
                .id(account.getId())
                .accountType(account.getAccountType())
                .name(account.getName())
                .balance(account.getBalance().add(amount))
                .build();
        repository.save(updatedAccount);

        log.info("Account with id: {} updated", updatedAccount.getId());
        log.debug("Updated Account: {}", account);
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

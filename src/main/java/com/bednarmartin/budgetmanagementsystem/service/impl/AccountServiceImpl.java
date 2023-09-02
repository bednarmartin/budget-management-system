package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.annotations.LogMethod;
import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.repository.AccountRepository;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.mapper.ResponseObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    private final AccountTypeService accountTypeService;

    private final ResponseObjectMapper responseObjectMapper;
    private final String errorMessage = "Such Account not in database";

    @LogMethod
    @Override
    public AccountResponse addAccount(CreateAccountRequest request) {
        AccountType accountType = accountTypeService.getAccountTypeByName(request.getAccountTypeName());

        Account account = Account.builder()
                .name(request.getName())
                .accountType(accountType)
                .balance(request.getInitialBalance())
                .build();
        repository.save(account);

        return responseObjectMapper.mapToAccountResponse(account);
    }

    @LogMethod
    @Override
    public AccountResponse updateAccount(long id, UpdateAccountRequest request) {
        AccountType accountType = accountTypeService.getAccountTypeByName(request.getAccountTypeName());
        repository.updateAccountById(id, request.getName(), accountType);

        Account account = repository.findById(id).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        return responseObjectMapper.mapToAccountResponse(account);

    }

    @LogMethod
    @Override
    public void deleteAccountById(long id) {
        repository.deleteById(id);
    }

    @LogMethod
    @Override
    public AccountResponse getAccountById(long id) {
        Account account = repository.findById(id).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        return responseObjectMapper.mapToAccountResponse(account);
    }

    @LogMethod
    @Override
    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = repository.findAll();
        return accounts.stream().map(responseObjectMapper::mapToAccountResponse).toList();
    }

    @LogMethod
    @Override
    public Account getAccountByName(String name) {
        return repository.findByName(name).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
    }

    @LogMethod
    @Override
    public void subtractFromBalance(Account account, BigDecimal amount) {
        Account updatedAccount = Account.builder()
                .id(account.getId())
                .accountType(account.getAccountType())
                .name(account.getName())
                .balance(account.getBalance().subtract(amount))
                .build();
        repository.save(updatedAccount);
    }

    @LogMethod
    @Override
    public void addToBalance(Account account, BigDecimal amount) {
        Account updatedAccount = Account.builder()
                .id(account.getId())
                .accountType(account.getAccountType())
                .name(account.getName())
                .balance(account.getBalance().add(amount))
                .build();

        repository.save(updatedAccount);
    }

}

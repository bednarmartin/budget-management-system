package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.repository.AccountTypeRepository;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepository repository;

    @Override
    public void addAccountType(AccountTypeRequest request) {
        log.debug("addAccountType with parameter: {} called", request);

        AccountType accountType = AccountType.builder()
                .name(request.getName())
                .build();
        repository.save(accountType);

        log.info("AccountType with id: {} saved", accountType.getId());
        log.debug("AccountType: {} saved", accountType);
    }

    @Override
    public void updateAccountType(long id, AccountTypeRequest request) {
        log.debug("updateAccountType with parameters: {}, {} called", id, request);

        repository.updateAccountTypeById(id, request.getName());

        log.info("AccountType with id: {} updated", id);
        log.debug("AccountType: {} updated", request);
    }

    @Override
    public void deleteAccountTypeById(long id) {
        log.debug("deleteAccountTypeById with parameter: {} called", id);
        repository.deleteById(id);
        log.info("AccountType with id: {} deleted", id);

    }

    @Override
    public AccountTypeResponse getAccountTypeById(long id) {
        log.debug("getAccountTypeById with parameter: {} called", id);

        String errorMessage = "Such AccountType not in database";
        AccountType accountType = repository.findById(id).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        AccountTypeResponse accountTypeResponse = mapToAccountTypeResponse(accountType);

        log.debug("AccountTypeResponse: {} returned", accountTypeResponse);
        log.info("AccountTypeResponse with id: {} returned", id);

        return accountTypeResponse;
    }

    @Override
    public List<AccountTypeResponse> getAllAccountTypes() {
        log.debug("getAllAccountTypes called");
        List<AccountType> accountTypes = repository.findAll();

        log.info("all AccountTypeResponse returned");
        return accountTypes.stream().map(this::mapToAccountTypeResponse).toList();
    }

    @Override
    public AccountType getAccountTypeByName(String name) {
        log.debug("getAccountTypeByName with parameter: {} called", name);

        String errorMessage = "Such AccountType is not in the Database";
        AccountType accountType = repository.findByName(name)
                .orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        log.debug("AccountType: {} returned", accountType);
        log.info("AccountType with id: {} returned", accountType.getId());

        return accountType;
    }

    @Override
    public void deleteAccountTypeByName(String name) {
        log.debug("deleteAccountTypeByName with parameter: {} called", name);
        repository.deleteByName(name);
        log.info("AccountType with name: {} deleted", name);
    }

    private AccountTypeResponse mapToAccountTypeResponse(AccountType accountType) {
        return AccountTypeResponse.builder()
                .id(accountType.getId())
                .name(accountType.getName())
                .build();
    }
}

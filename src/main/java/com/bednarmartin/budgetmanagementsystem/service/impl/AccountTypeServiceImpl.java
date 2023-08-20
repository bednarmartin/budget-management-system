package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.annotations.LogMethod;
import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.repository.AccountTypeRepository;
import com.bednarmartin.budgetmanagementsystem.exception.DatabaseDuplicateException;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.mapper.ResponseObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepository repository;

    private final ResponseObjectMapper responseObjectMapper;
    private final String errorMessage = "Such Account Type not in database";

    @LogMethod
    @Override
    public AccountTypeResponse addAccountType(AccountTypeRequest request) {
        checkDuplicate(request.getName());

        AccountType accountType = AccountType.builder()
                .name(request.getName())
                .build();

        repository.save(accountType);
        return responseObjectMapper.mapToAccountTypeResponse(accountType);
    }

    @LogMethod
    @Override
    public AccountTypeResponse updateAccountType(long id, AccountTypeRequest request) {
        repository.findById(id).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        repository.updateAccountTypeById(id, request.getName());
        return AccountTypeResponse.builder().id(id).name(request.getName()).build();
    }

    @LogMethod
    @Override
    public void deleteAccountTypeById(long id) {
        repository.deleteById(id);
    }

    @LogMethod
    @Override
    public AccountTypeResponse getAccountTypeById(long id) {
        AccountType accountType = repository.findById(id).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        return responseObjectMapper.mapToAccountTypeResponse(accountType);
    }

    @LogMethod
    @Override
    public List<AccountTypeResponse> getAllAccountTypes() {
        List<AccountType> accountTypes = repository.findAll();
        return accountTypes.stream().map(responseObjectMapper::mapToAccountTypeResponse).toList();
    }

    @LogMethod
    @Override
    public AccountType getAccountTypeByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
    }

    @LogMethod
    @Override
    public void deleteAccountTypeByName(String name) {
        repository.findByName(name).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        repository.deleteByName(name);
    }

    @LogMethod
    private void checkDuplicate(String name) {
        if (repository.findByName(name).isPresent()) {
            throw new DatabaseDuplicateException("Account Type with the same name already in the database");
        }
    }
}

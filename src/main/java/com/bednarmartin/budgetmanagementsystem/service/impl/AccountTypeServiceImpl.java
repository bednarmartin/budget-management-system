package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.repository.AccountTypeRepository;
import com.bednarmartin.budgetmanagementsystem.exception.DatabaseDuplicateException;
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
    public AccountTypeResponse addAccountType(AccountTypeRequest request) {
        log.debug("addAccountType with parameter: {} called", request);

        checkDuplicate(request.getName());

        AccountType accountType = AccountType.builder()
                .name(request.getName())
                .build();

        repository.save(accountType);

        log.info("AccountType with id: {} saved", accountType.getId());
        log.debug("AccountType: {} saved", accountType);

        return AccountTypeService.mapToAccountTypeResponse(accountType);
    }

    @Override
    public AccountTypeResponse updateAccountType(long id, AccountTypeRequest request) {
        log.debug("updateAccountType with parameters: {}, {} called", id, request);

        String errorMessage = "Such Account Type not in database";
        repository.findById(id).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        repository.updateAccountTypeById(id, request.getName());

        log.info("AccountType with id: {} updated", id);
        log.debug("AccountType: {} updated", request);

        return AccountTypeResponse.builder().id(id).name(request.getName()).build();


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

        String errorMessage = "Such Account Type not in database";
        AccountType accountType = repository.findById(id).
                orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        AccountTypeResponse accountTypeResponse = AccountTypeService.mapToAccountTypeResponse(accountType);

        log.debug("AccountTypeResponse: {} returned", accountTypeResponse);
        log.info("AccountTypeResponse with id: {} returned", id);

        return accountTypeResponse;
    }

    @Override
    public List<AccountTypeResponse> getAllAccountTypes() {
        log.debug("getAllAccountTypes called");
        List<AccountType> accountTypes = repository.findAll();

        log.info("all AccountTypeResponse returned");
        return accountTypes.stream().map(AccountTypeService::mapToAccountTypeResponse).toList();
    }

    @Override
    public AccountType getAccountTypeByName(String name) {
        log.debug("getAccountTypeByName with parameter: {} called", name);

        String errorMessage = "Such Account Type is not in the Database";
        AccountType accountType = repository.findByName(name)
                .orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        log.debug("AccountType: {} returned", accountType);
        log.info("AccountType with id: {} returned", accountType.getId());

        return accountType;
    }

    @Override
    public void deleteAccountTypeByName(String name) {
        log.debug("deleteAccountTypeByName with parameter: {} called", name);

        String errorMessage = "Such Account Type not in database";
        repository.findByName(name).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        repository.deleteByName(name);
        log.info("AccountType with name: {} deleted", name);
    }


    private void checkDuplicate(String name) {
        if (repository.findByName(name).isPresent()) {
            throw new DatabaseDuplicateException("Account Type with the same name already in the database");
        }
    }
}

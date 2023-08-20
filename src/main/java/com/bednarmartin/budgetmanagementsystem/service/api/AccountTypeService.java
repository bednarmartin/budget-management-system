package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountTypeService {

    AccountTypeResponse addAccountType(AccountTypeRequest request);

    @Transactional
    AccountTypeResponse updateAccountType(long id, AccountTypeRequest request);

    void deleteAccountTypeById(long id);

    AccountTypeResponse getAccountTypeById(long id);

    List<AccountTypeResponse> getAllAccountTypes();

    AccountType getAccountTypeByName(String name);

    @Transactional
    void deleteAccountTypeByName(String name);

}

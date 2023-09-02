package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;

import java.util.List;

public interface AccountTypeService {

    AccountTypeResponse addAccountType(AccountTypeRequest request);

    AccountTypeResponse updateAccountType(long id, AccountTypeRequest request);

    void deleteAccountTypeById(long id);

    AccountTypeResponse getAccountTypeById(long id);

    List<AccountTypeResponse> getAllAccountTypes();

    AccountType getAccountTypeByName(String name);

    void deleteAccountTypeByName(String name);

}

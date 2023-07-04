package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountTypeService {

    void addAccountType(AccountTypeRequest request);

    @Transactional
    void updateAccountType(long id, AccountTypeRequest request);

    void deleteAccountTypeById(long id);

    AccountTypeResponse getAccountTypeById(long id);

    List<AccountTypeResponse> getAllAccountTypes();

}

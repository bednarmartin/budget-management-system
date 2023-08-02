package com.bednarmartin.budgetmanagementsystem.controller.graphql;

import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountTypeGraphqlController {

    private final AccountTypeService accountTypeService;

    @QueryMapping
    public AccountTypeResponse getAccountTypeById(@Argument Long id) {
        return accountTypeService.getAccountTypeById(id);
    }

    @QueryMapping
    public List<AccountTypeResponse> getAllAccountTypes() {
        return accountTypeService.getAllAccountTypes();
    }

    @MutationMapping
    public AccountTypeResponse createAccountType(@Argument AccountTypeRequest request) {
        return accountTypeService.addAccountType(request);
    }

    @MutationMapping
    public AccountTypeResponse updateAccountType(@Argument Long id, @Argument AccountTypeRequest request) {
        return accountTypeService.updateAccountType(id, request);
    }

    @MutationMapping
    public String deleteAccountType(@Argument Long id) {
        accountTypeService.deleteAccountTypeById(id);
        return "Account Type with id: " + id + " deleted";
    }


}

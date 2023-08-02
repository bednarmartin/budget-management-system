package com.bednarmartin.budgetmanagementsystem.controller.graphql;

import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountGraphqlController {

    private final AccountService accountService;

    @QueryMapping
    public AccountResponse getAccountById(@Argument Long id) {
        return accountService.getAccountById(id);
    }

    @QueryMapping
    public List<AccountResponse> getAllAccounts() {
        return accountService.getAllAccounts();

    }

    @MutationMapping
    public AccountResponse createAccount(@Argument CreateAccountRequest request) {
        return accountService.addAccount(request);
    }

    @MutationMapping
    public AccountResponse updateAccount(@Argument Long id, @Argument UpdateAccountRequest request) {
        return accountService.updateAccount(id, request);
    }

    @MutationMapping
    public String deleteAccount(@Argument Long id) {
        accountService.deleteAccountById(id);
        return "Account with id: " + id + " deleted";
    }
}

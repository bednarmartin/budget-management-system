package com.bednarmartin.budgetmanagementsystem.controller.graphql;

import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
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
}

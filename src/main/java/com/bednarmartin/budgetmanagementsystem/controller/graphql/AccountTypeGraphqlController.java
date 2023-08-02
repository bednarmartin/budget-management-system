package com.bednarmartin.budgetmanagementsystem.controller.graphql;

import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountTypeGraphqlController {

    private final AccountTypeService accountTypeService;

    @QueryMapping
    public AccountTypeResponse getAccountTypeById(@Argument Long id){
        return accountTypeService.getAccountTypeById(id);
    }

    @QueryMapping
    public List<AccountTypeResponse> getAllAccountTypes(){
        return accountTypeService.getAllAccountTypes();
    }




}

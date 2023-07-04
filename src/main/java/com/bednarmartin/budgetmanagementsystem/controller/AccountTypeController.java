package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account/type")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountTypeResponse getCategoryById(@PathVariable long id) {
        return accountTypeService.getAccountTypeById(id);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountTypeResponse> getAllCategories() {
        return accountTypeService.getAllAccountTypes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addCategory(@RequestBody AccountTypeRequest request) {
        accountTypeService.addAccountType(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable long id) {
        accountTypeService.deleteAccountTypeById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCategory(@PathVariable long id, @RequestBody AccountTypeRequest request) {
        accountTypeService.updateAccountType(id, request);
    }
}

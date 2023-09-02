package com.bednarmartin.budgetmanagementsystem.controller.rest;

import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable long id) {
        return new ResponseEntity<>(accountService.getAccountById(id), OK);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addAccount(@Valid @RequestBody CreateAccountRequest request) {
        String message = "Account created successfully";
        accountService.addAccount(request);
        return new ResponseEntity<>(Map.of("message", message), CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAccount(@PathVariable long id) {
        String message = "Account deleted successfully";
        accountService.deleteAccountById(id);
        return new ResponseEntity<>(Map.of("message", message), OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateAccount(@PathVariable long id, @Valid @RequestBody UpdateAccountRequest request) {
        String message = "Account updated successfully";
        accountService.updateAccount(id, request);
        return new ResponseEntity<>(Map.of("message", message), OK);
    }
}

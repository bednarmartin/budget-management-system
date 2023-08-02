package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.exception.DatabaseDuplicateException;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable long id) {
        AccountResponse accountResponse = null;
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            accountResponse = accountService.getAccountById(id);
        } catch (SuchElementNotInDatabaseException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(accountResponse, httpStatus);

    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> accountResponses = new ArrayList<>();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            accountResponses = accountService.getAllAccounts();
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(accountResponses, httpStatus);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addAccount(@Valid @RequestBody CreateAccountRequest request) {
        String message = "Account created successfully";
        HttpStatus httpStatus = HttpStatus.CREATED;

        try {
            accountService.addAccount(request);
        } catch (DatabaseDuplicateException e) {
            message = e.getMessage();
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            message = "Something went wrong";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(Map.of("message", message), httpStatus);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAccount(@PathVariable long id) {
        String message = "Account deleted successfully";
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            accountService.deleteAccountById(id);
        } catch (SuchElementNotInDatabaseException e) {
            message = e.getMessage();
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            message = "Something went wrong";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(Map.of("message", message), httpStatus);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateAccount(@PathVariable long id, @Valid @RequestBody UpdateAccountRequest request) {
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Account updated successfully";

        try {
            accountService.updateAccount(id, request);
        } catch (SuchElementNotInDatabaseException e) {
            message = e.getMessage();
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            message = "Something went wrong";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(Map.of("message", message), httpStatus);
    }
}

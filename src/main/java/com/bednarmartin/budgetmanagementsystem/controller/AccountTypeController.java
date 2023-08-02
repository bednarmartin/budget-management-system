package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.exception.DatabaseDuplicateException;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/account/type")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountTypeResponse> getAccountTypeById(@PathVariable long id) {
        AccountTypeResponse accountTypeResponse = null;
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            accountTypeResponse = accountTypeService.getAccountTypeById(id);
        } catch (SuchElementNotInDatabaseException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(accountTypeResponse, httpStatus);

    }

    @GetMapping
    public ResponseEntity<List<AccountTypeResponse>> getAllAccountTypes() {
        List<AccountTypeResponse> accountTypeResponses = new ArrayList<>();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            accountTypeResponses = accountTypeService.getAllAccountTypes();
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(accountTypeResponses, httpStatus);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addAccountType(@Valid @RequestBody AccountTypeRequest request) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        String message = "Account Type created successfully";

        try {
            accountTypeService.addAccountType(request);
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
    public ResponseEntity<Map<String, String>> deleteAccountType(@PathVariable long id) {
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Account Type deleted successfully";

        try {
            accountTypeService.deleteAccountTypeById(id);
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
    public ResponseEntity<Map<String, String>> updateAccountType(@PathVariable long id, @Valid @RequestBody AccountTypeRequest request) {
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Account Type updated successfully";
        try {
            accountTypeService.updateAccountType(id, request);
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

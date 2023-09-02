package com.bednarmartin.budgetmanagementsystem.controller.rest;

import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/account/type")
@RequiredArgsConstructor
public class AccountTypeRestController {

    private final AccountTypeService accountTypeService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountTypeResponse> getAccountTypeById(@PathVariable long id) {
        return new ResponseEntity<>(accountTypeService.getAccountTypeById(id), OK);

    }

    @GetMapping
    public ResponseEntity<List<AccountTypeResponse>> getAllAccountTypes() {
        return new ResponseEntity<>(accountTypeService.getAllAccountTypes(), OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addAccountType(@Valid @RequestBody AccountTypeRequest request) {
        String message = "Account Type created successfully";
        accountTypeService.addAccountType(request);
        return new ResponseEntity<>(Map.of("message", message), CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAccountType(@PathVariable long id) {
        String message = "Account Type deleted successfully";
        accountTypeService.deleteAccountTypeById(id);
        return new ResponseEntity<>(Map.of("message", message), OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateAccountType(@PathVariable long id, @Valid @RequestBody AccountTypeRequest request) {
        String message = "Account Type updated successfully";
        accountTypeService.updateAccountType(id, request);
        return new ResponseEntity<>(Map.of("message", message), OK);
    }
}

package com.bednarmartin.budgetmanagementsystem.controller.rest;

import com.bednarmartin.budgetmanagementsystem.service.api.TransactionService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateTransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionRestController {

    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable long id) {
        return new ResponseEntity<>(transactionService.getTransactionById(id), OK);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return new ResponseEntity<>(transactionService.getAllTransactions(), OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addTransaction(@Valid @RequestBody CreateTransactionRequest request) {
        String message = "Transaction created successfully";
        transactionService.addTransaction(request);
        return new ResponseEntity<>(Map.of("message", message), CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTransaction(@PathVariable long id) {
        String message = "Transaction deleted successfully";
        transactionService.deleteTransactionById(id);
        return new ResponseEntity<>(Map.of("message", message), OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateTransaction(@PathVariable long id, @Valid @RequestBody CreateTransactionRequest request) {
        String message = "Transaction updated successfully";
        transactionService.updateTransaction(id, request);
        return new ResponseEntity<>(Map.of("message", message), OK);
    }


}

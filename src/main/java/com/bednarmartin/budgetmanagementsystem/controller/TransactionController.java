package com.bednarmartin.budgetmanagementsystem.controller;

import com.bednarmartin.budgetmanagementsystem.exception.DatabaseDuplicateException;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.service.api.TransactionService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.TransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable long id) {
        TransactionResponse response = null;
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            response = transactionService.getTransactionById(id);
        } catch (SuchElementNotInDatabaseException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> responses = new ArrayList<>();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            responses = transactionService.getAllTransactions();
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(responses, httpStatus);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addTransaction(@Valid @RequestBody TransactionRequest request) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        String message = "Transaction created successfully";

        try {
            transactionService.addTransaction(request);
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
    public ResponseEntity<Map<String, String>> deleteTransaction(@PathVariable long id) {
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Transaction deleted successfully";

        try {
            transactionService.deleteTransactionById(id);
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
    public ResponseEntity<Map<String, String>> updateTransaction(@PathVariable long id, @Valid @RequestBody TransactionRequest request) {
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Transaction updated successfully";
        try {
            transactionService.updateTransaction(id, request);
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

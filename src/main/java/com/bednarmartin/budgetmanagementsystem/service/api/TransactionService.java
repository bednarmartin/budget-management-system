package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateTransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse addTransaction(CreateTransactionRequest request);

    TransactionResponse updateTransaction(long id, CreateTransactionRequest request);

    void deleteTransactionById(long id);

    List<TransactionResponse> getAllTransactions();

    TransactionResponse getTransactionById(long id);
}


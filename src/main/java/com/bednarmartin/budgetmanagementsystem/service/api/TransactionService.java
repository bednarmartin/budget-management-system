package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.db.model.Transaction;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateTransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionService {

    TransactionResponse addTransaction(CreateTransactionRequest request);

    @Transactional
    TransactionResponse updateTransaction(long id, CreateTransactionRequest request);

    void deleteTransactionById(long id);

    List<TransactionResponse> getAllTransactions();

    TransactionResponse getTransactionById(long id);

    static TransactionResponse mapTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .category(transaction.getCategory())
                .type(transaction.getType())
                .dateCreated(transaction.getDateCreated())
                .account(transaction.getAccount())
                .build();
    }
}


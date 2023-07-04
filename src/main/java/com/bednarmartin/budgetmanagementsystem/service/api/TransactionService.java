package com.bednarmartin.budgetmanagementsystem.service.api;

import com.bednarmartin.budgetmanagementsystem.service.api.request.TransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionService {

    void addTransaction(TransactionRequest request);
    @Transactional
    void updateTransaction(long id, TransactionRequest request);

    void deleteTransactionById(long id);

    List<TransactionResponse> getAllTransactions();

    TransactionResponse getTransactionById(long id);
}

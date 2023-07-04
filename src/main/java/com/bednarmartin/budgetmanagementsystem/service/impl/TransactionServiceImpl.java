package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.db.model.Transaction;
import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.repository.TransactionRepository;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.exception.TransactionTypeMismatchException;
import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.TransactionService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.TransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    private final CategoryService categoryService;

    @Override
    public void addTransaction(TransactionRequest request) {
        log.debug("addTransaction with parameter: {} called", request);

        Category category = categoryService.getCategoryByName(request.getCategoryName());

        checkTransactionTypes(request, category);

        LocalDateTime actualTime = LocalDateTime.now();
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .category(category)
                .description(request.getDescription())
                .dateCreated(actualTime)
                .dateUpdated(actualTime)
                .type(request.getType())
                .build();
        repository.save(transaction);

        log.info("Transaction with id: {} saved", transaction.getId());
        log.debug("Transaction: {} saved", transaction);

    }


    @Override
    public void updateTransaction(long id, TransactionRequest request) {
        log.debug("updateTransaction with parameters: {}, {} called", id, request);

        Category category = categoryService.getCategoryByName(
                request.getCategoryName());

        checkTransactionTypes(request, category);

        LocalDateTime actualTime = LocalDateTime.now();
        repository.updateTransactionById(id,
                request.getAmount(),
                request.getDescription(),
                category,
                request.getType(),
                actualTime);

        log.info("Transaction with id: {} updated", id);
        log.debug("Transaction: {} updated", request);
    }

    @Override
    public void deleteTransactionById(long id) {
        log.debug("deleteTransactionById with parameter: {} called", id);
        repository.deleteById(id);
        log.info("Transaction with id: {} deleted", id);
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        log.debug("getAllTransactions called");
        List<Transaction> transactions = repository.findAll();

        log.info("all TransactionResponses returned");
        return transactions.stream().map(this::mapTransactionResponse).toList();
    }

    @Override
    public TransactionResponse getTransactionById(long id) {
        log.debug("getTransactionById with parameter: {} called", id);

        String errorMessage = "Such Transaction not in database";
        Transaction transaction = repository.findById(id).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        TransactionResponse transactionResponse = mapTransactionResponse(transaction);

        log.debug("TransactionResponse: {} returned", transactionResponse);
        log.info("TransactionResponse with id: {} returned", id);

        return transactionResponse;
    }

    private TransactionResponse mapTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .category(transaction.getCategory())
                .type(transaction.getType())
                .dateCreated(transaction.getDateCreated())
                .build();
    }

    private void checkTransactionTypes(TransactionRequest transactionRequest, Category category) {
        boolean notSameTransactionTypes = !category.getTransactionType().equals(transactionRequest.getType());
        if (notSameTransactionTypes) {
            throw new TransactionTypeMismatchException("Transaction types of request and category must be the same!");
        }
    }
}

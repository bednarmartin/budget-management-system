package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.Transaction;
import com.bednarmartin.budgetmanagementsystem.db.repository.TransactionRepository;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import com.bednarmartin.budgetmanagementsystem.exception.TransactionTypeMismatchException;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.TransactionService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateTransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    private final CategoryService categoryService;

    private final AccountService accountService;

    @Override
    public TransactionResponse addTransaction(CreateTransactionRequest request) {
        log.debug("addTransaction with parameter: {} called", request);

        Category category = categoryService.getCategoryByName(request.getCategoryName());
        Account account = accountService.getAccountByName(request.getAccountName());

        checkTransactionTypes(request, category);
        updateAccountBalance(request, account);
        account = accountService.getAccountByName(request.getAccountName());

        LocalDateTime actualTime = LocalDateTime.now();
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .category(category)
                .description(request.getDescription())
                .dateCreated(actualTime)
                .dateUpdated(actualTime)
                .type(request.getType())
                .account(account)
                .build();
        repository.save(transaction);

        log.info("Transaction with id: {} saved", transaction.getId());
        log.debug("Transaction: {} saved", transaction);

        return TransactionService.mapTransactionResponse(transaction);

    }

    @Override
    public TransactionResponse updateTransaction(long id, CreateTransactionRequest request) {
        log.debug("updateTransaction with parameters: {}, {} called", id, request);

        Category category = categoryService.getCategoryByName(request.getCategoryName());
        Account account = accountService.getAccountByName(request.getAccountName());

        String errorMessage = "Such Transaction not in database";
        Transaction transaction = repository.findById(id).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        BigDecimal originalBalance = transaction.getAmount();
        BigDecimal newBalance;
        switch (request.getType()) {
            case INCOME -> newBalance = account.getBalance().subtract(originalBalance).add(request.getAmount());
            case EXPENSE -> newBalance = account.getBalance().add(originalBalance).subtract(request.getAmount());
            default -> throw new IllegalArgumentException();
        }
        account.setBalance(newBalance);
        accountService.updateAccount(account.getId(), AccountService.mapToUpdateAccountRequest(account));

        checkTransactionTypes(request, category);

        LocalDateTime actualTime = LocalDateTime.now();
        repository.updateTransactionById(id,
                request.getAmount(),
                request.getDescription(),
                category,
                request.getType(),
                account,
                actualTime);

        transaction = repository.findById(id).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        log.info("Transaction with id: {} updated", id);
        log.debug("Transaction: {} updated", request);

        return TransactionService.mapTransactionResponse(transaction);
    }

    @Override
    public void deleteTransactionById(long id) {
        log.debug("deleteTransactionById with parameter: {} called", id);

        String errorMessage = "Such Transaction not in database";
        Transaction transaction = repository.findById(id).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        Account account = transaction.getAccount();

        BigDecimal newBalance;
        switch (transaction.getType()){
            case INCOME -> newBalance = account.getBalance().subtract(transaction.getAmount());
            case EXPENSE -> newBalance = account.getBalance().add(transaction.getAmount());
            default -> throw new IllegalArgumentException();
        }
        account.setBalance(newBalance);

        accountService.updateAccount(account.getId(), AccountService.mapToUpdateAccountRequest(account));
        repository.deleteById(id);

        log.info("Transaction with id: {} deleted", id);
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        log.debug("getAllTransactions called");

        List<Transaction> transactions = repository.findAll();

        log.info("all TransactionResponses returned");
        return transactions.stream().map(TransactionService::mapTransactionResponse).toList();
    }

    @Override
    public TransactionResponse getTransactionById(long id) {
        log.debug("getTransactionById with parameter: {} called", id);

        String errorMessage = "Such Transaction not in database";
        Transaction transaction = repository.findById(id).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        TransactionResponse transactionResponse = TransactionService.mapTransactionResponse(transaction);

        log.debug("TransactionResponse: {} returned", transactionResponse);
        log.info("TransactionResponse with id: {} returned", id);

        return transactionResponse;
    }


    private void checkTransactionTypes(CreateTransactionRequest createTransactionRequest, Category category) {
        boolean notSameTransactionTypes = !category.getTransactionType().equals(createTransactionRequest.getType());
        if (notSameTransactionTypes) {
            throw new TransactionTypeMismatchException("Transaction types of request and category must be the same!");
        }
    }

    private void updateAccountBalance(CreateTransactionRequest request, Account account) {
        switch (request.getType()) {
            case INCOME -> accountService.addToBalance(account, request.getAmount());
            case EXPENSE -> accountService.subtractFromBalance(account, request.getAmount());
        }
    }
}
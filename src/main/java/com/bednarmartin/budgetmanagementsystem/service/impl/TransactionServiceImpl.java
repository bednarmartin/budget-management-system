package com.bednarmartin.budgetmanagementsystem.service.impl;

import com.bednarmartin.budgetmanagementsystem.annotations.LogMethod;
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
import com.bednarmartin.budgetmanagementsystem.service.api.request.mapper.RequestObjectMapper;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.mapper.ResponseObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    private final CategoryService categoryService;

    private final AccountService accountService;

    private final ResponseObjectMapper responseObjectMapper;

    private final RequestObjectMapper requestObjectMapper;
    private final String errorMessage = "Such Transaction not in database";

    @LogMethod
    @Override
    public TransactionResponse addTransaction(CreateTransactionRequest request) {
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
        return responseObjectMapper.mapToTransactionResponse(transaction);

    }

    @LogMethod
    @Override
    public TransactionResponse updateTransaction(long id, CreateTransactionRequest request) {
        Category category = categoryService.getCategoryByName(request.getCategoryName());
        Account account = accountService.getAccountByName(request.getAccountName());
        Transaction transaction = repository.findById(id).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));

        BigDecimal originalBalance = transaction.getAmount();
        BigDecimal newBalance;
        switch (request.getType()) {
            case INCOME -> newBalance = account.getBalance().subtract(originalBalance).add(request.getAmount());
            case EXPENSE -> newBalance = account.getBalance().add(originalBalance).subtract(request.getAmount());
            default -> throw new IllegalArgumentException();
        }
        account.setBalance(newBalance);
        accountService.updateAccount(account.getId(), requestObjectMapper.mapToUpdateAccountRequest(account));

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
        return responseObjectMapper.mapToTransactionResponse(transaction);
    }

    @LogMethod
    @Override
    public void deleteTransactionById(long id) {
        Transaction transaction = repository.findById(id).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        Account account = transaction.getAccount();

        BigDecimal newBalance;
        switch (transaction.getType()) {
            case INCOME -> newBalance = account.getBalance().subtract(transaction.getAmount());
            case EXPENSE -> newBalance = account.getBalance().add(transaction.getAmount());
            default -> throw new IllegalArgumentException();
        }
        account.setBalance(newBalance);

        accountService.updateAccount(account.getId(), requestObjectMapper.mapToUpdateAccountRequest(account));
        repository.deleteById(id);
    }

    @LogMethod
    @Override
    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = repository.findAll();
        return transactions.stream().map(responseObjectMapper::mapToTransactionResponse).toList();
    }

    @LogMethod
    @Override
    public TransactionResponse getTransactionById(long id) {
        Transaction transaction = repository.findById(id).orElseThrow(() -> new SuchElementNotInDatabaseException(errorMessage));
        return responseObjectMapper.mapToTransactionResponse(transaction);
    }

    @LogMethod
    private void checkTransactionTypes(CreateTransactionRequest createTransactionRequest, Category category) {
        boolean notSameTransactionTypes = !category.getTransactionType().equals(createTransactionRequest.getType());
        if (notSameTransactionTypes) {
            throw new TransactionTypeMismatchException("Transaction types of request and category must be the same!");
        }
    }

    @LogMethod
    private void updateAccountBalance(CreateTransactionRequest request, Account account) {
        switch (request.getType()) {
            case INCOME -> accountService.addToBalance(account, request.getAmount());
            case EXPENSE -> accountService.subtractFromBalance(account, request.getAmount());
        }
    }
}
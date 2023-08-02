package com.bednarmartin.budgetmanagementsystem.controller.graphql;

import com.bednarmartin.budgetmanagementsystem.service.api.TransactionService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.TransactionRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionGraphqlController {

    private final TransactionService transactionService;

    @QueryMapping
    public TransactionResponse getTransactionById(@Argument Long id) {
        return transactionService.getTransactionById(id);
    }

    @QueryMapping
    public List<TransactionResponse> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @MutationMapping
    public TransactionResponse createTransaction(@Argument TransactionRequest request) {
        return transactionService.addTransaction(request);
    }

    @MutationMapping
    public TransactionResponse updateTransaction(@Argument Long id, @Argument TransactionRequest request) {
        return transactionService.updateTransaction(id, request);
    }

    @MutationMapping
    public String deleteTransaction(@Argument Long id) {
        transactionService.deleteTransactionById(id);
        return "Category with id: " + id + " deleted";
    }

}

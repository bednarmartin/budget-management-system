package com.bednarmartin.budgetmanagementsystem.exception;

public class TransactionTypeMismatchException extends RuntimeException {
    public TransactionTypeMismatchException(String errorMessage) {
        super(errorMessage);
    }
}

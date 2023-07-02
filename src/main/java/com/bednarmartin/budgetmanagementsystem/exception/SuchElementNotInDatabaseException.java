package com.bednarmartin.budgetmanagementsystem.exception;

public class SuchElementNotInDatabaseException extends RuntimeException {
    public SuchElementNotInDatabaseException(String errorMessage) {
        super(errorMessage);
    }
}
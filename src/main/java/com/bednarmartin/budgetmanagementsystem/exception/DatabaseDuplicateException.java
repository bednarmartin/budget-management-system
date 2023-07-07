package com.bednarmartin.budgetmanagementsystem.exception;

public class DatabaseDuplicateException extends RuntimeException {

    public DatabaseDuplicateException(String errorMessage) {
        super(errorMessage);
    }
}

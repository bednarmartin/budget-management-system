package com.bednarmartin.budgetmanagementsystem.controller.rest.handlers;

import com.bednarmartin.budgetmanagementsystem.exception.DatabaseDuplicateException;
import com.bednarmartin.budgetmanagementsystem.exception.SuchElementNotInDatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(SuchElementNotInDatabaseException.class)
    public ResponseEntity<Map<String, String>> handleSuchElementNotInDatabaseException(
            SuchElementNotInDatabaseException exception) {
        log.warn(exception.getMessage());
        return new ResponseEntity<>(Map.of("message", exception.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler(DatabaseDuplicateException.class)
    public ResponseEntity<Map<String, String>> handleDatabaseDuplicateException(DatabaseDuplicateException exception) {
        log.warn(exception.getMessage());
        return new ResponseEntity<>(Map.of("message", exception.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception exception) {
        log.warn(exception.getMessage());
        return new ResponseEntity<>(Map.of("message", exception.getMessage()), INTERNAL_SERVER_ERROR);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}

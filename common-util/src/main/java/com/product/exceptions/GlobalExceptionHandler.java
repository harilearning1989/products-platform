package com.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleRuntimeException(DuplicateResourceException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("Duplicate Resource");
        problem.setDetail("Product SKU already exists");
        problem.setInstance(URI.create("/products"));

        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {

        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Validation Failed");

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://api.example.com/errors/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}

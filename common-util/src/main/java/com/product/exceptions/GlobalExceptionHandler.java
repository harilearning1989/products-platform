package com.product.exceptions;

import com.product.configs.ReadErrorProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private ReadErrorProperties readErrorProperties;

    @ExceptionHandler(NoStockAvailableException.class)
    public ProblemDetail handleNoStockAvailableException(
            NoStockAvailableException ex,
            HttpServletRequest request) {
        logger.info("Entered NoStockAvailableException");

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());

        problemDetail.setTitle("Stock Not Available");
        problemDetail.setType(URI.create("https://api.inventory.com/errors/no-stock"));
        problemDetail.setType(URI.create(readErrorProperties.getNoStock()));
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("method", request.getMethod());
        problemDetail.setProperty("productId", ex.getProductId());

        return problemDetail;
    }

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

    @ExceptionHandler(ErrorResponseException.class)
    public ProblemDetail handleErrorResponse(ErrorResponseException ex) {
        ProblemDetail problem = ex.getBody();

        if (problem.getStatus() == 404) {
            problem.setTitle("Order API Not Found");
            problem.setDetail("The requested endpoint does not exist in Order Service");
            problem.setType(URI.create("https://api.orderservice.com/errors/not-found"));
        }

        return problem;
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ProblemDetail inventoryNotFoundException(DuplicateResourceException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("Duplicate Resource");
        problem.setDetail("Product SKU already exists");
        problem.setInstance(URI.create("/products"));

        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception ex, HttpServletRequest request) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://api.inventory.com/errors/internal-error"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("method", request.getMethod());

        return problemDetail;
    }

    /*@ExceptionHandler(NoStockAvailableException.class)
    public ProblemDetail handleNoStockAvailableException(
            NoStockAvailableException ex,
            HttpServletRequest request) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());

        problemDetail.setTitle("Stock Not Available");
        problemDetail.setType(URI.create(ErrorConstants.NO_STOCK));
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        problemDetail.setProperty("method", request.getMethod());
        problemDetail.setProperty("productId", ex.getProductId());

        return problemDetail;
    }*/

/*
    @ExceptionHandler(NoHandlerFoundException.class)
    public ProblemDetail handleNotFound(NoHandlerFoundException ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        "The requested endpoint does not exist in Order Service"
                );

        problemDetail.setTitle("Order API Not Found");
        problemDetail.setType(URI.create("https://api.orderservice.com/errors/not-found"));
        problemDetail.setProperty("path", ex.getRequestURL());

        return problemDetail;
    }*/

}

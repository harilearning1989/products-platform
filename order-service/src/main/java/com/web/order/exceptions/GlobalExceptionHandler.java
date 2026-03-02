package com.web.order.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResource(NoResourceFoundException ex) {
        ProblemDetail problem = ex.getBody();

        problem.setTitle("Order API Not Found");
        problem.setDetail("The requested endpoint does not exist in Order Service");
        problem.setType(URI.create("https://api.orderservice.com/errors/not-found"));

        return problem;
    }
}

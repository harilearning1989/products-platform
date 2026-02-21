package com.web.product.exceptions;

import org.springframework.http.ProblemDetail;

public class JwtProblemException extends RuntimeException {

    private final ProblemDetail problemDetail;

    public JwtProblemException(ProblemDetail problemDetail) {
        super(problemDetail.getDetail());
        this.problemDetail = problemDetail;
    }

    public ProblemDetail getProblemDetail() {
        return problemDetail;
    }
}

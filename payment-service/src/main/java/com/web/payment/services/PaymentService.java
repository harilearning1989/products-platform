package com.web.payment.services;

import com.web.payment.dtos.PaymentRequest;
import com.web.payment.dtos.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
}

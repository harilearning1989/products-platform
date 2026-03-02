package com.web.order.client;

import com.web.order.dtos.PaymentResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/payments")
public interface PaymentClient {

    @GetExchange("/{id}")
    PaymentResponse getPaymentByOrderId(@PathVariable Long id);
}

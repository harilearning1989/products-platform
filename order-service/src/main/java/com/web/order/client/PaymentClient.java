package com.web.order.client;

import com.web.order.dtos.PaymentResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/payments")
public interface PaymentClient {

    @GetExchange("/{id}")
    PaymentResponse getPaymentByOrderId(@PathVariable Long id);

    @PostExchange("/bulk")
    List<PaymentResponse> fetchAllPayments(@RequestBody List<Long> orderIds);
}

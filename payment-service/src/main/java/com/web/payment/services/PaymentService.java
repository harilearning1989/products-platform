package com.web.payment.services;

import com.web.payment.dtos.PaymentResponse;

import java.util.List;

public interface PaymentService {
    void processInventoryReserved(String json);

    PaymentResponse getPaymentByOrderId(Long orderId);

    List<PaymentResponse> getPayments(List<Long> orderIds);
}

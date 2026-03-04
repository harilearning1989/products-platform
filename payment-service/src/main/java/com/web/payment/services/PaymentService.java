package com.web.payment.services;

import com.web.payment.dtos.PaymentResponse;

public interface PaymentService {
    void processInventoryReserved(String json);

    PaymentResponse getPaymentByOrderId(Long orderId);
}

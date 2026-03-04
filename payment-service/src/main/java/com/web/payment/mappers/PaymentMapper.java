package com.web.payment.mappers;

import com.web.payment.dtos.PaymentResponse;
import com.web.payment.models.Payment;

public interface PaymentMapper {
    PaymentResponse toPaymentResponse(Payment payment);
}

package com.web.payment.mappers;

import com.web.payment.dtos.PaymentResponse;
import com.web.payment.models.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapperImpl implements PaymentMapper {
    @Override
    public PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getCustomerEmail(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getCreatedAt(),
                payment.getUpdatedAt(),
                payment.getFailureReason());
    }

}

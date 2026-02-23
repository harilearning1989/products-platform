package com.web.payment.services;

import com.web.payment.dtos.PaymentRequest;
import com.web.payment.dtos.PaymentResponse;
import com.web.payment.enums.PaymentStatus;
import com.web.payment.models.Payment;
import com.web.payment.producer.PaymentEventProducer;
import com.web.payment.repos.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final PaymentEventProducer producer;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {

        // Idempotency check
        repository.findByOrderId(request.orderId())
                .ifPresent(existing -> {
                    throw new RuntimeException("Payment already processed for order");
                });

        Payment payment = Payment.builder()
                .orderId(request.orderId())
                .customerEmail(request.customerEmail())
                .amount(request.amount())
                .build();

        // Simulate external gateway call
        boolean success = simulateGateway();

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(UUID.randomUUID().toString());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        Payment saved = repository.save(payment);

        // Publish event
        if (saved.getStatus() == PaymentStatus.SUCCESS) {
            producer.publishPaymentSuccess(saved);
        } else {
            producer.publishPaymentFailed(saved);
        }

        return new PaymentResponse(
                saved.getId(),
                saved.getOrderId(),
                saved.getAmount(),
                saved.getStatus().name(),
                saved.getTransactionId(),
                saved.getCreatedAt()
        );
    }

    private boolean simulateGateway() {
        return Math.random() > 0.2; // 80% success rate
    }
}

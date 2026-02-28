package com.web.payment.services;

import com.web.payment.dtos.OrderCreatedEvent;
import com.web.payment.dtos.PaymentProcessedInternalEvent;
import com.web.payment.enums.PaymentStatus;
import com.web.payment.models.Payment;
import com.web.payment.produce.PaymentEventProduce;
import com.web.payment.repos.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final PaymentEventProduce paymentEventProduce;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void processInventoryReserved(String json) {
        log.info("processInventoryReserved json: {}", json);
        OrderCreatedEvent event =
                objectMapper.readValue(json, OrderCreatedEvent.class);

        if (paymentRepository.findByOrderId(event.orderId()).isPresent()) {
            return;
        }

        Payment payment = Payment.builder()
                .orderId(event.orderId())
                .customerEmail(event.customerEmail())
                .amount(event.totalAmount())
                .status(PaymentStatus.INITIATED)
                .build();

        paymentRepository.save(payment);

        boolean success = processPayment(event.totalAmount());

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(UUID.randomUUID().toString());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Payment gateway error");
        }

        // 🔥 Publish INTERNAL event (not Kafka yet)
        eventPublisher.publishEvent(
                new PaymentProcessedInternalEvent(payment, event.items())
        );
    }

    private boolean processPayment(BigDecimal amount) {
        return true;
    }

}

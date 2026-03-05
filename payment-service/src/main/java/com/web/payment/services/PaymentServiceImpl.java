package com.web.payment.services;

import com.web.payment.dtos.OrderCreatedEvent;
import com.web.payment.dtos.PaymentProcessedInternalEvent;
import com.web.payment.dtos.PaymentResponse;
import com.web.payment.enums.PaymentStatus;
import com.web.payment.mappers.PaymentMapper;
import com.web.payment.models.Payment;
import com.web.payment.repos.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final PaymentMapper paymentMapper;

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
                .userId(event.userId())
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

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return paymentMapper.toPaymentResponse(payment);
    }

    @Override
    public List<PaymentResponse> getPayments(List<Long> orderIds) {
        return paymentRepository.findAllByOrderIdIn(orderIds)
                .stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }


    private boolean processPayment(BigDecimal amount) {
        return true;
    }

}

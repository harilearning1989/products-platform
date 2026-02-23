package com.web.history.consumer;

import com.web.history.dtos.PaymentEvent;
import com.web.history.models.PaymentHistory;
import com.web.history.repos.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final PaymentHistoryRepository repository;

    @KafkaListener(topics = "payment-success", groupId = "payment-history-group")
    public void consumeSuccess(PaymentEvent event) {
        save(event);
    }

    @KafkaListener(topics = "payment-failed", groupId = "payment-history-group")
    public void consumeFailed(PaymentEvent event) {
        save(event);
    }

    private void save(PaymentEvent event) {
        PaymentHistory history = PaymentHistory.builder()
                .paymentId(event.paymentId())
                .orderId(event.orderId())
                .customerEmail(event.customerEmail())
                .amount(event.amount())
                .status(event.status())
                .transactionId(event.transactionId())
                .build();

        repository.save(history);
    }
}

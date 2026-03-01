package com.web.history.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.history.dtos.PaymentFailedEvent;
import com.web.history.dtos.PaymentSuccessEvent;
import com.web.history.models.PaymentHistory;
import com.web.history.repos.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final PaymentHistoryRepository repository;
    private final ObjectMapper objectMapper;

    /*
     * 1️⃣ Payment Success
     */
    @KafkaListener(
            topics = "payment-success",
            groupId = "payment-history-group"
    )
    public void handlePaymentSuccess(String json) throws Exception {
        PaymentSuccessEvent event =
                objectMapper.readValue(
                        json,
                        PaymentSuccessEvent.class);

        PaymentHistory history = PaymentHistory.builder()
                .orderId(event.orderId())
                .transactionId(event.transactionId())
                .amount(event.amount())
                .status("SUCCESS")
                .build();

        repository.save(history);
    }

    /*
     * 2️⃣ Payment Failed
     */
    @KafkaListener(
            topics = "payment-failed",
            groupId = "payment-history-group"
    )
    public void handlePaymentFailed(String json) throws Exception {
        PaymentFailedEvent event =
                objectMapper.readValue(
                        json,
                        PaymentFailedEvent.class);

        PaymentHistory history = PaymentHistory.builder()
                .orderId(event.orderId())
                .amount(event.amount())
                .status("FAILED")
                .failureReason(event.failureReason())
                .build();

        repository.save(history);
    }
}

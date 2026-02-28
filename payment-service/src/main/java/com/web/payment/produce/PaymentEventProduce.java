package com.web.payment.produce;

import com.web.payment.dtos.PaymentFailedEvent;
import com.web.payment.dtos.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class PaymentEventProduce {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendPaymentSuccessEvent(String orderId, PaymentSuccessEvent paymentSuccessEvent) {
        String paymentEventJson = objectMapper.writeValueAsString(paymentSuccessEvent);
        kafkaTemplate.send(
                "payment-success",
                orderId,
                paymentEventJson
        );
    }

    public void sendPaymentFailureEvent(String orderId, PaymentFailedEvent paymentFailedEvent) {
        String paymentEventJson = objectMapper.writeValueAsString(paymentFailedEvent);
        kafkaTemplate.send(
                "payment-failed",
                orderId,
                paymentEventJson
        );
    }

}

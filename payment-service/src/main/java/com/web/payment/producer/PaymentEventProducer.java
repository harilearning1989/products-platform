package com.web.payment.producer;

import com.web.payment.models.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentSuccess(Payment payment) {
        kafkaTemplate.send("payment-success", payment);
    }

    public void publishPaymentFailed(Payment payment) {
        kafkaTemplate.send("payment-failed", payment);
    }
}

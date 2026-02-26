package com.web.payment.consumer;

import com.web.payment.dtos.PaymentEvent;
import com.web.payment.dtos.PaymentRequest;
import com.web.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "inventory-reserved", groupId = "payment-group")
    public void process(PaymentEvent paymentEvent) {
        PaymentRequest paymentRequest = new PaymentRequest(paymentEvent.orderId(),
                paymentEvent.userId(), paymentEvent.productId(), paymentEvent.quantity(), paymentEvent.amount());
        paymentService.processPayment(paymentRequest);
    }
}

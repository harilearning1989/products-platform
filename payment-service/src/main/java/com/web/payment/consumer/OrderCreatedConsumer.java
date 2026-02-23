package com.web.payment.consumer;

import com.web.payment.dtos.OrderCreatedEvent;
import com.web.payment.dtos.PaymentRequest;
import com.web.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void consume(OrderCreatedEvent event) {

        paymentService.processPayment(
                new PaymentRequest(
                        event.orderId(),
                        event.customerEmail(),
                        event.amount()
                )
        );
    }
}

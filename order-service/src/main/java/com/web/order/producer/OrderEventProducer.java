package com.web.order.producer;

import com.web.order.dtos.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCreated(Long orderId,
                                    String email,
                                    String amount) {

        var event = new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                orderId,
                email,
                amount
        );

        kafkaTemplate.send("order-created", event);
    }
}

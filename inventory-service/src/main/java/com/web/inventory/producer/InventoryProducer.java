package com.web.inventory.producer;

import com.web.inventory.dtos.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class InventoryProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishInventoryReserved(OrderCreatedEvent orderCreatedEvent) {
        String orderCreatedEventJson = objectMapper.writeValueAsString(orderCreatedEvent);

        kafkaTemplate.send(
                "inventory-reserved",
                orderCreatedEvent.orderId().toString(),
                orderCreatedEventJson
        );
    }

    public void publishInventoryFailed(OrderCreatedEvent orderCreatedEvent) {
        String orderCreatedEventJson = objectMapper.writeValueAsString(orderCreatedEvent);
        kafkaTemplate.send(
                "inventory-failed",
                orderCreatedEvent.orderId().toString(),
                orderCreatedEventJson
        );
    }

}

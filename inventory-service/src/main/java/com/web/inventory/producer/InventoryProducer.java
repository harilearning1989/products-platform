package com.web.inventory.producer;

import com.web.inventory.dtos.InventoryStatusEvent;
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

    public void publishInventoryReserved(OrderCreatedEvent event) {
        InventoryStatusEvent inventoryStatusEvent =
                new InventoryStatusEvent(
                        event.orderId()
                );
        String inventoryStatusEventJson = objectMapper.writeValueAsString(inventoryStatusEvent);

        kafkaTemplate.send(
                "inventory-reserved",
                event.orderId().toString(),
                inventoryStatusEventJson
        );
    }

    public void publishInventoryFailed(OrderCreatedEvent event) {
        InventoryStatusEvent failedEvent =
                new InventoryStatusEvent(
                        event.orderId()
                );

        String inventoryStatusEventJson = objectMapper.writeValueAsString(failedEvent);
        kafkaTemplate.send(
                "inventory-failed",
                event.orderId().toString(),
                inventoryStatusEventJson
        );
    }

}

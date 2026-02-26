package com.web.inventory.producer;

import com.web.inventory.dtos.InventoryStatusEvent;
import com.web.inventory.dtos.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishInventoryReserved(OrderCreatedEvent event) {
        InventoryStatusEvent reservedEvent =
                new InventoryStatusEvent(
                        event.orderId()
                );

        kafkaTemplate.send(
                "inventory-reserved",
                event.orderId().toString(),
                reservedEvent
        );
    }

    public void publishInventoryFailed(OrderCreatedEvent event) {
        InventoryStatusEvent failedEvent =
                new InventoryStatusEvent(
                        event.orderId()
                );

        kafkaTemplate.send(
                "inventory-failed",
                event.orderId().toString(),
                failedEvent
        );
    }
    
}

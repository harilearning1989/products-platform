package com.web.inventory.listener;

import com.web.inventory.dtos.OrderCreatedEvent;
import com.web.inventory.producer.InventoryProducer;
import com.web.inventory.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final InventoryService inventoryService;
    private final InventoryProducer inventoryProducer;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-created")
    public void reserve(String orderCreatedEventJson) {
        System.out.println("Order Created Event: " + orderCreatedEventJson);
        OrderCreatedEvent inventoryStatusEvent = objectMapper.readValue(orderCreatedEventJson, OrderCreatedEvent.class);
        boolean reserveStock = inventoryService.reserveStock(inventoryStatusEvent.items());

        if (reserveStock) {
            inventoryProducer.publishInventoryReserved(inventoryStatusEvent);
            //inventoryProducer.publishPayment(event);
        } else {
            inventoryProducer.publishInventoryFailed(inventoryStatusEvent);
        }
    }
}

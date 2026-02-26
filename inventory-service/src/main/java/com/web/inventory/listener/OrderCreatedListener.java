package com.web.inventory.listener;

import com.web.inventory.dtos.OrderCreatedEvent;
import com.web.inventory.models.Inventory;
import com.web.inventory.producer.InventoryProducer;
import com.web.inventory.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final InventoryService inventoryService;
    private final InventoryProducer inventoryProducer;

    @KafkaListener(topics = "order-created")
    public void reserve(OrderCreatedEvent event) {
        boolean reserveStock = inventoryService.reserveStock(event.items());

        if (reserveStock) {
            inventoryProducer.publishInventoryReserved(event);
            //inventoryProducer.publishPayment(event);
        } else {
            inventoryProducer.publishInventoryFailed(event);
        }
    }
}

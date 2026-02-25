package com.web.inventory.consumer;

import com.web.inventory.dtos.ProductCreateDto;
import com.web.inventory.models.Inventory;
import com.web.inventory.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCreatedConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "product-created-topic",
            groupId = "product-group"
    )
    public void consume(
            ProductCreateDto event
    ) {
        /*
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset

        System.out.println("📦 Received Product Event:");
        System.out.println("Topic: " + topic);
        System.out.println("Partition: " + partition);
        System.out.println("Offset: " + offset);
        System.out.println("Event Data: " + event);
         */

        // Business logic here
        System.out.println("Received event: " + event);
        //processProduct(event);
    }
/*
    public void processProduct(ProductCreatedEvent event) {
        // Example business logic
        System.out.println("Processing product: " + event.productName());
        if (inventoryService.findByProductId(event.productId())) {
            return;
        }

        Inventory inventory = Inventory.builder()
                .productId(event.productId())
                .productName(event.productName())
                .availableQuantity(0)
                .reservedQuantity(0)
                .build();

        inventoryService.saveInventory(inventory);
    }*/
}

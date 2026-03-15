package com.web.inventory.listener;

import com.product.topics.KafkaTopicConstants;
import com.web.inventory.dtos.ProductCreateDto;
import com.web.inventory.models.Inventory;
import com.web.inventory.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class ProductCreatedListener {

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = KafkaTopicConstants.PRODUCT_CREATED_TOPIC,
            groupId = "product-group"
    )
    public void consume(
            String productCreateJson,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {
        ProductCreateDto event = objectMapper.readValue(productCreateJson, ProductCreateDto.class);

        System.out.println("📦 Product Event | Topic: %s | Partition: %s | Offset: %s | Product Name: %s | Product Id: %s"
                        .formatted(topic, partition, offset, event.productName(), event.productId())
        );

        // Business logic here
        System.out.println("Product Name : %s and Product Id : %s".formatted(event.productName(), event.productId()));
        processProduct(event);
    }

    public void processProduct(ProductCreateDto event) {
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
    }
}

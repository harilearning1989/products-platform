package com.web.inventory.consumer;

import com.web.inventory.dtos.ProductCreatedEvent;
import com.web.inventory.models.Inventory;
import com.web.inventory.repos.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductEventConsumer {

    private final InventoryRepository repository;

    @KafkaListener(topics = "product-created", groupId = "inventory-group")
    @Transactional
    public void handleProductCreated(ProductCreatedEvent event) {

        if (repository.findByProductId(event.productId()).isPresent()) {
            return;
        }

        Inventory inventory = Inventory.builder()
                .productId(event.productId())
                .availableQuantity(0)
                .reservedQuantity(0)
                .build();

        repository.save(inventory);
    }
}

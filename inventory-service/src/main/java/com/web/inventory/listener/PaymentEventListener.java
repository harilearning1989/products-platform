package com.web.inventory.listener;

import com.web.inventory.dtos.OrderItemEvent;
import com.web.inventory.dtos.PaymentFailedEvent;
import com.web.inventory.models.Inventory;
import com.web.inventory.repos.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-failed")
    @Transactional
    public void handlePaymentFailed(String paymentFailedEventJson) {
        System.out.println("Payment service OrderInventoryListener handlePaymentFailed:: " + paymentFailedEventJson);
        PaymentFailedEvent paymentEvent = objectMapper.readValue(paymentFailedEventJson, PaymentFailedEvent.class);
        for (OrderItemEvent item : paymentEvent.items()) {
            Inventory inventory = inventoryRepository.findById(item.productId()).get();

            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity() + item.quantity()
            );
        }
    }
}

package com.web.payment.listener;

import com.web.payment.produce.PaymentEventProduce;
import com.web.payment.repos.PaymentRepository;
import com.web.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class InventoryReservedListener {

    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final PaymentEventProduce paymentEventProduce;
    private final PaymentService paymentService;


    @KafkaListener(
            topics = "inventory-reserved",
            groupId = "payment-group"
    )
    public void handleInventoryReserved(String inventoryReservedEventJson) {
        paymentService.processInventoryReserved(inventoryReservedEventJson);
    }

}

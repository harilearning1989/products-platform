package com.web.order.producer;

import com.web.order.dtos.OrderCreatedDomainEvent;
import com.web.order.dtos.OrderCreatedEvent;
import com.web.order.dtos.OrderItemEvent;
import com.web.order.models.OrderProduct;
import com.web.order.repos.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderKafkaPublisher {

    private final OrderProductRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(OrderCreatedDomainEvent domainEvent) {
        OrderProduct order =
                orderRepository.findById(domainEvent.orderId())
                        .orElseThrow();

        List<OrderItemEvent> items =
                order.getItems().stream()
                        .map(i -> new OrderItemEvent(
                                i.getProductId(),
                                i.getQuantity()))
                        .toList();

        OrderCreatedEvent orderCreatedEvent =
                new OrderCreatedEvent(
                        UUID.randomUUID().toString(),
                        order.getId(),
                        order.getCustomerEmail(),
                        order.getTotalAmount(),
                        items,
                        Instant.now()
                );
        String orderCreatedEventJson = objectMapper.writeValueAsString(orderCreatedEvent);

        kafkaTemplate.send("order-created", orderCreatedEventJson);
    }
}

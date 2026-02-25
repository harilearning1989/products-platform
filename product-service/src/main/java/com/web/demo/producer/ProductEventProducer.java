package com.web.demo.producer;

import com.web.demo.dtos.ProductCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductEventProducer {

    private final KafkaTemplate<String, ProductCreateDto> kafkaTemplate;

    private static final String TOPIC = "product-created-topic";

    public void publishProductCreated(Long productId, String name) {
        /*ProductCreatedEvent event =
                new ProductCreatedEvent(productId, name);*/
        ProductCreateDto productCreateDto = new ProductCreateDto(productId,name);
        kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(),productCreateDto);
    }
}

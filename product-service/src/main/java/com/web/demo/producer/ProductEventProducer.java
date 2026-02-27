package com.web.demo.producer;

import com.web.demo.dtos.ProductCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "product-created-topic";

    public void publishProductCreated(Long productId, String name) {
        /*ProductCreatedEvent event =
                new ProductCreatedEvent(productId, name);*/
        ProductCreateDto productCreateDto = new ProductCreateDto(productId,name);
        String productCreateJson = objectMapper.writeValueAsString(productCreateDto);

        kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(),productCreateJson);
    }
}

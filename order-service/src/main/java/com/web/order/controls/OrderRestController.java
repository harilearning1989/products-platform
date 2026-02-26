package com.web.order.controls;

import com.web.order.dtos.CreateOrderRequest;
import com.web.order.dtos.OrderResponse;
import com.web.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createNewOrder(
            @Validated @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createNewOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

}

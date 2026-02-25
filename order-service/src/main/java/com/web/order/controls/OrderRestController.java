package com.web.order.controls;

import com.web.order.dtos.OrderItemRequest;
import com.web.order.dtos.OrderItemResponse;
import com.web.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<OrderItemResponse> createNewOrder(@RequestBody OrderItemRequest request) {
        OrderItemResponse order = orderService.createNewOrder(request);
        return ResponseEntity.ok(order);
    }

}

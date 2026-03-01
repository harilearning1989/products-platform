package com.web.order.controls;

import com.web.order.dtos.CreateOrderRequest;
import com.web.order.dtos.OrderResponse;
import com.web.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;
    private static int counter = 0;

    @PostMapping
    public ResponseEntity<OrderResponse> createNewOrder(
            @Validated @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createNewOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/test-retry")
    public ResponseEntity<String> testRetry() {
        counter++;
        System.out.println("testRetry Attempt: " + counter);

        if (counter < 3) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Temporary failure");
        }
        return ResponseEntity.ok("Success on attempt " + counter);
    }

    @GetMapping("/slow")
    public ResponseEntity<String> slowResponse() throws InterruptedException {
        System.out.println("slowResponse Attempt: " + counter);
        TimeUnit.SECONDS.sleep(6);
        return ResponseEntity.ok("Response after 10 seconds");
    }

}

package com.web.order.controls;

import com.product.dtos.CreateOrderRequest;
import com.product.dtos.OrderDetailsResponse;
import com.product.dtos.OrderResponse;
import com.product.dtos.User;
import com.product.util.JsonUtil;
import com.web.order.services.MyService;
import com.web.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(OrderRestController.class);

    private final OrderService orderService;
    private static int counter = 0;
    private final MyService myService;

    @PostMapping
    public ResponseEntity<OrderResponse> createNewOrder(
            @Validated @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createNewOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<OrderDetailsResponse>> getAllOrdersFull() {
        logger.info("Orders Fetched successfully");
        List<OrderDetailsResponse> response =
                orderService.getAllOrderDetails();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allOrders/{status}")
    public ResponseEntity<List<OrderDetailsResponse>> getAllOrdersByStatus(
            @PathVariable(value = "status") String orderStatus) {
        List<OrderDetailsResponse> response = orderService.getAllOrderDetails(orderStatus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allOrders/user/{userId}")
    public ResponseEntity<List<OrderDetailsResponse>> getAllOrdersByUserId(
            @PathVariable(value = "userId") Long userId) {
        List<OrderDetailsResponse> response = orderService.getAllOrdersByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public OrderDetailsResponse getOrderDetails(
            @PathVariable Long id) {
        myService.test();
        return orderService.getOrderDetails(id);
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

    @GetMapping("/common")
    public String helloWorld() {
        User userDto = new User("Haridu", 18);
        String jsonData = JsonUtil.toJson(userDto);
        System.out.println(jsonData);
        userDto = JsonUtil.fromJson(jsonData, User.class);
        System.out.println(userDto);

        String prettyJson = JsonUtil.toPrettyJson(userDto);
        System.out.println(prettyJson);

        return "Hello World";
    }

}

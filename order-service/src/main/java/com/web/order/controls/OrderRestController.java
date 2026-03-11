package com.web.order.controls;

import com.product.dtos.CreateOrderRequest;
import com.product.dtos.OrderDetailsResponse;
import com.product.dtos.OrderResponse;
import com.product.dtos.User;
import com.product.util.JsonUtil;
import com.web.order.services.MyService;
import com.web.order.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Tag(name = "Order APIs", description = "Operations related to Order management")
public class OrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(OrderRestController.class);

    private final OrderService orderService;
    private final MyService myService;

    private static int counter = 0;

    @Operation(
            summary = "Create a new order",
            description = "Creates a new order based on the provided order request."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createNewOrder(
            @Validated @RequestBody CreateOrderRequest request) {

        logger.info("Received request to create new order: {}", request);

        OrderResponse response = orderService.createNewOrder(request);

        logger.info("Order created successfully with id: {}", response.orderId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Fetch all orders",
            description = "Retrieve complete details of all orders."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDetailsResponse.class)))
    })
    @GetMapping("/allOrders")
    public ResponseEntity<List<OrderDetailsResponse>> getAllOrdersFull() {

        logger.info("Fetching all order details");

        List<OrderDetailsResponse> response = orderService.getAllOrderDetails();

        logger.info("Total orders fetched: {}", response.size());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Fetch orders by status",
            description = "Retrieve orders filtered by order status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders fetched successfully")
    })
    @GetMapping("/allOrders/{status}")
    public ResponseEntity<List<OrderDetailsResponse>> getAllOrdersByStatus(
            @PathVariable("status") String orderStatus) {

        logger.info("Fetching orders with status: {}", orderStatus);

        List<OrderDetailsResponse> response = orderService.getAllOrderDetails(orderStatus);

        logger.info("Orders found with status {} : {}", orderStatus, response.size());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Fetch orders by userId",
            description = "Retrieve all orders placed by a specific user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders fetched successfully")
    })
    @GetMapping("/allOrders/user/{userId}")
    public ResponseEntity<List<OrderDetailsResponse>> getAllOrdersByUserId(
            @PathVariable Long userId) {

        logger.info("Fetching orders for userId: {}", userId);

        List<OrderDetailsResponse> response = orderService.getAllOrdersByUserId(userId);

        logger.info("Total orders fetched for user {} : {}", userId, response.size());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get order details",
            description = "Retrieve full details of an order by order ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order details fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDetailsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public OrderDetailsResponse getOrderDetails(@PathVariable Long id) {

        logger.info("Fetching order details for id: {}", id);

        myService.test();

        OrderDetailsResponse response = orderService.getOrderDetails(id);

        logger.info("Order details retrieved successfully for id: {}", id);

        return response;
    }

    @Operation(
            summary = "Test retry endpoint",
            description = "Simulates failures for first 2 attempts to test retry mechanisms."
    )
    @GetMapping("/test-retry")
    public ResponseEntity<String> testRetry() {

        counter++;

        logger.warn("testRetry attempt number: {}", counter);

        if (counter < 3) {
            logger.error("Simulated failure for retry testing");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Temporary failure");
        }

        logger.info("Retry test succeeded on attempt {}", counter);

        return ResponseEntity.ok("Success on attempt " + counter);
    }

    @Operation(
            summary = "Slow response endpoint",
            description = "Simulates slow response to test timeout handling."
    )
    @GetMapping("/slow")
    public ResponseEntity<String> slowResponse() throws InterruptedException {

        logger.warn("Slow endpoint triggered, simulating delay");

        TimeUnit.SECONDS.sleep(6);

        logger.info("Slow response returned successfully");

        return ResponseEntity.ok("Response after delay");
    }

    @Operation(
            summary = "Common test endpoint",
            description = "Demonstrates JSON serialization and deserialization."
    )
    @GetMapping("/common")
    public String helloWorld() {

        logger.info("Executing JSON utility test");

        User userDto = new User("Haridu", 18);

        String jsonData = JsonUtil.toJson(userDto);
        logger.debug("Serialized JSON: {}", jsonData);

        userDto = JsonUtil.fromJson(jsonData, User.class);
        logger.debug("Deserialized object: {}", userDto);

        String prettyJson = JsonUtil.toPrettyJson(userDto);
        logger.debug("Pretty JSON: {}", prettyJson);

        logger.info("JSON utility execution completed");

        return "Hello World";
    }
}

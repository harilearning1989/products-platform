package com.web.order.services;

import com.product.dtos.*;
import com.product.enums.OrderStatus;
import com.web.order.mappers.OrderMapper;
import com.web.order.models.OrderItem;
import com.web.order.models.OrderProduct;
import com.web.order.repos.OrderProductRepository;
import com.web.order.wrapper.CustomerClientWrapper;
import com.web.order.wrapper.PaymentClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderProductRepository orderProductRepository;
    private final CustomerClientWrapper customerClientWrapper;
    private final PaymentClientWrapper paymentClientWrapper;
    private final ApplicationEventPublisher eventPublisher;
    private final OrderMapper orderMapper;
    private final ProductEnrichmentService productEnrichmentService;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    .withZone(ZoneOffset.UTC);

    @Override
    @Transactional
    public OrderResponse createNewOrder(CreateOrderRequest request) {

        log.info("Creating new order for userId={}, itemsCount={}",
                request.userId(), request.items().size());

        Set<Long> productIds = productEnrichmentService.getProductIds(request.items());

        log.debug("Extracted productIds for order creation: {}", productIds);

        Map<Long, ProductResponse> productMap =
                productEnrichmentService.fetchProductMap(productIds);

        log.debug("Fetched {} products from product service", productMap.size());

        OrderProduct order = OrderProduct.builder()
                .userId(request.userId())
                .customerEmail(request.customerEmail())
                .status(OrderStatus.PENDING)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest item : request.items()) {

            ProductResponse product = productMap.get(item.productId());

            if (product == null || !product.active()) {

                log.error("Invalid product detected. productId={}", item.productId());

                throw new RuntimeException("Invalid product: " + item.productId());
            }

            OrderItem orderItem = orderMapper.buildOrderItem(item, product, order);

            total = total.add(orderItem.getLineTotal());

            order.getItems().add(orderItem);

            log.debug("Added order item productId={}, quantity={}, lineTotal={}",
                    item.productId(), item.quantity(), orderItem.getLineTotal());
        }

        order.setTotalAmount(total);

        log.debug("Calculated total order amount: {}", total);

        order = orderProductRepository.save(order);

        log.info("Order created successfully with id={} and totalAmount={}",
                order.getId(), order.getTotalAmount());

        eventPublisher.publishEvent(
                new OrderCreatedDomainEvent(order.getId()));

        log.info("OrderCreatedDomainEvent published for orderId={}", order.getId());

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailsResponse> getAllOrderDetails(String status) {

        log.info("Fetching orders with status={}", status);

        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());

        List<OrderProduct> orders =
                orderProductRepository.findByStatusWithItems(orderStatus);

        log.info("Fetched {} orders with status={}", orders.size(), status);

        return buildOrderDetailsResponse(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailsResponse> getAllOrdersByUserId(Long userId) {

        log.info("Fetching orders for userId={}", userId);

        List<OrderProduct> orders = orderProductRepository.findByUserId(userId);

        log.info("Found {} orders for userId={}", orders.size(), userId);

        return buildOrderDetailsResponse(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailsResponse> getAllOrderDetails() {

        log.info("Fetching all orders");

        List<OrderProduct> orders = orderProductRepository.findAll();

        log.info("Total orders fetched: {}", orders.size());

        return buildOrderDetailsResponse(orders);
    }

    private List<OrderDetailsResponse> buildOrderDetailsResponse(List<OrderProduct> orders) {

        if (orders.isEmpty()) {
            log.warn("No orders found for requested query");
            return Collections.emptyList();
        }

        log.debug("Building order details response for {} orders", orders.size());

        // Collect IDs
        List<Long> userIds = orders.stream()
                .map(OrderProduct::getUserId)
                .distinct()
                .toList();

        List<Long> orderIds = orders.stream()
                .map(OrderProduct::getId)
                .toList();

        Set<Long> productIds = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .map(OrderItem::getProductId)
                .collect(Collectors.toSet());

        log.debug("Collected userIds={}, orderIds={}, productIds={}",
                userIds.size(), orderIds.size(), productIds.size());

        // Fetch customers
        log.debug("Calling customer service for {} users", userIds.size());

        List<CustomerResponse> allCustomers =
                customerClientWrapper.getCustomersBulk(userIds);

        Map<Long, CustomerResponse> customerMap = allCustomers.stream()
                .collect(Collectors.toMap(CustomerResponse::userId, p -> p));

        log.debug("Customer service returned {} records", customerMap.size());

        // Fetch payments
        log.debug("Calling payment service for {} orders", orderIds.size());

        List<PaymentResponse> allPayments =
                paymentClientWrapper.getPaymentsBulk(orderIds);

        Map<Long, PaymentResponse> paymentMap = allPayments.stream()
                .collect(Collectors.toMap(PaymentResponse::orderId, p -> p));

        log.debug("Payment service returned {} records", paymentMap.size());

        // Fetch products
        Map<Long, ProductResponse> productMap =
                productEnrichmentService.fetchProductMap(productIds);

        log.debug("Product enrichment returned {} products", productMap.size());

        // Build response
        return orders.stream()
                .map(order -> {

                    CustomerResponse customer = customerMap.get(order.getUserId());

                    PaymentResponse payment = paymentMap.getOrDefault(
                            order.getId(),
                            PaymentResponse.empty(order.getId())
                    );

                    List<OrderDetailItemResponse> itemDetails =
                            mapToDetailItems(order.getItems(), productMap);

                    log.debug("Mapped {} items for orderId={}",
                            itemDetails.size(), order.getId());

                    return orderMapper.getOrderDetailsResponse(
                            order,
                            customer,
                            itemDetails,
                            payment
                    );
                })
                .toList();
    }

    @Override
    public OrderDetailsResponse getOrderDetails(Long orderId) {

        log.info("Fetching order details for orderId={}", orderId);

        OrderProduct orderProduct =
                orderProductRepository.findById(orderId)
                        .orElseThrow(() -> {
                            log.error("Order not found for orderId={}", orderId);
                            return new RuntimeException("Order not found");
                        });

        log.debug("Order entity retrieved for orderId={}", orderId);

        CustomerResponse customer =
                customerClientWrapper.getCustomerById(orderProduct.getUserId());

        log.debug("Customer fetched for userId={}", orderProduct.getUserId());

        PaymentResponse payment =
                paymentClientWrapper.getPaymentByOrderId(orderId);

        log.debug("Payment fetched for orderId={}", orderId);

        Set<Long> productIds =
                productEnrichmentService.getProductIdFromEntity(orderProduct.getItems());

        Map<Long, ProductResponse> productMap =
                productEnrichmentService.fetchProductMap(productIds);

        List<OrderDetailItemResponse> itemDetails =
                mapToDetailItems(orderProduct.getItems(), productMap);

        log.info("Order details successfully built for orderId={}", orderId);

        return orderMapper.getOrderDetailsResponse(orderProduct, customer, itemDetails, payment);
    }

    private List<OrderDetailItemResponse> mapToDetailItems(
            List<OrderItem> orderItems,
            Map<Long, ProductResponse> productMap) {

        log.debug("Mapping {} order items to response", orderItems.size());

        return orderItems.stream()
                .map(item -> {

                    ProductResponse product =
                            productMap.get(item.getProductId());

                    if (product == null) {
                        log.warn("Product details missing for productId={}", item.getProductId());
                    }

                    return new OrderDetailItemResponse(
                            item.getProductId(),
                            product != null ? product.name() : item.getProductName(),
                            item.getPrice(),
                            item.getQuantity()
                    );
                })
                .toList();
    }
}

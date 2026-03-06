package com.web.order.repos;

import com.web.order.enums.OrderStatus;
import com.web.order.models.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByStatus(OrderStatus status);
    List<OrderProduct> findByUserId(Long userId);

}

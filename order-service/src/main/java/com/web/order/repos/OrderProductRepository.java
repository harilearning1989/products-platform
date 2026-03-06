package com.web.order.repos;

import com.product.enums.OrderStatus;
import com.web.order.models.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByStatus(OrderStatus status);

    List<OrderProduct> findByUserId(Long userId);

    @Query("""
            SELECT o 
            FROM OrderProduct o
            LEFT JOIN FETCH o.items
            WHERE o.status = :status
            """)
    List<OrderProduct> findByStatusWithItems(@Param("status") OrderStatus status);

}

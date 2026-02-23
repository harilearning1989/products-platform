package com.web.history.repos;

import com.web.history.models.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

    List<PaymentHistory> findByCustomerEmail(String email);

    List<PaymentHistory> findByOrderId(Long orderId);

    List<PaymentHistory> findByStatus(String status);
}

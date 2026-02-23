package com.web.history.services;

import com.web.history.dtos.PaymentHistoryResponse;
import com.web.history.models.PaymentHistory;
import com.web.history.repos.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    private final PaymentHistoryRepository repository;

    @Override
    public List<PaymentHistoryResponse> getByCustomer(String email) {
        return repository.findByCustomerEmail(email)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<PaymentHistoryResponse> getByOrder(Long orderId) {
        return repository.findByOrderId(orderId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<PaymentHistoryResponse> getByStatus(String status) {
        return repository.findByStatus(status)
                .stream()
                .map(this::map)
                .toList();
    }

    private PaymentHistoryResponse map(
            PaymentHistory ph) {

        return new PaymentHistoryResponse(
                ph.getPaymentId(),
                ph.getOrderId(),
                ph.getCustomerEmail(),
                ph.getAmount(),
                ph.getStatus(),
                ph.getTransactionId(),
                ph.getEventTime()
        );
    }
}

package com.web.history.services;

import com.web.history.dtos.PaymentHistoryResponse;

import java.util.List;

public interface PaymentHistoryService {

    List<PaymentHistoryResponse> getByCustomer(String email);

    List<PaymentHistoryResponse> getByOrder(Long orderId);

    List<PaymentHistoryResponse> getByStatus(String status);
}

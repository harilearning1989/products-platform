package com.web.notification.services;

import com.web.notification.dtos.OrderCreatedEvent;

public interface NotificationService {

    void processOrderNotification(OrderCreatedEvent event);
}

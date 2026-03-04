package com.web.order.services;

import com.web.order.config.MessagesProperties;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    private final MessagesProperties messages;

    public MyService(MessagesProperties messages) {
        this.messages = messages;
    }

    public void test() {
        System.out.println(messages.getErrors().getNotFound());
    }
}

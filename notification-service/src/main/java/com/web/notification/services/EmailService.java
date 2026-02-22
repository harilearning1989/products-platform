package com.web.notification.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    public void sendEmail(String to, String subject, String body) {

        // Integrate with:
        // SendGrid / AWS SES / SMTP server

        log.info("Sending email to {} with subject {}", to, subject);

        // Simulate sending
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException("Email sending failed");
        }

        log.info("Email sent successfully to {}", to);
    }
}

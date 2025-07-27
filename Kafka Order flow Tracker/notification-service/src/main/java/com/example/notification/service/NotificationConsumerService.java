package com.example.notification.service;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumerService.class);

    @Value("${kafka.topics.inventory-status}")
    private String inventoryStatusTopic;

    @KafkaListener(topics = "${kafka.topics.inventory-status}", groupId = "notification-group")
    public void consume(ConsumerRecord<String, String> record) {
        String message = record.value();
        Headers headers = record.headers();

        String correlationId = null;

        try {
            if (headers != null) {
                Header header = headers.lastHeader("correlation-id");
                if (header != null) {
                    correlationId = new String(header.value(), StandardCharsets.UTF_8);
                    MDC.put("traceId", correlationId);
                } else {
                    logger.warn("No correlation-id found in header");
                }
            }

            logger.info("Sending notification for inventory status: {}", message);
            // Simulate actual notification logic (e.g., send email or SMS)

        } finally {
            MDC.clear();
        }
    }
}

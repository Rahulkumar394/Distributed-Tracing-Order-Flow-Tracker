package com.example.inventory.service;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topics.inventory-status}")
    private String inventoryStatusTopic;

    @KafkaListener(topics = "${kafka.topics.orders-new}", groupId = "inventory-group")
    public void consume(ConsumerRecord<String, String> record) {
        String orderData = record.value();
        Headers headers = record.headers();

        String correlationId = null;

        try {
            if (headers != null) {
                Header header = headers.lastHeader("correlation-id");
                if (header != null) {
                    correlationId = new String(header.value(), StandardCharsets.UTF_8);
                    MDC.put("traceId", correlationId);
                    logger.debug("Extracted correlation-id from Kafka headers: {}", correlationId);
                } else {
                    logger.warn("No correlation-id found in Kafka headers");
                }
            } else {
                logger.warn("Kafka record has no headers");
            }

            logger.info("Received order message: {}", orderData);

            // Respond with inventory status
            ProducerRecord<String, String> response =
                    new ProducerRecord<>(inventoryStatusTopic, "INVENTORY_OK");

            if (correlationId != null) {
                response.headers().add("correlation-id", correlationId.getBytes(StandardCharsets.UTF_8));
                logger.debug("Attached correlation-id to response: {}", correlationId);
            } else {
                logger.warn("Sending inventory response without correlation-id");
            }

            kafkaTemplate.send(response);
            logger.info("Sent inventory status response to topic: {}", inventoryStatusTopic);

        } catch (Exception e) {
            logger.error("Error processing order message", e);
        } finally {
            MDC.clear(); // Always clean up MDC
        }
    }
}

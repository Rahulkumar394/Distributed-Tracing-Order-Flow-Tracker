package com.order.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topics.orders-new}")
    private String ordersNewTopic;

    public void sendOrder(String orderJson) {
        String traceId = MDC.get("traceId"); // Get traceId from MDC
        logger.info("Sending order to topic={} with traceId={} and payload={}", ordersNewTopic, traceId, orderJson);

        kafkaTemplate.send(ordersNewTopic, orderJson);
    }
}


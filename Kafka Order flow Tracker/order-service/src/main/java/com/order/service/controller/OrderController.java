package com.order.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.service.service.KafkaProducerService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	
	 @Autowired
	 private KafkaProducerService producerService;

	 @PostMapping
	 public ResponseEntity<String> placeOrder(@RequestBody String order) {
	     producerService.sendOrder(order);
	     return ResponseEntity.ok("Order sent to Kafka topic");
	 }

}

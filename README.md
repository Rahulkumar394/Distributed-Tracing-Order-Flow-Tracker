Distributed Tracing — Order Flow Tracker
Project Name: OrderTrackingSystem
Goal:
Trace the entire order lifecycle via Kafka headers with correlation ID.

Tech Stack:
Java 17
Spring Boot
Spring Kafka
Sleuth + OpenTelemetry
Zipkin or Jaeger

How It Works:
OrderService → produces to orders.new
InventoryService → consumes → responds on inventory.status
NotificationService → final stage
Each service logs with trace ID.

Rules to Follow:
Kafka-Specific:

Add correlation ID to headers
Maintain consistent topic naming
Code Quality:

Use interceptors or filters to inject correlation ID
Avoid log pollution; use MDC
Testing:

Unit: Validate headers
Integration: End-to-end trace via Jaeger

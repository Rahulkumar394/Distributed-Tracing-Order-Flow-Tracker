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


==============================================================
                  STEP TO TEST PROJECT
=============================================================

step 
--build java application
mvn clean package -DskipTests

-- Up conatiner using below cmd
docker compose up -d --build

-- check runnings container
docker compose ps


Order Flow Manually Trigger Karna
A. Step 1: Order Place karo
POST call karo /orders endpoint pe:

curl -X POST http://localhost:8081/api/orders \
-H "Content-Type: application/json" \
-d '{"orderId": "123", "productId": "abc", "quantity": 2}'
Expectation: OrderService ek Kafka message produce kare orders.new topic pe, aur Kafka header mein correlation ID (trace ID) hona chahiye.

check Order Log 
check Inventory log 
check notification log


Tracing Verify Karna (Jaeger or Zipkin UI)
Jaeger: Open karo http://localhost:16686
Zipkin: Open karo http://localhost:9411

Steps:
Service dropdown mein OrderService select karo.

Recent trace ya specific trace ID se search karo.

Trace Flow Verify Karo:
OrderService → InventoryService → NotificationService

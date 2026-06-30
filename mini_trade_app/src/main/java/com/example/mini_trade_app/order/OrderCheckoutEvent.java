package com.example.mini_trade_app.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "order_checkout_events",
    indexes = {
        @Index(
            name = "idx_order_checkout_events_event_id",
            columnList = "eventId"
        ),
        @Index(
            name = "idx_order_checkout_events_order_id",
            columnList = "orderId"
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor()
public class OrderCheckoutEvent {
    @Column(unique = true, nullable = false)
    private Long    orderId;
    @Column(unique = true, nullable = false)
    private String  eventId;
}

/*

idempotency key
public class OrderCreatedEvent {
    private String eventId;
    private Long orderId;
}

@Entity
public class ProcessedEvent {
    @Id
    private String eventId;
}

@RabbitListener(queues = "order.created.queue")
public void handle(OrderCreatedEvent event) {

    if (processedEventRepository.existsById(event.getEventId())) {
        return; // already processed
    }

    matchingService.matchOrder(event.getOrderId());

    processedEventRepository.save(
        new ProcessedEvent(event.getEventId())
    );
}

OUTBOX TABLE keep track of events

@Entity
public class OutboxEvent {
    @Id
    private String id;

    private String type;
    private String payload;

    private boolean sent;
}

@Transactional // example
public void createOrder(Order order) {

    orderRepository.save(order);

    OutboxEvent event = new OutboxEvent();
    event.setId(UUID.randomUUID().toString());
    event.setType("ORDER_CREATED");
    event.setPayload("{orderId:" + order.getId() + "}");
    event.setSent(false);

    outboxRepository.save(event);
}

back ground task example

@Scheduled(fixedDelay = 2000)
public void publishOutboxEvents() {

    List<OutboxEvent> events =
            outboxRepository.findBySentFalse();

    for (OutboxEvent event : events) {

        rabbitTemplate.convertAndSend(
                OrderRabbitConstants.EXCHANGE,
                "order.created",
                event.getPayload()
        );

        event.setSent(true);
        outboxRepository.save(event);
    }
}

OrderService
   → DB Order + OutboxEvent

Outbox Scheduler
   → publishes to RabbitMQ

RabbitMQ
   → routes message

Consumer
   → checks idempotency
   → processes safely
   → retries if needed
   → DLQ if broken

*/

/*


rabbitmq

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

@Configuration
public class RabbitConfig {

    public static final String ORDER_QUEUE = "order.created.queue";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ROUTING_KEY = "order.created";

    @Bean
    public Queue queue() {
        return new Queue(ORDER_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }
}

KAFKA

<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>

spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      group-id: order-service
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer

*/
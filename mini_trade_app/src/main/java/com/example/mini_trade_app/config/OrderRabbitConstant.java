package com.example.mini_trade_app.config;

public class OrderRabbitConstant {
    public static final String EXCHANGE = "order.exchange";

    public static final String ROUTING_ORDER_CREATED = "order.created";
    public static final String ROUTING_ORDER_CANCELLED = "order.cancelled";
    public static final String ROUTING_ORDER_PAID = "order.paid";
    public static final String ROUTING_ORDER_CHECKOUT = "order.checkout";

    public static final String QUEUE_ORDER_CREATED = "order.created.queue";
    public static final String QUEUE_ORDER_CANCELLED = "order.cancelled.queue";
    public static final String QUEUE_ORDER_PAID = "order.paid.queue";
    public static final String QUEUE_ORDER_CHECKOUT = "order.checkout.queue"; 
}

/*
@Bean
public Queue orderRetryQueue() {
    return QueueBuilder.durable("order.retry.queue")
            .withArgument("x-message-ttl", 5000) // 5 seconds delay
            .withArgument("x-dead-letter-exchange", OrderRabbitConstants.EXCHANGE)
            .withArgument("x-dead-letter-routing-key", "order.created")
            .build();
}

@Bean
public Queue orderCreatedQueue() {
    return QueueBuilder.durable("order.created.queue")
            .withArgument("x-dead-letter-exchange", "order.dlx")
            .build();
}

*/
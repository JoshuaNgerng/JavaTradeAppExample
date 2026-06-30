package com.example.mini_trade_app.config;

import org.springframework.amqp.core.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // ========================
    // EXCHANGE
    // ========================
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(OrderRabbitConstant.EXCHANGE);
    }

    // ========================
    // QUEUES
    // ========================
    @Bean
    public Queue orderCheckoutQueue() {
        return new Queue(OrderRabbitConstant.QUEUE_ORDER_CHECKOUT);
    }

    // ========================
    // BINDINGS
    // ========================
    @Bean
    public Binding bindOrderCheckout(Queue orderCheckoutQueue, TopicExchange orderExchange) {
        return BindingBuilder
                .bind(orderCheckoutQueue)
                .to(orderExchange)
                .with(OrderRabbitConstant.ROUTING_ORDER_CHECKOUT);
    }
}

/*

for retry with dlq (stopping point)
order.created.queue → order.dlx → order.dlq 

@Bean
public TopicExchange deadLetterExchange() {
    return new TopicExchange("order.dlx");
}

@Bean
public Queue deadLetterQueue() {
    return new Queue("order.dlq");
}

@Bean
public Binding dlqBinding() {
    return BindingBuilder
            .bind(deadLetterQueue())
            .to(deadLetterExchange())
            .with("#");
}
*/
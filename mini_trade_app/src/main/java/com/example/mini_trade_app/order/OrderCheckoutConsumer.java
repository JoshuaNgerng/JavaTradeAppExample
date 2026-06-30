package com.example.mini_trade_app.order;

import com.example.mini_trade_app.config.OrderRabbitConstant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

public class OrderCheckoutConsumer {

@Component
public class OrderCreatedConsumer {

    private final OrderMatchingService matchingService;

    public OrderCreatedConsumer(OrderMatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @RabbitListener(queues = OrderRabbitConstant.QUEUE_ORDER_CREATED)
    public void handle(OrderCheckoutEvent event) {
        matchingService.match(event.getOrderId());
    }
}
}

/*
@RabbitListener(queues = "order.created.queue")
public void handle(OrderCreatedEvent event) {
    try {
        matchingService.matchOrder(event.getOrderId());
    } catch (Exception ex) {
        throw ex; // triggers retry/DLQ
    }
}
*/
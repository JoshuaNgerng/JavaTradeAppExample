package com.example.mini_trade_app.order;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.example.mini_trade_app.config.OrderRabbitConstant;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor()
public class OrderMatchingServiceConsumer {
    private final OrderCheckoutEventRepository eventRepository;
    private final OrderMatchingService service;

    @RabbitListener(queues = OrderRabbitConstant.QUEUE_ORDER_CREATED)
    public void consume(OrderCheckoutEvent event) {
        try {
            eventRepository.save(
                new OrderCheckoutEvent(event.getOrderId(), event.getEventId())
            );
        } catch (DataIntegrityViolationException e) {
            return;
        }

        this.service.match(event.getOrderId());
    }
}

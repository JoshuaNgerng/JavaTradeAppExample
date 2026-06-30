package com.example.mini_trade_app.order;

import java.util.List;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mini_trade_app.order.entity.Order;
import com.example.mini_trade_app.order.entity.OrderItem;
import com.example.mini_trade_app.order.entity.OrderType;
import com.example.mini_trade_app.shared.exception.BusinessException;
import com.example.mini_trade_app.trade.TradeRepository;
import com.example.mini_trade_app.trade.entity.Trade;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class OrderMatchingService {

    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;

    @Retryable(
        value = {
            CannotAcquireLockException.class,
            OptimisticLockingFailureException.class
        }, 
        maxRetries = 3,
        delay = 2000
    )
    public void match(Long Id) {

        Order order = orderRepository.findById(Id).orElseThrow(
            () -> new BusinessException(OrderErrorCode.ORDER_ITEM_NOT_FOUND)
        );

        for (OrderItem item : order.getItems()) {

            if (item.getRemainingQuantity() <= 0) {
                continue;
            }

            matchItem(item);
        }
    }

    private void matchItem(OrderItem incomingItem) {

        List<OrderItem> candidates =
            orderRepository.findMatchingOrders(
                incomingItem.getProduct(),
                incomingItem.getOrder().getType(),
                incomingItem.getRemainingQuantity()
            );

        for (OrderItem candidate : candidates) {

            if (incomingItem.getRemainingQuantity() == 0) {
                break;
            }

            executeTrade(incomingItem, candidate);
        }
    }

    private void executeTrade(OrderItem incoming, OrderItem existing) {

        long tradedQuantity = Math.min(
            incoming.getRemainingQuantity(),
            existing.getRemainingQuantity()
        );

        incoming.removeQuantity(
            incoming.getRemainingQuantity() - tradedQuantity
        );

        existing.removeQuantity(
            existing.getRemainingQuantity() - tradedQuantity
        );

        Trade trade = new Trade(
            tradedQuantity, existing.getPricePerUnit(), 
            existing.getTotalPrice()
        );

        if (incoming.getOrder().getType() == OrderType.BUYER) {
            trade.setBuyerOrder(incoming);
            trade.setSellerOrder(existing);
        } else {
            trade.setBuyerOrder(existing);
            trade.setSellerOrder(incoming);
        }

        tradeRepository.save(trade);

    }

}
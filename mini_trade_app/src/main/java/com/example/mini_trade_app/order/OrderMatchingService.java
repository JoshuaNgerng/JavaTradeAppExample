package com.example.mini_trade_app.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mini_trade_app.order.entity.Order;
import com.example.mini_trade_app.order.entity.OrderItem;
import com.example.mini_trade_app.order.entity.OrderType;
import com.example.mini_trade_app.trade.TradeRepository;
import com.example.mini_trade_app.trade.entity.Trade;

@Service
@Transactional
public class OrderMatchingService {

    private final OrderRepository orderItemRepository;
    private final TradeRepository tradeRepository;

    public OrderMatchingService(
        OrderRepository orderItemRepository,
        TradeRepository tradeRepository
    ) {

        this.orderItemRepository = orderItemRepository;
        this.tradeRepository = tradeRepository;
    }

    public void match(Order order) {

        for (OrderItem item : order.getItems()) {

            if (item.getRemainingQuantity() <= 0) {
                continue;
            }

            matchItem(item);
        }
    }

    private void matchItem(OrderItem incomingItem) {

        List<OrderItem> candidates =
            orderItemRepository.findMatchingOrders(
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
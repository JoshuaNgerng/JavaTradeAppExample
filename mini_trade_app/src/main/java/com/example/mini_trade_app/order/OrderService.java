package com.example.mini_trade_app.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.mini_trade_app.order.dto.CreateOrderForm;
import com.example.mini_trade_app.order.dto.OrderDetailsResponse;
import com.example.mini_trade_app.order.dto.OrderItemData;
import com.example.mini_trade_app.order.dto.OrderListingParams;
import com.example.mini_trade_app.order.dto.OrderListingResponse;
import com.example.mini_trade_app.trade.entity.Trade;
import com.example.mini_trade_app.trade.mapper.TradeMapper;
import com.example.mini_trade_app.trade.TradeRepository;
import com.example.mini_trade_app.user.entity.RoleType;
import com.example.mini_trade_app.user.entity.User;
import com.example.mini_trade_app.order.entity.Order;
import com.example.mini_trade_app.order.entity.OrderType;
import com.example.mini_trade_app.order.mapper.OrderMapper;
import com.example.mini_trade_app.security.AuthUserInfo;
import com.example.mini_trade_app.shared.dto.Listing;
import com.example.mini_trade_app.shared.exception.BusinessException;
import com.example.mini_trade_app.order.entity.OrderStatus;

public class OrderService {
    private final OrderRepository   orderRepo;
    private final TradeRepository   tradeRepo;
    private final OrderMapper       orderMapper;
    private final TradeMapper       tradeMapper;

    public OrderService(
        OrderRepository order, TradeRepository trade,
        OrderMapper mapper, TradeMapper tradeMapper
    ) {
        this.orderRepo = order;
        this.tradeRepo = trade;
        this.orderMapper = mapper;
        this.tradeMapper = tradeMapper;
    }

    public OrderListingResponse getOrderListing(
        OrderListingParams param, AuthUserInfo authUser
    ) {
        User user = authUser.user();
        RoleType activeRole = authUser.role();
        OrderType type = OrderType.fromUserRole(activeRole);
        Pageable pageParam = param.pageParams().getPageable(
            Sort.by("createdAt").descending()
        );
        Page<Order> data;
        if (param.status().isPresent()) {
            data = orderRepo.findByStatusAndTypeAndUserId(
                param.status().get(),
                type, user.getId(), pageParam
            );
        } else {
            data = orderRepo.findByTypeAndUserId(
                type, user.getId(), pageParam
            );
        }
        return new OrderListingResponse(
            Listing.from(data),
            data.getContent().stream().map(
                o -> orderMapper.toData(o)
            ).toList()
        );
    }
    
    public OrderDetailsResponse getOrderDetails(
        long orderId, AuthUserInfo authUser
    ) {
        User user = authUser.user();
        RoleType activeRole = authUser.role();
        Order order = getValidOrder(orderId, user.getId(), activeRole);
        List<Trade> trades;
        if (order.getType() == OrderType.BUYER) {
            trades = tradeRepo.findByBuyerOrderId(order.getId());
        } else {
            trades = tradeRepo.findBySellerOrderId(order.getId());
        }

        // check if userId is correct for this order // check if role is the same
        return new OrderDetailsResponse(
            orderMapper.toCreateOrder(order),
            trades.stream().map(t -> tradeMapper.toData(t)).toList()
        ); 
    }

    public void createOrder(
        CreateOrderForm param, AuthUserInfo authUser
    ) {
        User user = authUser.user();
        Order order = Order.newOrder(user, authUser.role());
        param.items().stream().forEach(i -> order.addItem(i));
        orderRepo.save(order);
    }

    public void checkOutOrder(
        long orderId, AuthUserInfo authUser
    ) {
        User user = authUser.user();
        Order order = getValidOrder(orderId, user.getId(), authUser.role()); 
        order.checkout();
        orderRepo.save(order);
    }

    public void addOrderItem(
        long orderId, OrderItemData param, AuthUserInfo authUser
    ) {
        User user = authUser.user();
        OrderType type = OrderType.fromUserRole(authUser.role());
        Order order = getValidOrder(orderId, user.getId(), type);
        order.addItem(param);
        orderRepo.save(order);
    }

    public void updateOrderItem(
        long orderId, long orderItemId, 
        OrderItemData param, AuthUserInfo authUser
    ) {
        User user = authUser.user();
        OrderType type = OrderType.fromUserRole(authUser.role());
        Order order = getValidOrder(orderId, user.getId(), type);
        order.updateItem(orderItemId, param);
        orderRepo.save(order);
    }

    public void deleteOrderItem(
        long orderId, long orderItemId, AuthUserInfo authUser
    ) {
        User user = authUser.user();
        OrderType type = OrderType.fromUserRole(authUser.role());
        Order order = getValidOrder(orderId, user.getId(), type);
        order.removeItemById(orderItemId);
        orderRepo.save(order);
    }

    public void deleteOrder(
        long orderId, AuthUserInfo authUser
    ) {
        User user = authUser.user();
        Order order = getValidOrder(orderId, user.getId(), authUser.role());
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new BusinessException(OrderErrorCode.ORDER_STATUS_LOCK);
        }
        orderRepo.delete(order);
    }

    private Order getValidOrder(long orderId, long userId, RoleType type) {
        return getValidOrder(orderId, userId, OrderType.fromUserRole(type));
    }

    private Order getValidOrder(long orderId, long userId, OrderType type) {
        Order order = this.orderRepo.findById(orderId).orElseThrow();
        if (order.getType() != type) {
            throw new BusinessException(OrderErrorCode.USER_TYPE_MISMATCH);
        }
        if (order.getUser().getId() != userId) {
            throw new BusinessException(OrderErrorCode.USER_MISMATCH);
        }
        return order;
    }
}

package com.example.mini_trade_app.trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

import com.example.mini_trade_app.trade.entity.Trade;
import com.example.mini_trade_app.user.entity.RoleType;
import com.example.mini_trade_app.user.entity.User;
import com.example.mini_trade_app.order.entity.Order;
import com.example.mini_trade_app.order.entity.OrderItem;

public class TradeSpecifications {
    public static Specification<Trade> hasTradeId(Long tradeId) {
        if (tradeId == null) {
            return null;
        }
        return (root, query, cb) -> {
            return cb.equal(root.get("id"), tradeId);
        };
    }

    public static Specification<Trade> hasUserId(Long userId, RoleType role) {
        return (root, query, cb) -> {

            if (userId == null || role == null) {
                return null; // means "no filter"
            }

            String joinType = getAttributeNameStrByUser(role);

            // Trade → OrderItem
            Join<Trade, OrderItem> orderItemJoin = root.join(joinType);

            // OrderItem → Order
            Join<OrderItem, Order> orderJoin = orderItemJoin.join("order");

            // Order -> User
            Join<Order, User> userJoin = orderJoin.join("user");

            return cb.equal(userJoin.get("id"), userId);
        };
    }

    public static Specification<Trade> quantityGte(Long min) {
        return (root, query, cb) ->
                min == null ? null : cb.greaterThanOrEqualTo(root.get("quantity"), min);
    }

    public static Specification<Trade> quantityLte(Long max) {
        return (root, query, cb) ->
                max == null ? null : cb.lessThanOrEqualTo(root.get("quantity"), max);
    }

    public static Specification<Trade> priceGte(BigDecimal min) {
        return (root, query, cb) ->
                min == null ? null : cb.greaterThanOrEqualTo(root.get("pricePerUnit"), min);
    }

    public static Specification<Trade> priceLte(BigDecimal max) {
        return (root, query, cb) ->
                max == null ? null : cb.lessThanOrEqualTo(root.get("pricePerUnit"), max);
    }

    public static Specification<Trade> createdAfter(LocalDateTime after) {
        return (root, query, cb) ->
                after == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), after);
    }

    public static Specification<Trade> createdBefore(LocalDateTime before) {
        return (root, query, cb) ->
                before == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), before);
    }

    private static String getAttributeNameStrByUser(RoleType role) {
        if (role == RoleType.ROLE_SELLER) {
            return "sellerOrder";
        }
        return "buyerOrder";
    }
}
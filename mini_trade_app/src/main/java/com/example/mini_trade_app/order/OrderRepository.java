package com.example.mini_trade_app.order;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.mini_trade_app.order.entity.*;
import com.example.mini_trade_app.product.Product;

public interface OrderRepository extends JpaRepository<Order, Long>{
    @EntityGraph(attributePaths = "items")
    Page<Order> findByStatusAndUserId(
        OrderStatus status, Long userId, Pageable page
    );
    @EntityGraph(attributePaths = "items")
    Page<Order> findByTypeAndUserId(
        OrderType type, Long userId, Pageable page
    );
    @EntityGraph(attributePaths = "items")
    Page<Order> findByStatusAndTypeAndUserId(
        OrderStatus status, OrderType type, Long userId, Pageable page
    );
    @EntityGraph(attributePaths = "items")
    Optional<Order> findByUserId(Long userId);

    @Query("""
        SELECT oi
        FROM OrderItem oi
        JOIN oi.order o
        WHERE
            oi.product = :product
            AND o.type = :oppositeType
            AND o.status IN ('OPEN', 'PARTIAL')
            AND oi.remainingQuantity > 0
        ORDER BY
            ABS(oi.remainingQuantity - :remainingQty),
            o.createdAt ASC
    """)
    List<OrderItem> findMatchingOrders(
        Product product,
        OrderType oppositeType,
        long remainingQty
    );
}

/*
BUY ORDER

Apple
100 units

    ↓

match(order)

    ↓

loop through items

    ↓

Apple

    ↓

findMatchingOrders()

    ↓

Seller A - 95
Seller B - 80
Seller C - 40

    ↓

executeTrade()

    ↓

update remaining quantities

    ↓

create Trade

    ↓

repeat until buyer has 0 remaining
*/
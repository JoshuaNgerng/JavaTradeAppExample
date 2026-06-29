package com.example.mini_trade_app.order.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.mini_trade_app.model.BaseEntity;
import com.example.mini_trade_app.order.OrderErrorCode;
import com.example.mini_trade_app.product.Product;
import com.example.mini_trade_app.shared.exception.BusinessException;

@Entity
@Table(
    name = "order_items",
    indexes = {
        @Index(name = "idx_order_items_product", columnList = "product"),
        @Index(name = "idx_order_items_status", columnList = "status"),
        @Index(name = "idx_order_items_remaining", columnList = "remainingQuantity")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Product product;
    @Column(nullable = false)
    private long quantity;
    @Column(nullable = false)
    private BigDecimal pricePerUnit;
    @Column(nullable = false)
    private long remainingQuantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(
        Product product, long quantity, BigDecimal price
    ) {
        update(product, quantity, price);
        // this.product = product;
        // this.quantity = quantity;
        // this.pricePerUnit = price
        //     .setScale(2, RoundingMode.HALF_UP);
        // this.remainingQuantity = quantity;
    }

    public void update(
        Product product, long quantity, BigDecimal price
    ) {
        this.product = product;
        this.quantity = quantity;
        this.pricePerUnit = price
            .setScale(2, RoundingMode.HALF_UP);
        this.remainingQuantity = quantity;
    }

    public void reduceQuantity(long taken) {
        if (taken > remainingQuantity) {
            throw new IllegalStateException(
                String.format(
                    "cannot take {}, only {} remaining", 
                    taken, remainingQuantity
                )
            );
        }
    }

    public BigDecimal getTotalPrice() {
        return this.pricePerUnit.multiply(
            BigDecimal.valueOf(this.quantity)
        );
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void removeQuantity(long itemsTaken) {
        if (itemsTaken > remainingQuantity) {
            throw new BusinessException(OrderErrorCode.ORDER_INVALID_STATE);
        }
        remainingQuantity = remainingQuantity - itemsTaken;
        if (remainingQuantity == 0) {
            status = OrderStatus.CLOSED;
        } else if (remainingQuantity < quantity) {
            status = OrderStatus.PARTIAL;
        } else {
            status = OrderStatus.OPEN;
        }
    }
}

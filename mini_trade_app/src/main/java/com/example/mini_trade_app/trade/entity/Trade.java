package com.example.mini_trade_app.trade.entity;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.example.mini_trade_app.model.BaseEntity;
import com.example.mini_trade_app.order.entity.OrderItem;

@Entity
@Table(
    name = "trades",
    indexes = {
        @Index(name = "idx_trades_timestamp", columnList = "executedAt"),
        @Index(name = "idx_trades_buyer", columnList = "buyer_order_id"),
        @Index(name = "idx_trades_seller", columnList = "seller_order_id")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trade extends BaseEntity {

    @Column(nullable = false, updatable = false)
    private long quantity;

    @Column(nullable = false, updatable = false, precision = 19, scale = 4)
    private BigDecimal executionPricePerUnit;

    @Column(nullable = false, updatable = false, precision = 19, scale = 4)
    private BigDecimal totalPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime executedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_order_id", nullable = false, updatable = false)
    private OrderItem buyerOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_order_id", nullable = false, updatable = false)
    private OrderItem sellerOrder;

    public Trade(
        long quantity, BigDecimal executionPricePerUnit, BigDecimal totalPrice
    ) {
        this.quantity = quantity;
        this.executionPricePerUnit = executionPricePerUnit;
        this.totalPrice = totalPrice;
        this.executedAt = LocalDateTime.now();
    }

    public void setBuyerOrder(OrderItem item) { this.buyerOrder=item; }

    public void setSellerOrder(OrderItem item) { this.sellerOrder=item; }
}

package com.example.mini_trade_app.trade;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.mini_trade_app.trade.dto.TradeDetail;
import com.example.mini_trade_app.trade.entity.Trade;

public interface TradeRepository extends JpaRepository<Trade, Long>, JpaSpecificationExecutor<Trade> {
    List<Trade> findByBuyerOrderId(Long buyerOrderId);
    List<Trade> findBySellerOrderId(Long sellerOrderId);
    @Query("""
        SELECT new com.example.mini_trade_app.trade.dto.TradeDetai(
            t.id,
            t.tradePrice,
            t.tradeTime,
            bo.product.name,
            bo.quantity
        )
        FROM Trade t
        JOIN t.buyerOrder bo
        WHERE 
            t.id = :id
            AND
            bo.order.user.id = :userId
    """)
    Optional<TradeDetail> findBuyerTradeDetails(Long id, Long userId);
    @Query("""
        SELECT new com.example.mini_trade_app.trade.dto.TradeDetai(
            t.id,
            t.tradePrice,
            t.tradeTime,
            so.product.name,
            so.quantity
        )
        FROM Trade t
        JOIN t.sellerOrder so
        WHERE 
            t.id = :id
            AND
            so.order.user.id = :userId
    """)
    Optional<TradeDetail> findSellerTradeDetails(Long id, Long userId);
}

/*
example detail query

public interface TradeSummary {

    Long getTradeId();
    BigDecimal getTradePrice();
    LocalDateTime getTradeTime();
    String getProductName();
    Long getQuantity();
}

@Query(value = """
    SELECT
        t.id AS tradeId,
        t.trade_price AS tradePrice,
        t.trade_time AS tradeTime,
        p.name AS productName,
        oi.quantity AS quantity
    FROM trade t
    JOIN order_item oi ON t.buyer_order_id = oi.id
    JOIN product p ON oi.product_id = p.id
    WHERE oi.user_id = :userId
    """,
    nativeQuery = true)
Page<TradeSummary> findTrades(Long userId, Pageable pageable);


*/
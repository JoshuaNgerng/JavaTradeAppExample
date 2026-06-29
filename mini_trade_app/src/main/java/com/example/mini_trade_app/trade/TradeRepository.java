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

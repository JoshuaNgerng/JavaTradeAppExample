package com.example.mini_trade_app.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCheckoutEventRepository extends JpaRepository<OrderCheckoutEvent, Long> {
    public Boolean existsByEventId(String eventId);
}

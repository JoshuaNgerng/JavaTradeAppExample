package com.example.mini_trade_app.order.entity;

import com.example.mini_trade_app.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "order_checkout_event_logs",
    indexes = {
        @Index(
            name = "idx_order_checkout_event_logs_event_id",
            columnList = "eventId"
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderCheckoutEventLog extends BaseEntity {
    @Id
    private String eventId;

}

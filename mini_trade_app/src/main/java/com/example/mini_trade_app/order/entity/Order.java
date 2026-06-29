package com.example.mini_trade_app.order.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.mini_trade_app.model.BaseEntity;
import com.example.mini_trade_app.order.OrderErrorCode;
import com.example.mini_trade_app.order.dto.OrderItemShape;
import com.example.mini_trade_app.shared.exception.BusinessException;
import com.example.mini_trade_app.user.entity.RoleType;
import com.example.mini_trade_app.user.entity.User;

@Entity
@Table(
    name = "orders",
    indexes = {
        @Index(name = "idx_orders_status", columnList = "status"),
        @Index(name = "idx_orders_type", columnList = "type")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    private BigDecimal totalPrice;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(
        mappedBy = "order", 
        cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();


    static public Order newOrder(User owner, RoleType role) {
        Order order = new Order();
        order.status = OrderStatus.DRAFT;
        order.user = owner;
        order.totalPrice = new BigDecimal(0);
        order.type = OrderType.fromUserRole(role);
        return order;
    }

    public void addItem(
        OrderItemShape item_input
    ) {
        BigDecimal price = item_input.pricePerUnit().multiply(
            BigDecimal.valueOf(item_input.quantity())
        );
        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException(
                "Cannot modify order after checkout started"
            );
        }
        OrderItem item = new OrderItem(
            item_input.product(), item_input.quantity(), item_input.pricePerUnit()
        );
        item.setOrder(this);
        items.add(item);
        totalPrice = totalPrice
            .add(price).setScale(2, RoundingMode.HALF_UP);
    }

    public void updateItem(
        long id, OrderItemShape item_input
    ) {
        if (status != OrderStatus.DRAFT) {
            throw new BusinessException(OrderErrorCode.ORDER_STATUS_LOCK);
        }
        OrderItem item = items.stream()
            .filter(i -> i.getId() == id)
            .findFirst().orElseThrow(
                () -> new BusinessException(OrderErrorCode.ORDER_ITEM_NOT_FOUND)
            );
        totalPrice.add(item_input.totalPrice()).subtract(item.getTotalPrice());
        totalPrice.setScale(2, RoundingMode.HALF_UP);
        item.update(
            item_input.product(), item_input.quantity(), 
            item_input.pricePerUnit()
        );
    }

    public OrderItem removeItemById(long id) {
        if (status != OrderStatus.DRAFT) {
            throw new BusinessException(OrderErrorCode.ORDER_STATUS_LOCK);
        }

        OrderItem removed = items
            .stream()
            .filter(item -> item.getId() == id)
            .findFirst().orElseThrow(
                () -> new BusinessException(OrderErrorCode.ORDER_ITEM_NOT_FOUND)
            );

        items.remove(removed);
        totalPrice = totalPrice.subtract(
            removed.getTotalPrice()
        );
        return removed;
    }

    public void checkout() {
        if (items.isEmpty()) {
            throw new BusinessException(OrderErrorCode.ORDER_EMPTY);
        }
        this.status = OrderStatus.OPEN;
    }


}

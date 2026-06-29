package com.example.mini_trade_app.order.mapper;

import org.mapstruct.Mapper;
// import org.mapstruct.Mapping;

import com.example.mini_trade_app.order.dto.OrderItemData;
import com.example.mini_trade_app.order.entity.OrderItem;


@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem   toEntity(OrderItemData data);
}

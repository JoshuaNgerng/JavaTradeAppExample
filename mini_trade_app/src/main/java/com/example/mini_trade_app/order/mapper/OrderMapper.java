package com.example.mini_trade_app.order.mapper;

import org.mapstruct.Mapper;
// import org.mapstruct.Mapping;

import com.example.mini_trade_app.order.dto.OrderData;
import com.example.mini_trade_app.order.dto.CreateOrderResponse;
import com.example.mini_trade_app.order.entity.Order;


@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {
    OrderData   toData(Order order);
    CreateOrderResponse toCreateOrder(Order order);
}

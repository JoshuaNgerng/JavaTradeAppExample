package com.example.mini_trade_app.trade.mapper;

import org.mapstruct.Mapper;
// import org.mapstruct.Mapping;

import com.example.mini_trade_app.trade.dto.TradeData;
import com.example.mini_trade_app.trade.entity.Trade;

@Mapper(componentModel = "spring")
public interface TradeMapper {
        TradeData toData(Trade trade);
        // @Mapping(target = "defaultRole", expression = "java(toRoleType(request.role()))")
        // @Mapping(target = "roles", expression = "java(initRoles(request.role()))")
        // User toEntity(RegisterRequest request);
    
        // UserView toView(User user);
    
}

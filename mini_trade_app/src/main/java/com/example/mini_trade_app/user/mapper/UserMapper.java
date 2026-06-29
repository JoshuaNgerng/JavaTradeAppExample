package com.example.mini_trade_app.user.mapper;

import org.mapstruct.Mapper;

import com.example.mini_trade_app.user.entity.User;
import com.example.mini_trade_app.user.dto.UserView;


@Mapper(componentModel = "spring")
public interface UserMapper {

    // @Mapping(target = "defaultRole", expression = "java(toRoleType(request.role()))")
    // @Mapping(target = "roles", expression = "java(initRoles(request.role()))")
    // User toEntity(RegisterRequest request);

    UserView toView(User user);

    // default RoleType toRoleType(String role) {
    //     return RoleType.valueOf(role.toUpperCase());
    // }

    // default Set<RoleType> initRoles(String role) {
    //     return new HashSet<>(Set.of(RoleType.valueOf(role.toUpperCase())));
    // }
}

package com.example.mini_trade_app.user.entity;

import com.example.mini_trade_app.model.BaseEntity;
import com.example.mini_trade_app.order.entity.Order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_users_email", columnList = "email")
    },
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String saltBase64;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String hashBase64;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType defaultRole;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<RoleType> roles = new HashSet<>();
   
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();
   
    public static User newUser(
        String userName,
        String email,
        String saltBase64,
        String hashBase64,
        String role
    ) {
        RoleType role_ = RoleType.valueOf(role);
        User user = new User();
        user.userName = userName;
        user.email = email;
        user.saltBase64 = saltBase64;
        user.hashBase64 = hashBase64;
        user.roles.add(role_);
        return user;
    }

    public void updatePassword(
        String saltBase64,
        String hashBase64
    ) {
        this.hashBase64 = hashBase64;
        this.saltBase64 = saltBase64;
    }

    public void changeDefaultRole(
        RoleType newRole
    ) {
        if (this.roles.contains(newRole)) {
            this.defaultRole = newRole;
        }
    }

    public void removeRole(RoleType role) {
        if (roles.size() == 1) {
            throw new IllegalStateException("User must have at least one role");
        }

        roles.remove(role);

        // If we removed the default, fix it
        if (role.equals(defaultRole) || !roles.contains(defaultRole)) {
            defaultRole = roles.iterator().next();
        }
    }

    public void addRole(
        RoleType newRole
    ) {
        roles.add(newRole);
    }
}

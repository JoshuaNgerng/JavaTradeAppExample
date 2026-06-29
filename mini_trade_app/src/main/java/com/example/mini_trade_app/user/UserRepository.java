package com.example.mini_trade_app.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mini_trade_app.user.entity.RoleType;
import com.example.mini_trade_app.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User>  findByEmail(String email);
    @Query("""
        SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
        FROM User u
        JOIN u.roles r
        WHERE u.id = :id
        AND r = :role
    """)
    Boolean userHasRole(
        @Param("id") Long id, @Param("role") RoleType role
    );
    boolean existsById(Long id);
}

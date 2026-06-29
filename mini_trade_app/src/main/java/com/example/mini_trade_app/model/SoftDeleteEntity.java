package com.example.mini_trade_app.model;

import java.time.LocalDateTime;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public class SoftDeleteEntity extends BaseEntity {
    private boolean deleted = false;
    private LocalDateTime deletedAt;    

    public void delete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}

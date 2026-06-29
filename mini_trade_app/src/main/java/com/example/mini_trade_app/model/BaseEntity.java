package com.example.mini_trade_app.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * Use a BaseEntity with Jpa Auditing
 * not relying on DB as source of truth
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; 

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt; 

	public boolean isNew() {
		return this.id == null;
	}

    protected void setId(Long id) {
        this.id = id;
    }
}

/*
@Entity
class Order {
    @Embedded AuditInfo audit;
}
composition way to get inheritance like bahviour
*/

/*
Serialization is only relevant when the Java object itself must be transported or stored outside the database 
(session, cache, messaging, etc.).

Summary
Feature	Purpose
Serializable	Converts Java objects to/from byte streams
@Entity	Maps Java objects to database tables
@MappedSuperclass	Shares JPA mappings with subclasses
implements Serializable in BaseEntity	Makes all subclasses serializable
JPA persistence	Uses SQL mapping, not Java serialization
Common reason in entities	Session storage, caching, clustering, remote transfer

In modern Jakarta applications using only JPA and a database, entities often work fine without explicitly implementing Serializable, 
but many teams still keep it on a common BaseEntity for compatibility with frameworks, distributed caches, and session replication.
*/
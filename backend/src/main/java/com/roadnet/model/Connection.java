package com.roadnet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "connections")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String connectionCode;

    @Column(nullable = false)
    private Long userAId;

    @Column(nullable = false)
    private Long userBId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Connection() {
    }

    public Connection(Long userAId, Long userBId) {
        this.userAId = userAId;
        this.userBId = userBId;
        this.connectionCode = generateCode();
    }

    public static String generateCode() {
        int year = LocalDateTime.now().getYear();
        String hex = UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();
        return "RD-" + year + "-" + hex;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (connectionCode == null || connectionCode.isBlank()) {
            connectionCode = generateCode();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConnectionCode() {
        return connectionCode;
    }

    public void setConnectionCode(String connectionCode) {
        this.connectionCode = connectionCode;
    }

    public Long getUserAId() {
        return userAId;
    }

    public void setUserAId(Long userAId) {
        this.userAId = userAId;
    }

    public Long getUserBId() {
        return userBId;
    }

    public void setUserBId(Long userBId) {
        this.userBId = userBId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

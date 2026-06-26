package com.dca.chat.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(name = "channels", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"workspace_id", "name"})
})
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false, updatable = false)
    private Workspace workspace;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Channel() {
    }

    public Channel(String name, Workspace workspace) {
        this.name = name;
        this.workspace = workspace;
    }

    @PrePersist
    private void onCreate() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
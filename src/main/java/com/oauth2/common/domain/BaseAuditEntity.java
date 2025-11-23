package com.oauth2.common.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.OffsetDateTime;

@MappedSuperclass
public abstract class BaseAuditEntity {

    protected OffsetDateTime createdAt;
    protected String createdBy;
    protected OffsetDateTime updatedAt;
    protected String updatedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
        this.createdBy = AuditContext.getCurrentUserId(); // 임시: 나중에 구현
        this.updatedBy = this.createdBy;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
        this.updatedBy = AuditContext.getCurrentUserId();
    }
}

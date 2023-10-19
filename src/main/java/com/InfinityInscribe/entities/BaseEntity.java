package com.InfinityInscribe.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass

public abstract class BaseEntity {

    //public UUID Id;
    protected LocalDateTime CreatedAt;
    @Nullable
    protected LocalDateTime DeletedAt;
    protected Long CreatorId;

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        CreatedAt = createdAt;
    }

    @Nullable
    public LocalDateTime getDeletedAt() {
        return DeletedAt;
    }

    public void setDeletedAt(@Nullable LocalDateTime deletedAt) {
        DeletedAt = deletedAt;
    }

    public Long getCreatorId() {
        return CreatorId;
    }

    public void setCreatorId(Long creatorId) {
        CreatorId = creatorId;
    }
}

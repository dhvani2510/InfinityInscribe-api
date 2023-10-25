package com.InfinityInscribe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "friendship_requests")
public class FriendshipRequest {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public RequestStatus getStatus() {
        return requestStatus;
    }

    public void setStatus(RequestStatus status) {
        this.requestStatus = status;
    }

    public LocalDate getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDate sentAt) {
        this.sentAt = sentAt;
    }

    @Id
    private String id;
    private User sender;
    private User receiver;
    private RequestStatus requestStatus;
    private LocalDate sentAt;

    public FriendshipRequest() {}

    public FriendshipRequest(User sender, User receiver, RequestStatus requestStatus) {
        this.sender = sender;
        this.receiver = receiver;
        this.requestStatus = requestStatus;
        this.sentAt = LocalDate.now();
    }
    // getters and setters
}


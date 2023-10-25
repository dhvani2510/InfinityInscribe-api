package com.InfinityInscribe.models;

import com.InfinityInscribe.entities.FriendshipRequest;
import com.InfinityInscribe.entities.RequestStatus;

public class FriendshipRequestDTO {
    private RequestStatus status;
    private String ID;
    private String senderId;

    public FriendshipRequestDTO build(FriendshipRequest followRequest) {
        FriendshipRequestDTO dto = new FriendshipRequestDTO();
        dto.setID(followRequest.getId());
        dto.setStatus(followRequest.getStatus());
        dto.setSenderId(followRequest.getSender().getId());
        return dto;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setID(String id) {
        this.ID = id;
    }

    public String getID() {
        return ID;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }
}

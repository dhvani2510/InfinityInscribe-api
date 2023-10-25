package com.InfinityInscribe.repositories;

import com.InfinityInscribe.entities.FriendshipRequest;
import com.InfinityInscribe.entities.RequestStatus;
import com.InfinityInscribe.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRequestRepository extends MongoRepository<FriendshipRequest,String> {
    List<FriendshipRequest> findByReceiverAndStatus(User receiver, RequestStatus status);

    @Query("{ 'receiver': ?0, 'status': 'PENDING' }")
    List<FriendshipRequest> findPendingFollowRequestsForReceiver(User receiver);

    boolean existsBySenderAndReceiverAndStatusIn(User sender, User receiver, List<RequestStatus> list);

    FriendshipRequest findBySenderAndReceiverAndRequestStatus(User sender, User receiver, RequestStatus requestStatus);
}

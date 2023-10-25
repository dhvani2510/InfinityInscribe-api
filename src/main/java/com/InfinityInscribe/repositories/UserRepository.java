package com.InfinityInscribe.repositories;

import com.InfinityInscribe.entities.FriendshipRequest;
import com.InfinityInscribe.entities.RequestStatus;
import com.InfinityInscribe.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    long countByUsername(String username);

//    List<FriendshipRequest> findFollowRequestsByReceiverAndStatus(User receiver, RequestStatus status);

    @Query(value = "{ '_id' : ?0, 'followers._id' : ?1 }", count = true)
    boolean isUserFollowing(String userId, String followerId);
}

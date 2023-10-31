package com.InfinityInscribe.services;

import com.InfinityInscribe.entities.FriendshipRequest;
import com.InfinityInscribe.entities.RequestStatus;
import com.InfinityInscribe.entities.User;
import com.InfinityInscribe.models.FriendshipRequestDTO;
import com.InfinityInscribe.repositories.FriendshipRequestRepository;
import com.InfinityInscribe.repositories.UserRepository;
import com.InfinityInscribe.shared.exceptions.InfinityInscribeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FollowService {

    private FriendshipRequestRepository friendshipRequestRepository;
    private UserRepository userRepository;
    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    @Autowired
    public FollowService(FriendshipRequestRepository friendshipRequestRepository, UserRepository userRepository) {
        this.friendshipRequestRepository = friendshipRequestRepository;
        this.userRepository = userRepository;
    }

    public FriendshipRequestDTO sendRequest(String requestedUserId) throws InfinityInscribeException {
        User receiver = userRepository.findById(requestedUserId)
                .orElseThrow(() ->new InfinityInscribeException("Requested user with id not found"));

        logger.info("Getting user profile");
        var auth= SecurityContextHolder.getContext().getAuthentication();
        if(!auth.isAuthenticated()){ // toggle this
            var details= auth.getDetails();
            logger.error("User {} is not authenticated", details);
            throw  new InfinityInscribeException("user is not authenticated");
        }
        var sender= (User)auth.getPrincipal();
        if(sender== null)
            throw  new InfinityInscribeException("User not found");

        FriendshipRequest existingRequest = friendshipRequestRepository.findBySenderAndReceiverAndRequestStatus(sender, receiver, RequestStatus.PENDING);
        if (existingRequest != null) {
            throw new InfinityInscribeException("Request already sent to this user");
        }

        FriendshipRequest followRequest = new FriendshipRequest();
        followRequest.setSender(sender);
        followRequest.setReceiver(receiver);
        followRequest.setStatus(RequestStatus.PENDING);
        followRequest.setSentAt(LocalDate.now());
        friendshipRequestRepository.save(followRequest);
        getPendingFollowRequestsForReceiver(receiver).add(followRequest);
        receiver.getPendingRequests().add(followRequest);
        userRepository.save(receiver);
        return  new FriendshipRequestDTO().build(followRequest);
    }

    public List<FriendshipRequest> getPendingFollowRequestsForReceiver(User receiver) {
        return friendshipRequestRepository.findPendingFollowRequestsForReceiver(receiver);
    }

//    public void acceptFriendshipRequest(String requestId) throws InfinityInscribeException {
//        FriendshipRequest request = friendshipRequestRepository.findById(requestId)
//                .orElseThrow(() -> new InfinityInscribeException("Friendship request not found"));
//        request.setStatus(RequestStatus.ACCEPTED);
//        friendshipRequestRepository.save(request);
//
//        User sender = request.getSender();
//        User receiver = request.getReceiver();
//
//        sender.getFollowers().add(receiver);
//        receiver.getFollowers().add(sender);
//    }

    public void declineFriendshipRequest(String requestId) throws InfinityInscribeException {
        FriendshipRequest request = friendshipRequestRepository.findById(requestId)
                .orElseThrow(() -> new InfinityInscribeException("Friendship request not found"));
        request.setStatus(RequestStatus.DECLINED);
        friendshipRequestRepository.save(request);
    }

    public List<FriendshipRequest> getPendingFriendshipRequests(String receiverId) throws InfinityInscribeException {
        User user = userRepository.findById(receiverId)
                .orElseThrow(()-> new InfinityInscribeException("User not found"));
        return friendshipRequestRepository.findByReceiverAndStatus(user, RequestStatus.PENDING);
    }
}

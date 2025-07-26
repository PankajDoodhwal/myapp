package com.example.myapp.service;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.dto.CreateFriendRequest;
import com.example.myapp.dto.FriendsResponse;
import com.example.myapp.exception.DuplicateException;
import com.example.myapp.model.Friends;
import com.example.myapp.model.User;
import com.example.myapp.repository.FriendRepository;
import com.example.myapp.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    FriendRepository friendRepository;

    @Autowired
    ProjectUtils projectUtils;

    private static final PrettyLogger logger = PrettyLogger.getLogger(FriendService.class);

    public Friends createFriend(CreateFriendRequest createFriendRequest) {

        User user = projectUtils.getUserFromToken();
        String mobile = createFriendRequest.mobileNumber();

        Friends existingFriend = friendRepository.findFriendByUserIsAndMobileNumber(user.getId(), mobile);
        if(existingFriend != null){
            throw new DuplicateException("The Friend " + existingFriend.getFriendName() + " is already present for you!");
        }

        Friends friends = new Friends();
        friends.setFriendName(createFriendRequest.name());
        friends.setMobileNumber(mobile);
        friends.setUser(user);

        return friendRepository.save(friends);
    }

    public List<FriendsResponse> getAllFriends(){
        User user = projectUtils.getUserFromToken();
        if (user == null) {
            return List.of(); // Or handle as an error
        }

        List<Friends> friendsList = friendRepository.findAllFriendsByUserId(user.getId());

        return friendsList.stream()
                .map(friend -> new FriendsResponse(
                        friend.getId(),
                        friend.getFriendName(),
                        friend.getMobileNumber()
                ))
                .collect(Collectors.toList());
    }
}

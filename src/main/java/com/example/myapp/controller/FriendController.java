package com.example.myapp.controller;

import com.example.myapp.common.Entity;
import com.example.myapp.common.MofConstants;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.dto.CreateFriendRequest;
import com.example.myapp.dto.FriendsResponse;
import com.example.myapp.dto.ScopeRequest;
import com.example.myapp.model.Friends;
import com.example.myapp.model.Scope;
import com.example.myapp.response.ApiResponse;
import com.example.myapp.service.FriendService;
import com.example.myapp.service.ScopeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
public class FriendController {
    @Autowired
    FriendService friendService;

    private static final PrettyLogger logger = PrettyLogger.getLogger(FriendController.class);

    @SuppressWarnings("unused")
    @PostMapping("/create")
    public ApiResponse<Friends> createFriend(@Valid @RequestBody CreateFriendRequest createFriendRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Creating a new Friend");
        ctx.put(MofConstants.CREATED_ENTITY_TYPE, Entity.FRIENDS);
        Friends newFriends = friendService.createFriend(createFriendRequest);
        logger.info("Created new friend:- " + newFriends.toString());
        ctx.put(MofConstants.CREATED_ENTITY_ID, newFriends.getId());
        return ApiResponse.success(newFriends, "Friend Created Successfully", ctx.getTraceId());
    }

    @SuppressWarnings("unused")
    @GetMapping("/get-all-friends")
    public ApiResponse<List<FriendsResponse>> getScopeByUser() {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Getting All friends for the user");
        List<FriendsResponse> scopeList = friendService.getAllFriends();
        logger.info("Fetched scopeList");
        return ApiResponse.success(scopeList, "Scope list fetched successfully", ctx.getTraceId());
    }
}

package com.example.myapp.controller;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.model.User;
import com.example.myapp.response.ApiResponse;
import com.example.myapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    private static final PrettyLogger logger = PrettyLogger.getLogger(UserController.class);

    @GetMapping
    public ApiResponse<List<User>> getUser() {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        ctx.setApiName("getUser");

        logger.info("calling getAllUser method");
        List<User> users = userService.getAllUsers();
        return ApiResponse.success(users, "Users fetched successfully", ctx.getTraceId());
    }

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody User user){
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        ctx.setApiName("createUser");
        logger.info("calling createUser method");
        User newUser = userService.createUser(user);
        logger.info("Created new user:- " + newUser.toString());
        logger.info(ApiResponse.success(newUser, "User Created Successfully", ctx.getTraceId()).toString());
        return ApiResponse.success(newUser, "User Created Successfully", ctx.getTraceId());
    }
}

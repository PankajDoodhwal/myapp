package com.example.myapp.controller;

import com.example.myapp.common.Entity;
import com.example.myapp.common.MofConstants;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.dto.LoginRequest;
import com.example.myapp.dto.LoginResponse;
import com.example.myapp.dto.SignupRequest;
import com.example.myapp.model.User;
import com.example.myapp.response.ApiResponse;
import com.example.myapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    private static final PrettyLogger logger = PrettyLogger.getLogger(UserController.class);

    @PostMapping("/signup")
    public ApiResponse<User> createUser(@Valid @RequestBody SignupRequest signupRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        ctx.setApiName("createUser");
        ctx.put(MofConstants.CREATED_ENTITY_TYPE, Entity.USER);
        logger.info("calling createUser method");
        User newUser = userService.createUser(signupRequest);
        logger.info("Created new user:- " + newUser.toString());
        ctx.put(MofConstants.CREATED_ENTITY_ID, newUser.getId());
        return ApiResponse.success(newUser, "User Created Successfully", ctx.getTraceId());
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> LoginUser(@Valid @RequestBody LoginRequest loginRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        ctx.setApiName("signup");
        logger.info("Lets login the user");

        LoginResponse loginResponse = userService.loginUser(loginRequest);

        logger.info("Login to user:- " + loginResponse.toString());
        return ApiResponse.success(loginResponse, "User Loged in Successfully", ctx.getTraceId());
    }

}

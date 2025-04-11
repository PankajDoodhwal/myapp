package com.example.myapp.service;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.controller.UserController;
import com.example.myapp.model.User;
import com.example.myapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final PrettyLogger logger = PrettyLogger.getLogger(UserService.class);


    public List<User> getAllUsers(){
        logger.info("getting all users from the databse");
        return userRepository.findAll();
    }

    public User createUser(User user){
        return userRepository.save(user);
    }
}

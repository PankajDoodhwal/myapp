package com.example.myapp.service;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.dto.LoginRequest;
import com.example.myapp.dto.LoginResponse;
import com.example.myapp.dto.SignupRequest;
import com.example.myapp.exception.DuplicateException;
import com.example.myapp.model.User;
import com.example.myapp.repository.UserRepository;
import com.example.myapp.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    private static final PrettyLogger logger = PrettyLogger.getLogger(UserService.class);

    public List<User> getAllUsers(){
        logger.info("getting all users from the databse");
        return userRepository.findAll();
    }

    public User createUser(SignupRequest signupRequest){
        String email = signupRequest.email();
        Optional<User> existingUser = userRepository.findByEmail(email);
        if(existingUser.isPresent()) {
            throw new DuplicateException(String.format("User with the email address '%s' already exists.", email));
        }
        User user = new User();
        String hashedPassword = passwordEncoder.encode(signupRequest.password());
        user.setName(signupRequest.name());
        user.setEmail(email);
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public LoginResponse loginUser(@Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        String userEmail = jwtUtils.getUserNameFromJwtToken(jwtToken);
        Optional<User> existingUser = userRepository.findByEmail(userEmail);
        if(existingUser.isEmpty()) {
            throw new DuplicateException(String.format("User with the email address '%s' is not present in:- ", userEmail));
        }
        logger.info("UserDetails:- " + existingUser.get());
        return new LoginResponse(existingUser.get(), jwtToken);
    }
}

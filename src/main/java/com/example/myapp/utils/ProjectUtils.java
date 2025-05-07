package com.example.myapp.utils;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.model.User;
import com.example.myapp.repository.UserRepository;
import com.example.myapp.security.services.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class ProjectUtils {
    @Autowired
    private UserRepository userRepository;

    private static final PrettyLogger logger = PrettyLogger.getLogger(ProjectUtils.class);


    public User getUserFromToken() throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userPrincipal.getUser();

            User newUser = userRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            logger.info("User form getUserFromToken:- " + newUser);
            return newUser;
        }
        return null;
    }
}

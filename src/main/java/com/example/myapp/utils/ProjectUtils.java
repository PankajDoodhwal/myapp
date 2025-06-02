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

import java.math.BigDecimal;
import java.math.RoundingMode;


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

    public BigDecimal addAndRound(Double num1, Double num2) {
        BigDecimal bd1 = BigDecimal.valueOf(num1);
        BigDecimal bd2 = BigDecimal.valueOf(num2);

        BigDecimal result = bd1.add(bd2);
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal subAndRound(Double num1, Double num2) {
        BigDecimal bd1 = BigDecimal.valueOf(num1);
        BigDecimal bd2 = BigDecimal.valueOf(num2);

        BigDecimal result = bd1.subtract(bd2);
        return result.setScale(2, RoundingMode.HALF_UP);
    }
}

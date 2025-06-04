package com.example.myapp.config; // Ensure this package is correct

import com.example.myapp.config.filter.JwtAuthFilter;
import com.example.myapp.security.jwt.AuthEntryPointJwt;
import com.example.myapp.security.services.UserDetailsServiceImpl;
// Remove unused import: import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// Remove unused import: import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// Remove unused import: import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    // Autowire JwtAuthFilter if it's a bean, otherwise use the authenticationJwtTokenFilter() bean method
    // If JwtAuthFilter is already a @Component or @Service, @Autowired is fine.
    // If you're creating it via the @Bean method authenticationJwtTokenFilter(),
    // then you'd typically pass the result of that bean method to addFilterBefore.
    // For simplicity, assuming jwtAuthFilter is correctly injected or created.
    @Autowired
    JwtAuthFilter jwtAuthFilter;


    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // If JwtAuthFilter is not a @Component/@Service and you need to create it as a bean:
    // @Bean
    // public JwtAuthFilter authenticationJwtTokenFilter() {
    //     return new JwtAuthFilter(); // Ensure dependencies are handled if any
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Removed AuthenticationManager from params as it's built later
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <--- ADD THIS LINE FOR CORS
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/auth/**").permitAll() // Combined signup and login
                                .requestMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**").permitAll() // Swagger/OpenAPI
                                // .requestMatchers("/api/v1/auth/**").permitAll() // If you have another auth path
                                .requestMatchers("/api/transaction/**").authenticated() // Secure transaction endpoints
                                .requestMatchers("/api/scope/**").authenticated()       // Secure scope endpoints
                                .requestMatchers("/api/category/**").authenticated()   // Secure category endpoints
                                .requestMatchers("/api/account/**").authenticated()    // Secure account endpoints
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        // Use the autowired jwtAuthFilter or the bean method result
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // OR if using the bean method directly:
        // http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    // --- ADD THIS CORS CONFIGURATION BEAN ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Your frontend URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Optional: How long the results of a preflight request can be cached

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration); // Apply this config to all /api/** paths
        return source;
    }
}
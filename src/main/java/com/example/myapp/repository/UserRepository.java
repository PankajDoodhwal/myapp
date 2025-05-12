package com.example.myapp.repository;

import com.example.myapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    String findByEmailQuery = "SELECT u.* from user u WHERE u.email = :email";
    @Query(value = findByEmailQuery, nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
}

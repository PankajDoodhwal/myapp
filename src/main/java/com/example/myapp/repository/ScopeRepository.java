package com.example.myapp.repository;

import com.example.myapp.model.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
    List<Scope> findByUserId(Long userId);
}

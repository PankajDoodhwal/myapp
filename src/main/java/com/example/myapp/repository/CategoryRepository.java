package com.example.myapp.repository;

import com.example.myapp.model.Category;
import com.example.myapp.model.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Scope> findByScopeId(Long scopeId);
}

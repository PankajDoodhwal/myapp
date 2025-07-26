package com.example.myapp.repository;

import com.example.myapp.model.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
    String getAllScopeByUserIdQuery = "SELECT s.* from scope s WHERE s.user_id = :userId";
    @Query(value = getAllScopeByUserIdQuery, nativeQuery = true)
    List<Scope> getAllScopeByUser(@Param("userId") Long userId);

    String getAllScopeByUserIdAndNameQuery = "SELECT s.* from scope s WHERE s.user_id = :userId AND s.scope_name = :scopeName";
    @Query(value = getAllScopeByUserIdAndNameQuery, nativeQuery = true)
    Scope findScopeByUserAndName(@Param("userId") Long id, @Param("scopeName") String scopeName);

    String getScopeByScopeId = "SELECT s.* from scope s WHERE s.id = :scopeId AND s.user_id = :userId";
    @Query(value = getScopeByScopeId, nativeQuery = true)
    Scope findByScopeIdAndUserId(@Param("scopeId") Long scopeId, @Param("userId") Long userId);
}

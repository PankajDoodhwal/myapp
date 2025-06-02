package com.example.myapp.repository;

import com.example.myapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    String getAllScopeByUserIdAndNameQuery = "SELECT c.* from category c WHERE c.scope_id = :scopeId AND c.name = :categoryName";
    @Query(value = getAllScopeByUserIdAndNameQuery, nativeQuery = true)
    List<Category> findByScopeIdAndName(@Param("scopeId")Long scopeId,
                                        @Param("categoryName")String categoryName);

    String getAllCategoriesByUser = "SELECT c.* from category c WHERE c.scope_id IN (SELECT s.id from scope s where s.user_id = :userId)";
    @Query(value = getAllCategoriesByUser, nativeQuery = true)
    List<Category> getAllCategoriesByUser(@Param("userId") Long id);

    String getAllCategoriesByUserAndScopeID = "SELECT c.* from category c WHERE c.scope_id = :scopeId";
    @Query(value = getAllCategoriesByUserAndScopeID, nativeQuery = true)
    List<Category> getAllCategoriesByUserAndScopeId(@Param("scopeId") Long id);
}

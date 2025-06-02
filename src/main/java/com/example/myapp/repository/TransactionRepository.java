package com.example.myapp.repository;

import com.example.myapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    String getAllAccountByUserIdQuery = "SELECT t.* from transaction t WHERE t.user_id = :userId";
    @Query(value = getAllAccountByUserIdQuery, nativeQuery = true)
    List<Transaction> getAllTransactionByUser(@Param("userId") Long id);
}

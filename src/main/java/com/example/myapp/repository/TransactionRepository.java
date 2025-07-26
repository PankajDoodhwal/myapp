package com.example.myapp.repository;

import com.example.myapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    String getAllTransactionsByUserIdQuery = "SELECT t.* from transaction t WHERE t.user_id = :userId";
    @Query(value = getAllTransactionsByUserIdQuery, nativeQuery = true)
    List<Transaction> getAllTransactionByUser(@Param("userId") Long id);

    String getTransactionByUserIdAndIdQuery = "SELECT t.* from transaction t WHERE t.user_id = :userId && t.id = :id";
    @Query(value = getTransactionByUserIdAndIdQuery, nativeQuery = true)
    Transaction getTransactionById(@Param("userId") Long userId, @Param("id") Long transactionId);
}

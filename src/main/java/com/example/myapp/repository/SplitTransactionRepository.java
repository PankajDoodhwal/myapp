package com.example.myapp.repository;

import com.example.myapp.model.SplitTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SplitTransactionRepository extends JpaRepository<SplitTransaction, Long> {
    String getAllSplitsByUserIdQuery = "SELECT s.* from split_transaction s WHERE s.user_id = :userId";
    @Query(value = getAllSplitsByUserIdQuery, nativeQuery = true)
    List<SplitTransaction> findAllSplitByUserId(@Param("userId") Long userId);

    String getAllSplitsByUserIdAndTxnIdQuery = "SELECT s.* from split_transaction s WHERE s.user_id = :userId AND s.transaction_id = :txnId";
    @Query(value = getAllSplitsByUserIdAndTxnIdQuery, nativeQuery = true)
    List<SplitTransaction> findAllSplitByUserIdAndTxnId(@Param("userId") Long userId, @Param("txnId") Long txnId);
}

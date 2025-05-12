package com.example.myapp.repository;

import com.example.myapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    String getAccountByAccNumberAndUserIdQuery = "SELECT a.* from account a WHERE a.user_id = :userId AND a.account_number = :accountNumber";
    @Query(value = getAccountByAccNumberAndUserIdQuery, nativeQuery = true)
    Account getAccountByAccNumberAndUser(@Param("accountNumber") String accountNumber, @Param("userId") Long userId);


    String getAllAccountByUserIdQuery = "SELECT a.* from account a WHERE a.user_id = :userId";
    @Query(value = getAllAccountByUserIdQuery, nativeQuery = true)
    List<Account> getAllAccountByUserId(@Param("userId") Long userId);
}

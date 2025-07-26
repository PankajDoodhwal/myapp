package com.example.myapp.repository;

import com.example.myapp.model.Friends;
import com.example.myapp.model.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friends, Long> {
    String getAllFriendsByUserIdAndMobileQuery = "SELECT f.* from friends f WHERE f.user_id = :userId AND f.mobile_number = :mobileNumber";
    @Query(value = getAllFriendsByUserIdAndMobileQuery, nativeQuery = true)
    Friends findFriendByUserIsAndMobileNumber(@Param("userId") Long id, @Param("mobileNumber") String mobile);

    String getAllFriendsByUserIdQuery = "SELECT f.* from friends f WHERE f.user_id = :userId";
    @Query(value = getAllFriendsByUserIdQuery, nativeQuery = true)
    List<Friends> findAllFriendsByUserId(@Param("userId") Long userId);
}

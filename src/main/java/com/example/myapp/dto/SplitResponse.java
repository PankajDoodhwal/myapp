package com.example.myapp.dto;

public record SplitResponse(
        Long id,
        Long transaction_id,
        Long PaidByFriend_id,
        Long PaidToFriend_id,
        Double amount,
        Boolean isSettled
) {
}

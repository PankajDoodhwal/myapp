package com.example.myapp.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record CreateSplitRequest(
        Long transactionId,

        @NotNull(message = "Payee (borrower) friend ID is required")
        Long payToFriendId, // the one who is being paid back

        @Size(min = 1, message = "At least one payer entry is required")
        List<SplitShare> shares // List of participants who paid
) {
    public record SplitShare(

            @NotNull(message = "PaidByFriendId is required")
            Long paidByFriendId,

            @NotNull(message = "Amount is required")
            @Positive(message = "Amount must be greater than zero")
            Double amount,

            @NotNull(message = "Settlement status is required")
            Boolean isSettled
    ) {}
}

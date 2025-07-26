package com.example.myapp.dto;

// Add other necessary imports like for date/time if you include them

public record AccountResponse(
        Long id,
        String bankName,
        String accountNumber,
        String ifsc,
        String accountType,
        Double balance
        // You can add other fields like createdDate, modifiedDate if your frontend needs them
        // String createdDate,
        // String modifiedDate
) {
}
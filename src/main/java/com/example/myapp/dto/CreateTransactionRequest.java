package com.example.myapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.Set;

public record CreateTransactionRequest(

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        Double amount,

        @NotBlank(message = "Note is required")
        String note,

        @NotNull(message = "Transaction date is required")
        LocalDateTime txnDate,

        @NotBlank(message = "Transaction type is required (INCOME or EXPENSE)")
        String txnType,

        @NotNull(message = "Scope ID is required")
        Long scopeId,

        @NotNull(message = "Account No. is required")
        String accountNo,

        @NotEmpty(message = "At least one category is required")
        Set<@NotNull Long> categoryIds

) {}

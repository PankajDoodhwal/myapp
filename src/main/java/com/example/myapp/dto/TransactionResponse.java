package com.example.myapp.dto;

import com.example.myapp.common.TransactionType;
import com.example.myapp.model.Category;
import com.example.myapp.model.Scope;
import com.example.myapp.model.Account;

import java.time.LocalDateTime;
import java.util.Set;

public record TransactionResponse(
        Long id,
        Double amount,
        String note,
        LocalDateTime txnDate,
        TransactionType txnType,
        Double myAmount,
        Double settledAmount,
        Double unSettledAmount,
        Boolean isSettled,
        Boolean isSplitted,
        Long scopeId,
        Long bankAccountId,
        Set<Long> categoryIds
) {}


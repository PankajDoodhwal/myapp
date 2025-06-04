package com.example.myapp.dto;

// Assuming TransactionType is an enum like: public enum TransactionType { DEBIT, CREDIT }
// If txnType in your Scope entity is just a String, then use String here.
// However, your Scope entity uses @Enumerated(EnumType.STRING) private TransactionType txnType;
// So, sending it as a String in the DTO is generally fine.

public record ScopeResponse(
        Long id,
        String scopeName,
        String txnType // Or use your TransactionType enum if you want the frontend to handle enum strings
) {
}
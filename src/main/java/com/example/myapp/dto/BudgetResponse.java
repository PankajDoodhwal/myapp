package com.example.myapp.dto;

import com.example.myapp.model.Scope;

public record BudgetResponse(
        Long budgetMonth,
        Long budgetYear,
        Double allocatedBudget,
        Double remainingBudget,
        Double usedBudget,
        Long scopeId
) {}

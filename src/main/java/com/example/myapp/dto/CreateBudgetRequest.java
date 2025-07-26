package com.example.myapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBudgetRequest(
        @NotNull(message = "budget month can not be null")
        Long budgetMonth,

        @NotNull(message = "budget year can not be blank")
        Long budgetYear,

        @NotNull(message = "Allocated budger can not be blank")
        Double allocatedBudget,

        @NotNull(message = "Scope id can not be blank")
        Long scopeId
) {
}

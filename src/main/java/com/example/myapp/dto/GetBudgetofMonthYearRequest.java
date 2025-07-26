package com.example.myapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GetBudgetofMonthYearRequest(
        @NotNull(message = "budgetMonth can not be blank")
        long budgetMonth,

        @NotNull(message = "budgetYear can not be blank")
        long budgetYear
) {
}

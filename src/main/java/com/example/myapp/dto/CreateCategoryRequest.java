package com.example.myapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(
    @NotBlank(message = "Category must not be null")
    String name,

    @NotNull(message = "Scope id must be present in the request")
    Long scope_id
) {
}

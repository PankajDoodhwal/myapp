package com.example.myapp.dto;

import com.example.myapp.model.Scope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ScopeRequest(
        @NotBlank(message = "Scope Name can not be blank")
        String name
) {

}

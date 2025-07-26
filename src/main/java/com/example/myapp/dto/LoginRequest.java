package com.example.myapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password can not be blank")
        @Size(min = 6, max = 20, message = "Password must be between 6 to 20 character!")
        String password
) {
}

package com.example.myapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.message.Message;

import java.util.EnumMap;

public record SignupRequest(
        @NotBlank(message = "Name can not be blank")
        String name,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password can not be blank")
        @Size(min = 6, max = 20, message = "Password must be between 6 to 20 character!")
        String password
) {
}

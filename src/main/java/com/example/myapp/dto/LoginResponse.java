package com.example.myapp.dto;

import com.example.myapp.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "user")
        User user,
        @Schema(description = "JWT token")
        String token
    ) {
}

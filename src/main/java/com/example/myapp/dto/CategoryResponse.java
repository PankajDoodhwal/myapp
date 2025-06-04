package com.example.myapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponse(
        @Schema(description = "categoryId")
        Long id,
        @Schema(description = "user")
        String categoryName,
        @Schema(description = "scopeId")
        String scopeId,
        @Schema(description = "scopeName")
        String scopeName,
        @Schema(description = "transactioType")
        String txntype
) {
}

package com.example.myapp.dto;

import jakarta.validation.constraints.NotBlank;

public record ScopeRequest(
        @NotBlank(message = "Scope Name can not be blank")
        String name,
        
        @NotBlank(message = "Txn Type can not be blank")
        String txntype
) {

}

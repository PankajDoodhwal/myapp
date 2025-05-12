package com.example.myapp.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @NotBlank(message = "Bankname can not be blank")
        String bankName,

        @NotBlank(message = "accountNumber can not be blank")
        String accountNumber,

        @NotBlank(message = "FISC can not be blank")
        String ifsc,

        @NotBlank(message = "AccountType can not be blank")
        String accountType,

        Double balance
) {
}

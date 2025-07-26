package com.example.myapp.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateFriendRequest(
        @NotBlank(message = "Scope Name can not be blank")
        String name,

        @NotBlank(message = "Mobile Number of friend can not be blank")
        String mobileNumber
) {

}

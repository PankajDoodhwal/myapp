package com.example.myapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.java.Log;

import java.util.List;

public record UpdateSplitStatusRequest(
        @NotNull
        Long txnId,

        @NotEmpty(message = "At least one split status update is required")
        List<SplitStatusUpdate> updates

){
        public record SplitStatusUpdate(

                @NotNull(message = "Split ID is required")
                Long splitId,

                @NotNull(message = "Settlement status is required")
                Boolean isSettled
        ) {}
}

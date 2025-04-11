package com.example.myapp.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String traceId;

    public ApiResponse(boolean success, String message, T data, String traceId) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.traceId = traceId;
    }

    public static <T> ApiResponse<T> success(T data, String message, String traceId) {
        return new ApiResponse<>(true, message, data, traceId);
    }

    public static <T> ApiResponse<T> failure(String message, String traceId) {
        return new ApiResponse<>(false, message, null, traceId);
    }

}


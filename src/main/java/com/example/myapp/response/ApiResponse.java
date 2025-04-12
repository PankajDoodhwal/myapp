package com.example.myapp.response;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.service.RequestLogService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String traceId;

    private static final PrettyLogger logger = PrettyLogger.getLogger(ApiResponse.class);

    public ApiResponse(boolean success, String message, T data, String traceId) {
        logger.info("creating final respose:-");
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


package com.oauth2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oauth2.common.exception.ErrorCode;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        ErrorCode code,
        String message,
        Map<String, Object> details
) {
    public static ApiError of(ErrorCode errorCode, String message) {
        return new ApiError(errorCode, message, null);
    }

    public static ApiError of(ErrorCode errorCode, String message, Map<String, Object> details) {
        return new ApiError(errorCode, message, details);
    }

    /**
     * ErrorCode + message + path → details = {"path": path}
     */
    public static ApiError of(ErrorCode errorCode, String message, String path) {
        return new ApiError(
                errorCode,
                message != null ? message : errorCode.getDefaultMessage(),
                Map.of("path", path)
        );
    }
}

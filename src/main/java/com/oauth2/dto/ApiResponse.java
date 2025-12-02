package com.oauth2.dto;

import com.oauth2.common.exception.ErrorCode;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error,
        Instant timestamp,
        String path,
        String requestId
) {

    // ------------------------ success ------------------------
    public static <T> ApiResponse<T> ok(T data, String path, String requestId) {
        return new ApiResponse<>(true, data, null, Instant.now(), path, requestId);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ok(data, null, null);
    }

    public static <T> ApiResponse<T> created(T data, String path, String requestId) {
        return new ApiResponse<>(true, data, null, Instant.now(), path, requestId);
    }

    // ------------------------ fail ------------------------
    public static <T> ApiResponse<T> fail(ApiError error, String path, String requestId) {
        return new ApiResponse<>(false, null, error, Instant.now(), path, requestId);
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message) {
        return fail(ApiError.of(errorCode, message), null, null);
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message, String path, String requestId) {
        return fail(ApiError.of(errorCode, message), path, requestId);
    }


}

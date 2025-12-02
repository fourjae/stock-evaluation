// com.oauth2.common.exception.GlobalExceptionHandler
package com.oauth2.common.exception;

import com.oauth2.dto.ApiError;
import com.oauth2.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1) 비즈니스 예외 (도메인/애플리케이션 예외 전부 여기로)
    // ------------------------ Business Exception ------------------------
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("BusinessException: code={}, message={}", errorCode, ex.getMessage());

        ApiError error = ApiError.of(errorCode, ex.getMessage(), request.getRequestURI());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(error));
    }

    // 2) 검증 예외 (예: @Valid 실패)
    // ------------------------ Validation ------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(err -> err.getDefaultMessage())
                .orElse("요청 값이 올바르지 않습니다.");

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        log.warn("Validation error: {}", message);

        ApiError error = ApiError.of(errorCode, message, request.getRequestURI());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(error));
    }

    // 3) 접근 권한 예외
    // ------------------------ Access Denied ------------------------
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.FORBIDDEN;
        log.warn("Access denied: {}", ex.getMessage());

        ApiError error = ApiError.of(errorCode, ex.getMessage(), request.getRequestURI());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(error));
    }

    // 4) 알 수 없는 예외 (마지막 방어막)
    // ------------------------ Unknown Error ------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Unexpected error", ex);

        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        ApiError error = ApiError.of(errorCode, ex.getMessage(), request.getRequestURI());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(error));
    }
}
